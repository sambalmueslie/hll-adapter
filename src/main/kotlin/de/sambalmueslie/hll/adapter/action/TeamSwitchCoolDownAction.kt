package de.sambalmueslie.hll.adapter.action


import de.sambalmueslie.hll.adapter.rcon.RconClientService
import de.sambalmueslie.hll.adapter.rcon.api.HllRconClient
import io.micronaut.security.authentication.Authentication
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class TeamSwitchCoolDownAction(clientService: RconClientService) : BaseAction(clientService,logger) {

    companion object {
        val logger: Logger = LoggerFactory.getLogger(TeamSwitchCoolDownAction::class.java)
        const val ID = "TeamSwitchCoolDown"
    }

    override fun getId() = ID


    fun get(auth: Authentication, serverId: Long) = getInt(auth, serverId, "get teamswitchcooldown")

    fun set(auth: Authentication, serverId: Long, coolDown: Int) = execute(auth, serverId, "setteamswitchcooldown $coolDown")
}
