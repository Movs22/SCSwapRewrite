package com.github.arcoda.SCSwap.Listener;

import com.github.arcoda.SCSwap.Library.LocationToString;
import com.github.arcoda.SCSwap.SCSwap;
import com.onarandombox.MultiversePortals.event.MVPortalEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public final class TeleportListener implements Listener {
    private static SCSwap plugin = SCSwap.getInstance();
    @EventHandler
    public void OnTeleport(MVPortalEvent event) {
        Player player = event.getTeleportee();

        plugin.devLog(player.getName()+" is being teleported to "+event.getDestination().getName()+" from "+event.getSendingPortal().getName());
        if (event.getSendingPortal().getName().equals(plugin.getConfig().getString("Portal.To"))) {
            plugin.getTeleportLib.getInventory().set(player.getUniqueId()+".Creative.Location", LocationToString.toString(player.getLocation().add(0, 0 , 2)));
            plugin.getTeleportLib.teleportLogic(true, player);
            player.setBedSpawnLocation(event.getDestination().getLocation(player).add(2, 0 ,0), true);
        }
        else if (event.getSendingPortal().getName().equals(plugin.getConfig().getString("Portal.From"))) {
            plugin.getTeleportLib.getInventory().set(player.getUniqueId()+".Survival.Location",LocationToString.toString(player.getLocation().add(0, 0 , 2)));
            plugin.getTeleportLib.teleportLogic(false, player);
            player.setBedSpawnLocation(event.getDestination().getLocation(player).add(0, 0 ,2), true);
        }
    }
}
