package com.github.arcoda.SCSwap.Listener;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.advancement.AdvancementProgress;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import com.github.arcoda.SCSwap.SCSwap;
import com.github.arcoda.SCSwap.Library.Graveyard;

public class InventoryListener implements Listener {
	private static SCSwap plugin = SCSwap.getInstance();
	@EventHandler
	public void onInventoryMove(InventoryClickEvent event) {
		if(event.getView().getTitle().endsWith("'s loot")) {
			if(event.getViewers().get(0).getGameMode() == GameMode.CREATIVE) {
				event.setCancelled(true);
			} else {
				String player = event.getView().getTitle().split("'s loot")[0];
				Player p = SCSwap.getInstance().getServer().getPlayerExact(player);
				Graveyard a = SCSwap.getInstance().graveyards.get(p);
				a.updateItemstack(event.getInventory().getContents());
			}
		};
		if(event.getCurrentItem() != null && event.getCurrentItem().getType().equals(Material.NETHER_STAR) && event.getCurrentItem().getItemMeta().getDisplayName().equals("§dRemote Graveyard")) {
			if(event.getClickedInventory().getType() == InventoryType.ANVIL) {
				event.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onInventoryClose(InventoryCloseEvent event) {
		if(event.getView().getOriginalTitle().endsWith("'s loot")) {
			String player = event.getView().getTitle().split("'s loot")[0];
			Player p = SCSwap.getInstance().getServer().getPlayerExact(player);
			Graveyard a = SCSwap.getInstance().graveyards.get(p);
			a.closeLoot((Player) event.getPlayer());
		};
	}
	
	@EventHandler
	public void onSkullClick(PlayerInteractEvent event) {
		if(event.getHand() == EquipmentSlot.OFF_HAND) return;
		if(event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.LEFT_CLICK_BLOCK) {
			if(plugin.graveyardLocs.get(event.getClickedBlock().getLocation()) != null) {
				Graveyard a = plugin.graveyardLocs.get(event.getClickedBlock().getLocation());
				a.openLoot(event.getPlayer());
				event.setCancelled(true);
				return;
			} else if(event.getItem() != null && event.getItem().getItemMeta().getDisplayName().equals("§dRemote Graveyard")) {
				Graveyard a = plugin.graveyards.get(event.getPlayer());
				if(a == null) {
					event.getPlayer().sendMessage("§cYou don't have any graveyards.");
					return;
				}
				Player p = event.getPlayer();
				SCSwap.getInstance().getServer().dispatchCommand(SCSwap.getInstance().getServer().getConsoleSender(), "advancement grant " +p.getName() + " only minecraft:adventure/remote_graveyard");
				a.openLoot(p);
				ItemMeta b = event.getItem().getItemMeta();
				Damageable c = (Damageable) b;
				c.setDamage(c.getDamage() + 1);
				event.getItem().setItemMeta(c);
				event.setCancelled(true);
				return;
			}
		} else if(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_AIR) {
			if(event.getItem().getItemMeta().getDisplayName().equals("§dRemote Graveyard")) {
				Graveyard a = plugin.graveyards.get(event.getPlayer());
				if(a == null) {
					event.getPlayer().sendMessage("§cYou don't have any graveyards.");
					return;
				}
				Player p = event.getPlayer();
				SCSwap.getInstance().getServer().dispatchCommand(SCSwap.getInstance().getServer().getConsoleSender(), "advancement grant " +p.getName() + " only minecraft:adventure/remote_graveyard");
				a.openLoot(p);
				ItemMeta b = event.getItem().getItemMeta();
				Damageable c = (Damageable) b;
				c.setDamage(c.getDamage() + 1);
				event.setCancelled(true);
				return;
			}
		}
	}
	
	public void onGraveyardDestroy(BlockBreakEvent event) {
		System.out.println("Block destroyed at " + event.getBlock().getLocation().getX() + "/" + event.getBlock().getLocation().getY() + "/" + event.getBlock().getLocation().getZ());
		if(plugin.graveyardLocs.get(event.getBlock().getLocation()) != null) {
			Graveyard a = plugin.graveyardLocs.get(event.getBlock().getLocation());
			a.destroy();
			event.setCancelled(true);
		}
	}
}
