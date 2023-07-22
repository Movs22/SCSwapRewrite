package com.github.arcoda.SCSwap.Listener;

import com.github.arcoda.SCSwap.SCSwap;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

public final class TeleportListener implements Listener {
	private static SCSwap plugin = SCSwap.getInstance();

	@EventHandler
	public void OnTeleport(PlayerTeleportEvent event) {
		if (event.getCause() == TeleportCause.SPECTATE || event.getCause() == TeleportCause.COMMAND) {
			if (event.getTo().getWorld().getName().startsWith("Survival")
					&& !SCSwap.getInstance().isStaff(event.getPlayer())) {
				event.getPlayer().sendMessage(ChatColor.RED + "You can't tp to someone in the SMP.");
				event.setCancelled(true);
			}
		}
	}
}
