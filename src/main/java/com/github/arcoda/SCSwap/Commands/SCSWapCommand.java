package com.github.arcoda.SCSwap.Commands;
import com.github.arcoda.SCSwap.SCSwap;
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
                plugin.getTeleportLib.setInventory(YamlConfiguration.loadConfiguration(SCSwap.getInstance().inventoryFile));
                plugin.reloadConfig();
                sender.sendMessage(plugin.prefix + "The config files have been reloaded!");
                return true;
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