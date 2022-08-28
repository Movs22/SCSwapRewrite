package com.github.arcoda.SCSwap.Listener;

import com.github.arcoda.SCSwap.SCSwap;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {
    private static SCSwap plugin = SCSwap.getInstance();
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        plugin.devLog(event.getPlayer().getName()+" joining on "+event.getPlayer().getWorld().getName());
        if (plugin.smpWorlds.contains(event.getPlayer().getWorld())) {
            plugin.devLog(event.getPlayer().getName()+" joined inside SMP, giving suffix");
            plugin.nametagAPI.setSuffix(event.getPlayer(), "[SMP]");
        }
    }
}
