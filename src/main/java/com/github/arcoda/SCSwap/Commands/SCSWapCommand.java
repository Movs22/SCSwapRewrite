package com.github.arcoda.SCSwap.Commands;
import com.github.arcoda.SCSwap.SCSwap;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.bukkit.ChatColor;
import java.util.List;

public class SCSWapCommand implements CommandExecutor {
    private SCSwap plugin = SCSwap.getInstance();
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            if ((args[0].equals("reload")) && (sender.hasPermission("scswap.reload") || sender.isOp() || sender instanceof ConsoleCommandSender)) {
                plugin.getTeleportLib.setInventory(YamlConfiguration.loadConfiguration(SCSwap.getInstance().inventoryFile));
                plugin.reloadConfig();
                sender.sendMessage(plugin.prefix + "The config files have been reloaded!");
                return true;
            } else if ((args[0].equals("block")) && (sender.hasPermission("scswap.block") || sender.isOp() || sender instanceof ConsoleCommandSender)) {
                List<String> bannedList = (List<String>) SCSwap.getInstance().getConfig().getList("blocked");
            	if(args.length >= 2) {
    	        	if(args[1].equals("add")) {
            			if(args.length == 2) {
    	        			sender.sendMessage(ChatColor.DARK_RED + "Please send the name of the user to block from using the smp.");
    			        	return true;
            			}
            			bannedList.add(args[2]);
    	        		SCSwap.getInstance().getConfig().set("blocked", bannedList);
    			        SCSwap.getInstance().saveConfig();
            			sender.sendMessage(ChatColor.DARK_RED + args[2] + " no longer can acess the SMP.");
    	        		return true;
            		} else if(args[1].equals("remove")) {
            	  		if(args.length == 2) {
    	        	   		sender.sendMessage(ChatColor.DARK_RED + "Please send the name of the user to unblock from using the smp.");
    			        	return true;
            			}
    	        		bannedList.remove(args[2]);
    			        SCSwap.getInstance().getConfig().set("blocked", bannedList);
            			SCSwap.getInstance().saveConfig();
        				sender.sendMessage(ChatColor.DARK_GREEN + args[2] + " can now acess the SMP!");
    	        		return true;
            		} else if(args[1].equals("list")) {
    	        		sender.sendMessage(ChatColor.GOLD + "The following people are blocked from joining the SMP: " + String.join(", ", bannedList) + ".");
            			return true;
            		}
            	} else {
            		return false;
            	}
            } else if (args[0].equals("about")) {
                sender.sendMessage("§e§lSCSwap§r by §b§lArcoda§r. Thank you for using my plugin :)");
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
