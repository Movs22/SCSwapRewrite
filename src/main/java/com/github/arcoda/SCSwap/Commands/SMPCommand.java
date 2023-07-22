package com.github.arcoda.SCSwap.Commands;
import com.github.arcoda.SCSwap.SCSwap;

import org.bukkit.ChatColor;

import java.io.IOException;
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
        Player p = (Player) sender;
        if(SCSwap.getInstance().isStaff(p)) {
        	sender.sendMessage(ChatColor.RED + "You can't run this command while in SMP Staff Mode.");
        	return true;
        }
		try {
			return SCSwap.getInstance().getTeleportLib.teleportTo(sender, "Survival");
		} catch (IOException e) {
			e.printStackTrace();
			return true;
        }
    }
}
