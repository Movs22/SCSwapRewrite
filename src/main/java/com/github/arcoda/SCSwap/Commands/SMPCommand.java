package com.github.arcoda.SCSwap.Commands;
import com.github.arcoda.SCSwap.SCSwap;

import org.bukkit.ChatColor;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SMPCommand implements CommandExecutor {
	
	@Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage("Must be a player to use this command!");
            return true;
        }
        if(SCSwap.getInstance().getConfig().getList("Blocked").contains(((Player)sender).getUniqueId().toString())) {
            sender.sendMessage(ChatColor.RED + "You have been banned from the SMP. Open a ticket if you wish to appeal your ban.");
            return true;
        }  else {
		    return SCSwap.getInstance().getTeleportLib.teleportTo(sender, "Survival");
        }
    }
}
