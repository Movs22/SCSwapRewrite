package com.github.arcoda.SCSwap.Commands;
import com.github.arcoda.SCSwap.SCSwap;
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
        return SCSwap.getInstance().getTeleportLib.teleportTo(sender, "Creative");
    }
}
