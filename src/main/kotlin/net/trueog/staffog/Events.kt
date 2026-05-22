package net.trueog.staffog

import net.kyori.adventure.text.Component
import net.trueog.staffog.grpc.endpoint.IpCheckEndpoint
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerPreLoginEvent

class Events : Listener {
    @EventHandler
    fun onPlayerPreLogin(event: AsyncPlayerPreLoginEvent) {
        IpCheckEndpoint.checkIpMap[event.playerProfile.id]?.let {
            it.complete(event.address.hostAddress)
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, Component.text("IP sent to Staff-Auth"))
        }
    }
}