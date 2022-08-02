package io.github.arcoda.SCSwap.Listener;

import com.onarandombox.MultiversePortals.event.MVPortalEvent;
import io.github.arcoda.SCSwap.SCSwap;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.data.DataMutateResult;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public final class TeleportListener implements Listener {
    private static SCSwap plugin = SCSwap.getInstance();
    private static LuckPerms perms = plugin.getLuckPerms;
    @EventHandler
    public void OnTeleport(MVPortalEvent event) {
        Player player = event.getTeleportee();
        plugin.devLog(player.getName()+" is being teleported to "+event.getDestination().getName()+" from "+event.getSendingPortal().getName());
        if (event.getSendingPortal().getName().equals(plugin.getConfig().getString("Portal.To"))) {
            teleportLogic(true, player);
            player.setBedSpawnLocation(event.getDestination().getLocation(player).add(2, 0 ,0), true);
        }
        else if (event.getSendingPortal().getName().equals(plugin.getConfig().getString("Portal.From"))) {
            teleportLogic(false, player);
            player.setBedSpawnLocation(event.getDestination().getLocation(player).add(0, 0 ,2), true);
        }
    }
    public static void teleportLogic(boolean toSMP, Player player) {
        if (toSMP) {
            saveInventory(player, "Creative", "Survival");
            //Check for op, save and remove if necessary
            if (player.isOp()) {
                setOpPermission(true, player);
                player.setOp(false);
            } else {
                setOpPermission(false, player);
            }
        } else {
            saveInventory(player, "Survival", "Creative");
            //Give back op if saved earlier
            if (player.hasPermission("scswap.isop")) {
                player.setOp(true);
            }
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> player.setGameMode(GameMode.CREATIVE), 5 * 20);
        }
    }

    private static void saveInventory(Player player, String FromMode, String ToMode) {
        //Saves the player inventory then clear it
        plugin.getConfig().set("Inventory."+FromMode+"."+player.getName(), player.getInventory().getContents());
        plugin.saveConfig();
        player.getInventory().clear();
        //Load inventory from config if it exists
        try {
            ArrayList<ItemStack> content = (ArrayList<ItemStack>) plugin.getConfig().getList("Inventory."+ToMode+"."+player.getName());
            plugin.devLog(content.get(1).toString());
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
            plugin.devLog("No "+ToMode+" inventory found in config for "+player.getName());
            plugin.devLog(e.getMessage());
        }
    }
    private static void setOpPermission(boolean isOp, Player player) {
        User user = perms.getPlayerAdapter(Player.class).getUser(player);
        user.data().add(Node.builder("scswap.isop").value(isOp).build());
        perms.getUserManager().saveUser(user);
    }
}
