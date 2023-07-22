package com.github.arcoda.SCSwap.Commands;
import com.github.arcoda.SCSwap.SCSwap;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.bukkit.ChatColor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SCSWapCommand implements CommandExecutor {
    private SCSwap plugin = SCSwap.getInstance();
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length >= 1) {
            if ((args[0].equals("reload")) && (sender.hasPermission("scswap.reload") || sender.isOp() || sender instanceof ConsoleCommandSender)) {
                plugin.getTeleportLib.setInventory(YamlConfiguration.loadConfiguration(SCSwap.getInstance().cmpInvFile), "Creative");
                plugin.getTeleportLib.setInventory(YamlConfiguration.loadConfiguration(SCSwap.getInstance().smpInvFile), "Survival");
                plugin.reloadConfig();
                sender.sendMessage(plugin.prefix + "The config files have been reloaded!");
                return true;
            } else if ((args[0].equals("staffmode")) &&  sender.isOp() && (sender instanceof Player)) {
            	Player p = (Player) sender;
            	if(plugin.isStaff(p)) {
            		sender.sendMessage(ChatColor.GOLD + "SMP Staff Mode has been"  + ChatColor.RED + " disabled" + ChatColor.GOLD + ".");
            		plugin.disableStaff(p);
            		return true;
            	} else {
            		sender.sendMessage(ChatColor.GOLD + "SMP Staff Mode has been"  + ChatColor.DARK_GREEN + " enabled" + ChatColor.GOLD + ".");
            		plugin.enableStaff(p);
            		return true;
            	}
            } else if ((args[0].equals("reset")) && args.length > 1 &&  sender.isOp() && (sender instanceof Player) && sender.hasPermission("scswap.reset")) {
            	Player p = (Player) sender;
            	if(plugin.isStaff(p)) {
            		Player a = sender.getServer().getPlayer(args[1]);
            		if(a.getUniqueId() == null) {
            			sender.sendMessage(ChatColor.RED + "Player is invalid or isn't online.");
            			return true;
            		}
            		a.sendMessage(ChatColor.GREEN + "Reseting your SMP data...");
            		if(a.hasPermission("scswap.cmp")) {
            			try {
            				SCSwap.getInstance().getTeleportLib.teleportTo(a, "Creative");
            			} catch (IOException e) {
            				// 	TODO Auto-generated catch block
            				e.printStackTrace();
            				a.sendMessage(ChatColor.RED + "FAILED");
            				sender.sendMessage(ChatColor.RED + "Failed to teleport player to the CMP.");
            				return true;
            			}
            		}
            		YamlConfiguration s = SCSwap.getInstance().getTeleportLib.getInventory("Survival");
            		s.set(a.getUniqueId().toString(), null);
            		SCSwap.getInstance().getTeleportLib.setInventory(s, "Survival");
            		a.sendMessage(ChatColor.GREEN + "Reseted your SMP data.");
            		sender.sendMessage(ChatColor.GREEN + "Sucess!");
            		return true;
            	} else {
            		sender.sendMessage(ChatColor.GOLD + "You must be in " + ChatColor.BOLD + "Staff Mode" + ChatColor.RESET + "" + ChatColor.GOLD + " to reset someone's inventory.");
            		return true;
            	}
            }else if (args[0].equals("about")) {
                sender.sendMessage("§e§lSCSwap§r by §b§lArcoda§r and rewritten by §x§f§f§0§0§0§0§lMovies22§r. Thank you for using my plugin :)");
                return true;
            } else {
                return(invalid(sender));
            }
        } else {
            return(invalid(sender));
        }
    }
    private boolean invalid(CommandSender sender) {
        sender.sendMessage(plugin.prefix+"§4Invalid argument(s)!");
        return true;
    }
}
