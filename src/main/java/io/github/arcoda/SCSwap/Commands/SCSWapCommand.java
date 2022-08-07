package io.github.arcoda.SCSwap.Commands;
import io.github.arcoda.SCSwap.SCSwap;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

public class SCSWapCommand implements CommandExecutor {
    private SCSwap plugin = SCSwap.getInstance();
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            if ((args[0].equals("reload")) && (sender.hasPermission("scswap.reload") || sender.isOp() || sender instanceof ConsoleCommandSender)) {
                plugin.getTeleportLib.setInventory(YamlConfiguration.loadConfiguration(SCSwap.getInstance().inventory));
                sender.sendMessage(plugin.prefix+"The config file has been reloaded!");
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