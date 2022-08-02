package io.github.arcoda.SCSwap;

import io.github.arcoda.SCSwap.Commands.CMPCommand;
import io.github.arcoda.SCSwap.Commands.SMPCommand;
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
    public Logger log;
    public FileConfiguration Config;
    public LuckPerms getLuckPerms;
    private static SCSwap instance;
    @Override
    public void onEnable() {
        instance = this;
        log = this.getLogger();
        Config = this.getConfig();
        getLuckPerms = LuckPermsProvider.get();
        loadConfiguration();
        registerListener(new TeleportListener());
        this.getCommand("smp").setExecutor(new SMPCommand());
        this.getCommand("cmp").setExecutor(new CMPCommand());
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(this);
    }

    public void devLog(String text) {
        if (Config.getBoolean("Debug")) {
            log.info(text);
        }
    }
    public static SCSwap getInstance() {
        return instance;
    }
    private void registerListener(Listener listener) {
        getServer().getPluginManager().registerEvents(listener, this);
    }

    private void loadConfiguration() {
        Config.addDefault("Portal.To", "TO_SMP");
        Config.addDefault("Portal.From", "FROM_SMP");
        Config.addDefault("Debug", false);
        Config.addDefault("World.Survival", "Survival1");
        Config.addDefault("World.Creative", "Main1");
        Config.options().copyDefaults(true);
        this.saveConfig();
    }
}
