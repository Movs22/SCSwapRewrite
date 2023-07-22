package com.github.arcoda.SCSwap.Listener;

import com.github.arcoda.SCSwap.SCSwap;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {
    private static SCSwap plugin = SCSwap.getInstance();
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if (plugin.smpWorlds.contains(event.getPlayer().getWorld())) {
        	event.setJoinMessage(""); //to avoid any conflicts with Essentials, we remove the join message and send a "fake" join message using .broadcastMessage
        	for (Player p : Bukkit.getOnlinePlayers()) {
        	    p.sendMessage(ChatColor.DARK_GREEN + event.getPlayer().getName() + ChatColor.YELLOW + " joined the game.");
        	}
        	Player player = event.getPlayer();
        	plugin.nametagAPI.setPrefix(player, "&2[SMP] &a");
			if(player.hasPermission("scswap.manager")) {
				plugin.nametagAPI.setPrefix(player, "&2[SMP] &9");
			}
			if(player.hasPermission("scswap.mayor")) {
				plugin.nametagAPI.setPrefix(player, "&2[SMP] &3");
			}
        } else {
        	plugin.nametagAPI.clearNametag(event.getPlayer());
        }
    }
}
