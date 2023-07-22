package com.github.arcoda.SCSwap.Library;

import com.github.arcoda.SCSwap.SCSwap;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.GameRule;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.advancement.Advancement;
import org.bukkit.advancement.AdvancementProgress;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class TeleportLibrary {
	private SCSwap plugin = SCSwap.getInstance();
	// private MultiverseCore MV = JavaPlugin.getPlugin(MultiverseCore.class);
	// private MultiversePortals MVPortals =
	// JavaPlugin.getPlugin(MultiversePortals.class);
	// private MVPortal portal;
	private LuckPerms perms = plugin.getLuckPerms;
	private YamlConfiguration survival;
	private YamlConfiguration creative;

	public boolean teleportTo(CommandSender sender, @NotNull String mode) throws IOException {
		if (!(sender instanceof Player))
			return false;
		Player player = ((Player) sender);
		player = player.getPlayer();
		User user = perms.getPlayerAdapter(Player.class).getUser(player);
		if (mode == "Survival") {
			if (player.isInsideVehicle()) {
				sender.sendMessage(ChatColor.RED + "Failed to teleport to the SMP. Are you riding an entity?");
				return true;
			}
			plugin.nametagAPI.setPrefix(player, "&2[SMP] &a");
			if(player.hasPermission("scswap.manager")) {
				plugin.nametagAPI.setPrefix(player, "&2[SMP] &9");
			}
			if(player.hasPermission("scswap.mayor")) {
				plugin.nametagAPI.setPrefix(player, "&2[SMP] &3");
			}
			// CMP Effects
			String effects = EffectLibrary.potionsToString(player.getActivePotionEffects());
			if (effects != null) {
				creative.set(player.getUniqueId() + ".Effects", effects);
			}

			// CMP Inventory
			String inv = InventoryLibrary.toBase64(player.getInventory());
			if (inv != null) {
				creative.set(player.getUniqueId() + ".Inventory", inv);
			}
			String einv = InventoryLibrary.toBase64(player.getEnderChest());
			if (einv != null) {
				creative.set(player.getUniqueId() + ".EnderChest", einv);
			}
			
			String oinv = InventoryLibrary.offHandtoBase64(player.getInventory().getItemInOffHand());
			if (oinv != null) {
				creative.set(player.getUniqueId() + ".Offhand", oinv);
			}

			// CMP XP
			creative.set(player.getUniqueId() + ".Experience", "" + player.getExp());
			// CMP Health/Hunger
			creative.set(player.getUniqueId() + ".Health", player.getHealth());
			creative.set(player.getUniqueId() + ".Hunger", player.getFoodLevel());
			// Username
			creative.set(player.getUniqueId() + ".User", sender.getName());
			// CMP Location
			creative.set(player.getUniqueId() + ".Location", LocationLibrary.toString(player.getLocation()));
			// CMP Bed/Respawn Location
			if (player.getBedSpawnLocation() != null) {
				creative.set(player.getUniqueId() + ".Spawn", LocationLibrary.toString(player.getBedSpawnLocation()));
			} else {
				creative.set(player.getUniqueId() + ".Spawn", " ");
			}
			Boolean a = false;
			// SMP Location/Spawn
			if (survival.getString(player.getUniqueId() + ".Location") == null) {
				player.teleport(LocationLibrary.toLocation("Survival1•175•65•13"));
				a = true;
			} else {
				String loc = survival.getString(player.getUniqueId() + ".Location");
				if (loc.startsWith("Survival")) {
					player.teleport(LocationLibrary.toLocation(loc));
				} else {
					player.teleport(LocationLibrary.toLocation("Survival1•175•65•13"));
				}
			}
			if (survival.getString(player.getUniqueId() + ".Spawn") == null) {
				player.setBedSpawnLocation(LocationLibrary.toLocation("Survival1•175•65•13"));
			} else {
				String loc = survival.getString(player.getUniqueId() + ".Spawn");
				if (loc.startsWith("Survival")) {
					player.setBedSpawnLocation(LocationLibrary.toLocation(loc));
				} else {
					player.setBedSpawnLocation(LocationLibrary.toLocation("Survival1•175•65•13"));
				}
			}

			
			for (PotionEffect p : player.getActivePotionEffects()) {
				player.removePotionEffect(p.getType());
			}

			// CMP Fly/Walk Speeds
			creative.set(player.getUniqueId() + ".FlySpeed", player.getFlySpeed());
			creative.set(player.getUniqueId() + ".WalkSpeed", player.getWalkSpeed());
			// Luck Perms
			// 1) Green Nick/Default group override
			user.data().add(Node.builder("group.survival").value(true).build());
			// 2) Operator permissions
			if (player.hasPermission("group.operators")) {
				user.data().add(Node.builder("scswap.operators").value(true).build());
				user.data().remove(Node.builder("group.operators").build());
			}
			// 3) [Unused] /fly permissions
			if (player.hasPermission("group.allow-fly")) {
				user.data().add(Node.builder("scswap.allow-fly").value(true).build());
				user.data().remove(Node.builder("group.allow-fly").build());
			}
			// 4) Worldedit permissions
			if (player.hasPermission("group.worldedit-full")) {
				user.data().add(Node.builder("scswap.worldedit-full").value(true).build());
				user.data().remove(Node.builder("group.worldedit-full").build());
			}
			// Updates /smp and /cmp perms
			user.data().add(Node.builder("scswap.cmp").value(true).build());
			user.data().add(Node.builder("scswap.smp").value(false).build());
			// Saves Luck Perms
			perms.getUserManager().saveUser(user);

			// Disables gamemode, flight and changes gamemode to survival
			player.setOp(false);
			player.setGameMode(GameMode.SURVIVAL);
			player.setAllowFlight(false);
			player.setFlying(false);
			// Clears inventory
			player.getInventory().clear();
			// SMP Health/Hunger
			if (survival.getInt(player.getUniqueId() + ".Health") > 0.1) {
				player.setHealth(survival.getInt(player.getUniqueId() + ".Health"));
			} else {
				player.setHealth(20.0f);
			}
			if (survival.getInt(player.getUniqueId() + ".Hunger") > 0.1) {
				player.setFoodLevel(survival.getInt(player.getUniqueId() + ".Hunger"));
			} else {
				player.setFoodLevel(20);
			}
			// SMP Experience
			if (survival.getString(player.getUniqueId() + ".Experience") != null) {
				player.setExp(Float.parseFloat(survival.getString(player.getUniqueId() + ".Experience")));
			} else {
				player.setExp(0.0f);
			}

			// SMP Walk/FLight speeds
			player.setFlySpeed(0.2f);
			player.setWalkSpeed(0.2f);
			// SMP Inventory/EnderChest
			if (survival.getString(player.getUniqueId() + ".Inventory") != null) {
				InventoryLibrary.loadInv(survival.getString(player.getUniqueId() + ".Inventory"), player);
			}
			if (a) {
				player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BELL, 1, 1);
				ItemStack book = InventoryLibrary.getDefaultBook();
				player.getInventory().addItem(book);
				PotionEffect e = new PotionEffect(PotionEffectType.SATURATION, 6000, 255);
				player.addPotionEffect(e);
				player.sendMessage(ChatColor.GREEN + "Welcome to the SMP. You've received " + ChatColor.DARK_GREEN
						+ "Saturation" + ChatColor.GREEN + " for " + ChatColor.DARK_GREEN + "5 minutes" + ChatColor.GREEN
						+ ". After those 5 minutes run out, you'll need to get food by yourself." + ChatColor.GOLD
						+ " Enjoy the SMP!");
			}
			if (survival.getString(player.getUniqueId() + ".EnderChest") != null) {
				InventoryLibrary.loadEnderChest(survival.getString(player.getUniqueId() + ".EnderChest"), player);
			}
			
			if (survival.getString(player.getUniqueId() + ".Offhand") != null) {
				InventoryLibrary.loadOffHand(survival.getString(player.getUniqueId() + ".Offhand"), player);
			}
			// SMP Potions
			if (survival.getString(player.getUniqueId() + ".Effects") != null) {
				EffectLibrary.loadPotions(survival.getString(player.getUniqueId() + ".Effects"), player);
			}
			try {
				creative.save("./plugins/SCSwap/creative.yml");
				survival.save("./plugins/SCSwap/survival.yml");
			} catch (IOException e) {
				e.printStackTrace();
			}
			return true;
		} else if (mode == "Creative") {
			if (player.isInsideVehicle()) {
				sender.sendMessage(ChatColor.RED + "Failed to teleport to the SMP. Are you riding an entity?");
				return true;
			}
			plugin.nametagAPI.clearNametag(player);
			// SMP Effects
			String effects = EffectLibrary.potionsToString(player.getActivePotionEffects());
			if (effects != null) {
				survival.set(player.getUniqueId() + ".Effects", effects);
			}
			
			// SMP Inventory
			String inv = InventoryLibrary.toBase64(player.getInventory());
			if (inv != null) {
				survival.set(player.getUniqueId() + ".Inventory", inv);
			}
			String einv = InventoryLibrary.toBase64(player.getEnderChest());
			if (einv != null) {
				survival.set(player.getUniqueId() + ".EnderChest", einv);
			}
			
			String oinv = InventoryLibrary.offHandtoBase64(player.getInventory().getItemInOffHand());
			if (oinv != null) {
				survival.set(player.getUniqueId() + ".Offhand", oinv);
			}

			// SMP XP
			survival.set(player.getUniqueId() + ".Experience","" + player.getExp());
			// SMP Health/Hunger
			survival.set(player.getUniqueId() + ".Health", player.getHealth());
			survival.set(player.getUniqueId() + ".Hunger", player.getFoodLevel());
			// Username
			survival.set(player.getUniqueId() + ".User", sender.getName());
			// SMP Location
			survival.set(player.getUniqueId() + ".Location", LocationLibrary.toString(player.getLocation()));
			// SMP Bed/Respawn Location
			if (player.getBedSpawnLocation() != null) {
				survival.set(player.getUniqueId() + ".Spawn", LocationLibrary.toString(player.getBedSpawnLocation()));
			} else {
				survival.set(player.getUniqueId() + ".Spawn", "Survival1•175•65•13");
			}
			// CMP Location/Spawn
			if (creative.getString(player.getUniqueId() + ".Location") == null) {
				player.teleport(LocationLibrary.toLocation("Main1•133•67•351"));
			} else {
				String loc = creative.getString(player.getUniqueId() + ".Location");
				if (loc.startsWith("Main1")) {
					player.teleport(LocationLibrary.toLocation(loc));
				} else {
					player.teleport(LocationLibrary.toLocation("Main1•133•67•351"));
				}
			}
			if (creative.getString(player.getUniqueId() + ".Spawn") == null) {
				player.setBedSpawnLocation(LocationLibrary.toLocation("Main1•133•67•351"));
			} else {
				String loc = creative.getString(player.getUniqueId() + ".Spawn");
				if (loc.startsWith("Main1")) {
					player.setBedSpawnLocation(LocationLibrary.toLocation(loc));
				} else {
					player.setBedSpawnLocation(LocationLibrary.toLocation("Main1•133•67•351"));
				}
			}

			for (PotionEffect p : player.getActivePotionEffects()) {
				player.removePotionEffect(p.getType());
			}
			
			// Luck Perms
			// 1) Green Nick/Default group override
			user.data().remove(Node.builder("group.survival").build());
			// 2) Operator permissions
			if (player.hasPermission("scswap.operators")) {
				player.setOp(true);
				user.data().add(Node.builder("group.operators").value(true).build());
				user.data().remove(Node.builder("scswap.operators").build());
			}
			// 3) [Unused] /fly permissions
			if (player.hasPermission("scswap.allow-fly")) {
				user.data().remove(Node.builder("scswap.allow-fly").build());
			}
			// 4) Worldedit permissions
			if (player.hasPermission("scswap.worldedit-full")) {
				user.data().add(Node.builder("group.worldedit-full").value(true).build());
				user.data().remove(Node.builder("scswap.worldedit-full").build());
			}
			// Updates /smp and /cmp perms
			user.data().add(Node.builder("scswap.cmp").value(false).build());
			user.data().add(Node.builder("scswap.smp").value(true).build());
			// Saves Luck Perms
			perms.getUserManager().saveUser(user);

			// Reenables flight and changes gamemode to creative
			player.setGameMode(GameMode.CREATIVE);
			player.setAllowFlight(true);
			player.setFlying(true);
			// Clears inventory
			player.getInventory().clear();
			// CMP Health/Hunger
			if (creative.getInt(player.getUniqueId() + ".Health") > 0.1) {
				player.setHealth(creative.getInt(player.getUniqueId() + ".Health"));
			} else {
				player.setHealth(20.0f);
			}
			if (creative.getInt(player.getUniqueId() + ".Hunger") > 0.1) {
				player.setFoodLevel(creative.getInt(player.getUniqueId() + ".Hunger"));
			} else {
				player.setFoodLevel(10);
			}
			// CMP Experience
			if (creative.getString(player.getUniqueId() + ".Experience") != null) {
				player.setExp(Float.parseFloat(creative.getString(player.getUniqueId() + ".Experience")));
			} else {
				player.setExp(0.0f);
			}
			if (creative.getString(player.getUniqueId() + ".Offhand") != null) {
				InventoryLibrary.loadOffHand(creative.getString(player.getUniqueId() + ".Offhand"), player);
			}

			// CMP Walk/FLight speeds
			if (creative.getInt(player.getUniqueId() + ".WalkSpeed") > 0.2) {
				player.setWalkSpeed(creative.getInt(player.getUniqueId() + ".WalkSpeed"));
			} else {
				player.setWalkSpeed(0.2f);
			}
			if (creative.getInt(player.getUniqueId() + ".FlySpeed") > 0.2) {
				player.setWalkSpeed(creative.getInt(player.getUniqueId() + ".FlySpeed"));
			} else {
				player.setWalkSpeed(0.2f);
			}
			// CMP Inventory/EnderChest
			if (creative.getString(player.getUniqueId() + ".Inventory") != null) {
				InventoryLibrary.loadInv(creative.getString(player.getUniqueId() + ".Inventory"), player);
			}
			if (creative.getString(player.getUniqueId() + ".EnderChest") != null) {
				InventoryLibrary.loadEnderChest(creative.getString(player.getUniqueId() + ".EnderChest"), player);
			}
			// CMP Potions
			if (creative.getString(player.getUniqueId() + ".Effects") != null) {
				EffectLibrary.loadPotions(creative.getString(player.getUniqueId() + ".Effects"), player);
			}
			try {
				creative.save("./plugins/SCSwap/creative.yml");
				survival.save("./plugins/SCSwap/survival.yml");
			} catch (IOException e) {
				e.printStackTrace();
			}
			return true;
		} else {
			return false;
		}
	}
	
	public void setInventory(YamlConfiguration i, String s) {
		if (s == "Survival") {
			survival = i;
		} else if (s == "Creative") {
			creative = i;
		}
	}

	public YamlConfiguration getInventory(String s) {
		if (s == "Survival") {
			return survival;
		} else if (s == "Creative") {
			return creative;
		}
		return null;
	}

}