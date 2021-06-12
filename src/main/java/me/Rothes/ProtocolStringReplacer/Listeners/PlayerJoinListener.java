package me.Rothes.ProtocolStringReplacer.Listeners;

import me.Rothes.ProtocolStringReplacer.ProtocolStringReplacer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        ProtocolStringReplacer.getUserManager().loadUser(player);
    }

}