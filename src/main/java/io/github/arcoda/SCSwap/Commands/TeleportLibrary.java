package io.github.arcoda.SCSwap.Commands;

import com.onarandombox.MultiverseCore.MultiverseCore;
import com.onarandombox.MultiverseCore.api.SafeTTeleporter;
import com.onarandombox.MultiverseCore.api.Teleporter;
import com.onarandombox.MultiverseCore.enums.TeleportResult;
import com.onarandombox.MultiverseCore.event.MVTeleportEvent;
import com.onarandombox.MultiverseCore.utils.SimpleSafeTTeleporter;
import com.onarandombox.MultiversePortals.MVPortal;
import com.onarandombox.MultiversePortals.MultiversePortals;
import io.github.arcoda.SCSwap.SCSwap;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

import static io.github.arcoda.SCSwap.Listener.TeleportListener.teleportLogic;

public class TeleportLibrary {
    private static SCSwap plugin = SCSwap.getInstance();
    private static MultiverseCore MV = JavaPlugin.getPlugin(MultiverseCore.class);
    private static MultiversePortals MVPortals = JavaPlugin.getPlugin(MultiversePortals.class);
    private static MVPortal portal;
    public static boolean teleportTo(CommandSender sender,@NotNull String mode) {
        if(!(sender instanceof Player)) {
            sender.sendMessage("Must be a player to use this command!");
            return true;
        }
        Player player = ((Player) sender).getPlayer();
        SafeTTeleporter teleport = MV.getSafeTTeleporter();
        if(mode.equals("Survival")) {
            plugin.devLog(plugin.getConfig().getString("Portal.To"));
            MVPortal portal = MVPortals.getPortalManager().getPortal(plugin.getConfig().getString("Portal.To"));
        } else if (mode.equals("Creative")) {
            MVPortal portal = MVPortals.getPortalManager().getPortal(plugin.getConfig().getString("Portal.From"));
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
}
