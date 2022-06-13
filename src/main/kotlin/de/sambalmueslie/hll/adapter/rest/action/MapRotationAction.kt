package de.sambalmueslie.hll.adapter.rest.action


import de.sambalmueslie.hll.adapter.rcon.api.HllRconClient
import io.micronaut.security.authentication.Authentication
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class MapRotationAction(private val client: HllRconClient) : BaseAction() {

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(MapRotationAction::class.java)
    }

    override fun getId() = "MapRotation"

    fun get(auth: Authentication): Set<String> {
        check(auth)
        return client.getSet("rotlist")
    }

    fun add(auth: Authentication, name: String): Any {
        check(auth)
        return client.sendCommand("rotadd $name")
    }

    fun remove(auth: Authentication, name: String): Any {
        check(auth)
        return client.sendCommand("rotdel $name")
    }

}
