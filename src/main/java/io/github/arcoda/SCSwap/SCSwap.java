package io.github.arcoda.SCSwap;

import io.github.arcoda.SCSwap.Listener.TeleportListener;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public final class SCSwap extends JavaPlugin {
    //private Logger log;
    public static Plugin getPlugin;
    public static Logger log;
    public static FileConfiguration Config;
    public static LuckPerms getLuckPerms;
    @Override
    public void onEnable() {
        getPlugin = Bukkit.getServer().getPluginManager().getPlugin("SCSwap");
        log = getPlugin.getLogger();
        Config = getPlugin.getConfig();
        getLuckPerms = LuckPermsProvider.get();
        loadConfiguration();
        registerListener(new TeleportListener());
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(getPlugin);
    }

    public static void devLog(String text) {
        if (Config.getBoolean("Debug")) {
            log.info(text);
        }
    }
    private void registerListener(Listener listener) {
        getServer().getPluginManager().registerEvents(listener, this);
    }

    private void loadConfiguration() {
        Config.addDefault("Portal.To", "TO_SMP");
        Config.addDefault("Portal.From", "FROM_SMP");
        Config.addDefault("Debug", false);
        Config.options().copyDefaults(true);
        getPlugin.saveConfig();
    }
}
