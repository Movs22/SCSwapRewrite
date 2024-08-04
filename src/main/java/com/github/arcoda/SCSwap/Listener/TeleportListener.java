package com.github.arcoda.SCSwap.Listener;

import com.github.arcoda.SCSwap.SCSwap;
import com.github.arcoda.SCSwap.Library.Graveyard;

import net.md_5.bungee.api.ChatColor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public final class TeleportListener implements Listener {

	private Location endPortal = new Location(SCSwap.getInstance().getServer().getWorld("Survival1_the_end"), 0, 63, 0);
	
	@EventHandler
	public void OnTeleport(PlayerTeleportEvent event) {
		if (event.getCause() == TeleportCause.SPECTATE || event.getCause() == TeleportCause.COMMAND) {
			if ((event.getTo().getWorld().getName().startsWith("Survival")
					|| event.getFrom().getWorld().getName().startsWith("Survival")) && !SCSwap.getInstance().isStaff(event.getPlayer())) {
				event.getPlayer().sendMessage(ChatColor.RED + "You can't tp to someone in the SMP.");
				event.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void OnPortalTeleport(PlayerPortalEvent event) {
		if(event.getFrom().getWorld().getName().endsWith("the_end") && event.getTo().getWorld().getName().startsWith("Survival1")) {
			event.setCancelled(true);
			event.getPlayer().teleport(event.getPlayer().getBedLocation());
		}
	}
	
	@EventHandler
	public void OnVoidDamage(EntityDamageEvent event) {
		if(event.getCause().equals(DamageCause.VOID)) {
			Player p = (Player) event.getEntity();
			event.setCancelled(true);
			p.setFallDistance(0);
			p.setVelocity(new Vector(0, 0, 0));
			p.teleport(endPortal);
			p.sendMessage("§aLakitu has fished you out of the void!");
			event.setCancelled(true);
		}
	}
	
}
