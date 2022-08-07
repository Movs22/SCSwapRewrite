package io.github.arcoda.SCSwap.Listener;

import io.github.arcoda.SCSwap.SCSwap;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {
    private static SCSwap plugin = SCSwap.getInstance();
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        plugin.devLog(event.getPlayer().getName()+" joining on "+event.getPlayer().getWorld().getName());
        if (event.getPlayer().getWorld().equals(Bukkit.getWorld(plugin.getConfig().getString("World.Survival")))) {
            plugin.devLog(event.getPlayer().getName()+" joined inside SMP, giving suffix");
            plugin.nametagAPI.setSuffix(event.getPlayer(), "[SMP]");
        }
    }
}
