package io.github.arcoda.SCSwap.Listener;

import com.onarandombox.MultiversePortals.event.MVPortalEvent;
import io.github.arcoda.SCSwap.SCSwap;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public final class TeleportListener implements Listener {
    private Player player;
    @EventHandler
    public void OnTeleport(MVPortalEvent event) {
        player = event.getTeleportee();
        //SCSwap.log.info(player.getName()+" is being teleported to "+event.getDestination().getName()+" from "+event.getSendingPortal().getName());
        if (event.getSendingPortal().getName().equals(SCSwap.getPlugin.getConfig().getString("Portal.To"))) {
            //saveInventory("Creative", "Survival");
            //Check for op, save and remove if necessary
            if (player.isOp()) {
                player.addAttachment(SCSwap.getPlugin, "scswap.isop", true);
                player.setOp(false);
            } else {
                player.addAttachment(SCSwap.getPlugin, "scswap.isop", false);
            }
        }
        else if (event.getSendingPortal().getName().equals(SCSwap.getPlugin.getConfig().getString("Portal.From"))) {
            //saveInventory("Survival", "Creative");
            //Give back op if saved earlier
            if (player.hasPermission("scswap.isop")) {
                player.setOp(true);
            }
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(SCSwap.getPlugin, () -> player.setGameMode(GameMode.CREATIVE), 5 * 20);

        }
    }

    /* private void saveInventory(String FromMode, String ToMode) {
        //Saves the player inventory then clear it
        SCSwap.getPlugin.getConfig().set("Inventory."+FromMode+"."+player.getName(), player.getInventory().getContents());
        SCSwap.getPlugin.saveConfig();
        player.getInventory().clear();
        //Load inventory from config if it exists
        try {
            ArrayList<ItemStack> content = (ArrayList<ItemStack>) SCSwap.getPlugin.getConfig().getList("Inventory."+ToMode+"."+player.getName());
            SCSwap.log.info(content.get(1).toString());
            ItemStack[] items = new ItemStack[content.size()];
            for (int i = 0; i < content.size(); i++) {
                ItemStack item = content.get(i);
                if (item != null) {
                    items[i] = item;
                } else {
                    items[i] = null;
                }
            }
            player.getInventory().setContents(items);
        } catch(Exception e) {
            SCSwap.log.info("No "+ToMode+" inventory found in config for "+player.getName());
            SCSwap.log.warning(e.getMessage());
        }
    } */
}
