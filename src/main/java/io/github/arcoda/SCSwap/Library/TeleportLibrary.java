package io.github.arcoda.SCSwap.Library;

import com.onarandombox.MultiverseCore.MultiverseCore;
import com.onarandombox.MultiverseCore.api.SafeTTeleporter;
import com.onarandombox.MultiverseCore.enums.TeleportResult;
import com.onarandombox.MultiversePortals.MVPortal;
import com.onarandombox.MultiversePortals.MultiversePortals;
import io.github.arcoda.SCSwap.SCSwap;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class TeleportLibrary {
    private SCSwap plugin = SCSwap.getInstance();
    private MultiverseCore MV = JavaPlugin.getPlugin(MultiverseCore.class);
    private MultiversePortals MVPortals = JavaPlugin.getPlugin(MultiversePortals.class);
    private MVPortal portal;
    private LuckPerms perms = plugin.getLuckPerms;
    private YamlConfiguration inventory;
    public boolean teleportTo(CommandSender sender,@NotNull String mode) {
        if(!(sender instanceof Player)) {
            sender.sendMessage("Must be a player to use this command!");
            return true;
        }
        Player player = ((Player) sender).getPlayer();
        SafeTTeleporter teleport = MV.getSafeTTeleporter();
        if(mode.equals("Survival")) {
            plugin.devLog(plugin.getConfig().getString("Portal.To"));
            portal = MVPortals.getPortalManager().getPortal(plugin.getConfig().getString("Portal.To"));
        } else if (mode.equals("Creative")) {
            portal = MVPortals.getPortalManager().getPortal(plugin.getConfig().getString("Portal.From"));
        }
        Location location = portal.getDestination().getLocation(player).add(0, 0 ,2);
        //Teleports the player and logs the result
        TeleportResult result = teleport.safelyTeleport(player, player, new Location(Bukkit.getWorld(plugin.getConfig().getString("World."+mode)), location.getX(), location.getY(), location.getZ()), false);
        plugin.devLog(result.toString());
        teleportLogic(mode.equals("Survival"), player);
        //Not working yet
        player.setBedSpawnLocation(portal.getDestination().getLocation(player).add(0, 0 ,2), true);
        return true;
    }
    public void teleportLogic(boolean toSMP, Player player) {
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
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> player.setGameMode(GameMode.CREATIVE), 3 * 20);
        }
    }

    private void saveInventory(Player player, String FromMode, String ToMode) {
        //Check for empty inventory to prevent a bug
        Boolean emptyInventory = true;
        for(ItemStack item : player.getInventory().getContents())
        {
            if(item != null) {
                emptyInventory = false;
            }
        }
        if (emptyInventory) {
            inventory.set(player.getUniqueId()+"."+FromMode, "4†e†");
        } else {
            //Saves the player inventory
            inventory.set(player.getUniqueId()+"."+FromMode+".Inventory", InventoryToString.invToString(player.getInventory()));
        }
        inventory.set(player.getUniqueId()+"."+FromMode+".Armor",ArmorToString.invToString(player.getInventory()));
        //Saves inventory config and clear player inventory
        try {inventory.save(plugin.inventory);} catch (IOException e) {throw new RuntimeException(e);}
        player.getInventory().clear();
        //Load inventory from config if it exists
        String invString = inventory.getString(player.getUniqueId()+"."+ToMode+".Inventory");
        if (invString == null){
            plugin.devLog("No "+ToMode+" inventory found in config for "+player.getName());
        }
        else {
            player.getInventory().setContents(InventoryToString.stringToInv(invString).getContents());
        }
        //Load armor from config if it exists
        String armString = inventory.getString(player.getUniqueId()+"."+ToMode+".Armor");
        if (armString == null){
            plugin.devLog("No "+ToMode+" armor found in config for "+player.getName());
        }
        else {
            ArmorToString.stringToInv(armString, player.getInventory());
        }
    }
    private void setOpPermission(boolean isOp, Player player) {
        User user = perms.getPlayerAdapter(Player.class).getUser(player);
        user.data().add(Node.builder("scswap.isop").value(isOp).build());
        perms.getUserManager().saveUser(user);
    }
    public void setInventory(YamlConfiguration file) {
        inventory = file;
    }
}
