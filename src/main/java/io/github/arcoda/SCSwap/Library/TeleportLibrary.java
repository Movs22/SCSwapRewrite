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
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

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
        Player player = ((Player) sender);
        SafeTTeleporter teleport = MV.getSafeTTeleporter();
        if(mode.equals("Survival")) {
            if (player.getWorld().equals(Bukkit.getWorld(plugin.getConfig().getString("World.Survival")))) {
                plugin.devLog(player.getName()+" is already in SMP, no need to teleport");
                player.sendMessage(plugin.prefix+"§4You are already in the SMP ._.");
                return true;
            }
            portal = MVPortals.getPortalManager().getPortal(plugin.getConfig().getString("Portal.To"));
        } else if (mode.equals("Creative")) {
            if (player.getWorld().equals(Bukkit.getWorld(plugin.getConfig().getString("World.Creative")))) {
                plugin.devLog(player.getName()+" is already in CMP, no need to teleport");
                player.sendMessage(plugin.prefix+"§4You are already in the CMP ._.");
                return true;
            }
            portal = MVPortals.getPortalManager().getPortal(plugin.getConfig().getString("Portal.From"));
        }
        Location location = portal.getDestination().getLocation(player).add(0, 0 ,2);
        //Teleports the player and logs the result
        TeleportResult result = teleport.safelyTeleport(player, player, new Location(Bukkit.getWorld(plugin.getConfig().getString("World."+mode)), location.getX(), location.getY(), location.getZ()), false);
        plugin.devLog(result.toString());
        teleportLogic(mode.equals("Survival"), player);
        player.setBedSpawnLocation(portal.getDestination().getLocation(player).add(0, 0 ,2), true);
        return true;
    }
    public void teleportLogic(boolean toSMP, Player player) {
        if (toSMP) {

            savePlayerData(player, "Creative", "Survival");
            player.setWalkSpeed(0.2F);
            player.setFlySpeed(0.2F);
            for (PotionEffect effect : player.getActivePotionEffects()) {
                player.removePotionEffect(effect.getType());
            }
            plugin.nametagAPI.setSuffix(player, "[SMP]");
            //Check for op, save and remove if necessary
            if (player.isOp()) {
                setOpPermission(true, player);
                player.setOp(false);
            } else {
                setOpPermission(false, player);
            }
        } else {
            savePlayerData(player, "Survival", "Creative");

            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                if (player.getWorld().equals(Bukkit.getWorld(plugin.getConfig().getString("World.Creative")))) {
                    player.setGameMode(GameMode.CREATIVE);
                    //Give back op if saved earlier
                    if (player.hasPermission("scswap.isop")) {
                        player.setOp(true);
                    }
                    plugin.nametagAPI.setSuffix(player, "");
                }
            }, 3 * 20);
        }
    }

    private void savePlayerData(Player player, String FromMode, String ToMode) {
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
        //Save the player armor
        inventory.set(player.getUniqueId()+"."+FromMode+".Armor",ArmorToString.invToString(player.getInventory()));

        //Save the player walkspeed and flyspeed if from creative
        if (FromMode.equals("Creative")) {
            inventory.set(player.getUniqueId()+"."+FromMode+".WalkSpeed",(double)player.getWalkSpeed());
            inventory.set(player.getUniqueId()+"."+FromMode+".FlySpeed",(double)player.getFlySpeed());
        }

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

        //Load walkspeed and flyspeed if to creative
        if(ToMode.equals("Creative")) {
            Float walkFloat = (float)inventory.getDouble(player.getUniqueId()+"."+ToMode+".WalkSpeed");
            if (walkFloat == null){
                plugin.devLog("No "+ToMode+" walkspeed found in config for "+player.getName());
            } else {
                player.setWalkSpeed(walkFloat);
            }
            Float flyFloat = (float)inventory.getDouble(player.getUniqueId()+"."+ToMode+".FlySpeed");
            if (flyFloat == null){
                plugin.devLog("No "+ToMode+" flyspeed found in config for "+player.getName());
            } else {
                player.setFlySpeed(flyFloat);
            }
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
