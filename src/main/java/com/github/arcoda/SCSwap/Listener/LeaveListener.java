package com.github.arcoda.SCSwap.Listener;

import com.github.arcoda.SCSwap.SCSwap;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class LeaveListener implements Listener {
    private static SCSwap plugin = SCSwap.getInstance();
    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        if (plugin.smpWorlds.contains(event.getPlayer().getWorld())) {
        	event.setQuitMessage(""); //to avoid any conflicts with Essentials, we remove the join message and send a "fake" join message using .broadcastMessage
        	for (Player p : Bukkit.getOnlinePlayers()) {
        	    p.sendMessage(ChatColor.DARK_GREEN + event.getPlayer().getName() + ChatColor.YELLOW + " left the game.");
        	}
        }
    }
}
