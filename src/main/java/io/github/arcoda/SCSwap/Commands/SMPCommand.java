package io.github.arcoda.SCSwap.Commands;
import io.github.arcoda.SCSwap.SCSwap;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SMPCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return SCSwap.getInstance().getTeleportLib.teleportTo(sender, "Survival");
    }
}
