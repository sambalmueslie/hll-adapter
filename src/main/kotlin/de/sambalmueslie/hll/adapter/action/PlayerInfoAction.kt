package de.sambalmueslie.hll.adapter.action


import de.sambalmueslie.hll.adapter.rcon.api.HllRconClient
import de.sambalmueslie.hll.adapter.rest.api.PlayerInfo
import de.sambalmueslie.hll.adapter.rest.api.PlayerScore
import io.micronaut.security.authentication.Authentication
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class PlayerInfoAction(private val client: HllRconClient) : BaseAction(logger) {


    companion object {
        val logger: Logger = LoggerFactory.getLogger(PlayerInfoAction::class.java)
        const val ID = "PlayerInfo"
        private val SEPERATOR = ": ".toRegex()
    }

    override fun getId() = ID


    fun get(auth: Authentication, player: String) = execute(auth, client, "playerinfo $player").let { parsePlayerInfo(it) }

    private fun parsePlayerInfo(content: String): Any {
        val values = content.split("\n")
            .map { it.split(SEPERATOR, 2) }
            .associate { it.getOrEmpty(0) to it.getOrEmpty(1) }

        val name = values["Name"] ?: "Unknown"
        val steamId = values["steamID64"] ?: "Unknown"
        val team = values["Team"] ?: "Unknown"
        val role = values["Role"] ?: "Unknown"
        val unit = values["Unit"] ?: "Unknown"
        val loadout = values["Loadout"] ?: "Unknown"
        val kd = values["Kills"]?.split(" - ") ?: emptyList()
        val kills = kd.getOrNull(0)?.toIntOrNull() ?: -1
        val deaths = kd.getOrNull(0)?.substringAfter(": ")?.toIntOrNull() ?: -1
        val score = parsePlayerScore(values["Score"])
        val level = values["Level"]?.toIntOrNull() ?: -1

        return PlayerInfo(name, steamId, team, role, unit, loadout, kills, deaths, score, level)
    }

    private fun parsePlayerScore(content: String?): PlayerScore {
        if (content == null) return PlayerScore(0, 0, 0, 0)
        val values = content.split(", ")
            .map { it.split(" ") }
            .associate { it.getOrEmpty(0) to it.getOrEmpty(1) }

        val combat: Int = values.getOrDefault("C", "0").toIntOrNull() ?: 0
        val offensive: Int = values.getOrDefault("O", "0").toIntOrNull() ?: 0
        val defensive: Int = values.getOrDefault("D", "0").toIntOrNull() ?: 0
        val support: Int = values.getOrDefault("S", "0").toIntOrNull() ?: 0
        return PlayerScore(combat, offensive, defensive, support)
    }

}

fun List<String>.getOrEmpty(index: Int): String {
    val value = getOrNull(index) ?: return ""
    return value.trim()
}
