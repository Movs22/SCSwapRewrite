package com.github.arcoda.SCSwap.Commands;
import com.github.arcoda.SCSwap.SCSwap;

import net.md_5.bungee.api.ChatColor;

import java.io.IOException;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CMPCommand implements CommandExecutor {
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
        if(p.getHealth() < 12.0f && p.getFoodLevel() < 18) {
        	sender.sendMessage(ChatColor.RED + "You can't run to the CMP like that. Please try again when you have more health.");
        	return true;
        }
        try {
			return SCSwap.getInstance().getTeleportLib.teleportTo(sender, "Creative");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return true;
		}
    }
}
