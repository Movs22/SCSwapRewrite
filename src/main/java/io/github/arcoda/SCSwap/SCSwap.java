package io.github.arcoda.SCSwap;

import org.bukkit.plugin.java.JavaPlugin;

public class SCSwap extends JavaPlugin {
    @Override
    public void onEnable() {
        getLogger().info("Starting up SCSwap!");
    }

    @Override
    public void onDisable() {
        getLogger().info("Shutting down SCSwap!");
    }
}
