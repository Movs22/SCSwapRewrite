package com.github.arcoda.SCSwap.Library;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.advancement.AdvancementProgress;
import org.bukkit.block.Block;
import org.bukkit.block.Skull;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import com.github.arcoda.SCSwap.SCSwap;

public class Graveyard {
	private List<ItemStack> drops;
	private Location loc;
	private Boolean open;
	private Player openBy;
	private Player lootOwner;
	public Graveyard(Location l, List<ItemStack> d, Player o) {
		this.loc = l;
		this.drops = d;
		this.open = false;
		this.lootOwner = o;
	    if(l.getBlock().getType() != Material.AIR && l.getBlock().getType() != Material.CAVE_AIR) {
	    	while((l.getBlock().getType() != Material.AIR && l.getBlock().getType() != Material.CAVE_AIR)) {
	    		if(l.getBlockY() > 317) {
	    			SCSwap.getInstance().getServer().getLogger().log(Level.SEVERE, "Failed to generate a graveyard at " + l.getBlockX() + "/" + l.getBlockY() + "/" + l.getBlockZ() + " (" + l.getWorld().getName() + ")");
	    			break;
	    		};
	    		l.add(0, 1, 0);
	    	}
	    }
	    l.getBlock().setType(Material.COARSE_DIRT);
	    l.add(0, 0, 1);
	    l.getBlock().setType(Material.COARSE_DIRT);
	    l.add(0, 1, 0);
		Block b = l.getBlock();
		b.setType(Material.PLAYER_HEAD);
		Skull sd = (Skull) b.getState();
	    sd.setOwnerProfile(o.getPlayerProfile());
	    sd.update();
	    if(this.drops.size() == 0) {
			return;
		}
	    SCSwap.getInstance().graveyardLocs.put(l.clone(), this);
	    if(SCSwap.getInstance().graveyards.containsKey(o)) {
	    	o.sendMessage("§cYou haven't reclaimed your previous loot. It has been destroyed.");
	    }
		SCSwap.getInstance().graveyards.put(o, this);
	}
	
	public void destroy() {
		this.drops.forEach(a -> {
			this.loc.getWorld().dropItem(this.loc, a);
		});
		SCSwap.getInstance().graveyardLocs.remove(this.loc);
		SCSwap.getInstance().graveyards.remove(this.lootOwner);
	}
	
	public void updateItemstack(ItemStack[] a) {
		this.drops = Arrays.asList(a);
		if(this.drops.size() == 0) {
			this.destroy();
			return;
		}
	}
	
	public void closeLoot(Player p) {
		if(this.openBy.equals(p)) this.open = false;
	}
	
	public void openLoot(Player p) {
		p.sendMessage("§cGraveyards have been disabled");
		return;
	}
}
