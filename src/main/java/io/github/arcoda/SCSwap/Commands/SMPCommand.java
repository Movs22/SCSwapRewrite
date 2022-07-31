package io.github.arcoda.SCSwap.Commands;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import static io.github.arcoda.SCSwap.Commands.TeleportLibrary.teleportTo;

public class SMPCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return teleportTo(sender, "Survival");
    }
}
