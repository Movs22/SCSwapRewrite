package io.github.arcoda.SCSwap.Commands;

import com.onarandombox.MultiverseCore.MultiverseCore;
import com.onarandombox.MultiverseCore.api.Teleporter;
import com.onarandombox.MultiverseCore.event.MVTeleportEvent;
import com.onarandombox.MultiverseCore.utils.SimpleSafeTTeleporter;
import com.onarandombox.MultiversePortals.MultiversePortals;
import io.github.arcoda.SCSwap.SCSwap;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import static io.github.arcoda.SCSwap.Listener.TeleportListener.teleportLogic;

public class TeleportLibrary {
    private static MultiverseCore MV = JavaPlugin.getPlugin(MultiverseCore.class);
    private static MultiversePortals MVPortals = JavaPlugin.getPlugin(MultiversePortals.class);
    public static boolean teleportTo(CommandSender sender,@NotNull String mode) {
        if(!(sender instanceof Player)) {
            sender.sendMessage("Must be a player to use this command!");
            return true;
        }
        Player player = ((Player) sender).getPlayer();
        SimpleSafeTTeleporter teleport = new SimpleSafeTTeleporter(MV);
        teleport.safelyTeleport(player, player, MV.getDestFactory().getPlayerAwareDestination(player, SCSwap.Config.getString("World."+mode)));
        teleportLogic(mode.equals("Survival"), player);
        //Not working yet
        if (mode.equals("Survival")) {
            player.setBedSpawnLocation(MVPortals.getPortalManager().getPortal(SCSwap.Config.getString("Portal.To")).getDestination().getLocation(player).add(2, 0 ,0), true);
            return true;
        } else if (mode.equals("Creative")) {
            player.setBedSpawnLocation(MVPortals.getPortalManager().getPortal(SCSwap.Config.getString("Portal.From")).getDestination().getLocation(player).add(0, 0 ,2), true);
        }
        return false;
    }
}
