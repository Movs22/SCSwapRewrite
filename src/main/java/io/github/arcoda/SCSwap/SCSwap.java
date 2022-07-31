package io.github.arcoda.SCSwap;

import io.github.arcoda.SCSwap.Listener.TeleportListener;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public final class SCSwap extends JavaPlugin {
    //private Logger log;
    public static Plugin getPlugin;
    public static Logger log;
    @Override
    public void onEnable() {
        getPlugin = Bukkit.getServer().getPluginManager().getPlugin("SCSwap");
        log = getPlugin.getLogger();
        loadConfiguration();
        registerListener(new TeleportListener());
    }

    @Override
    public void onDisable() {

    }

    private void registerListener(Listener listener) {
        getServer().getPluginManager().registerEvents(listener, this);
    }

    private void loadConfiguration() {
        getPlugin.getConfig().addDefault("Portal.To", "TO_SMP");
        getPlugin.getConfig().addDefault("Portal.From", "FROM_SMP");
        getPlugin.saveConfig();
    }
}
