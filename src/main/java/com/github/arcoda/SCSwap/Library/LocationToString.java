package com.github.arcoda.SCSwap.Library;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class LocationToString {
    static String sep = "•";
    public static String toString(Location loc) {
        return loc.getWorld().getName()+sep+loc.getBlockX()+sep+loc.getBlockY()+sep+loc.getBlockZ();
    }
    public static Location toLocation(String str) {
        String[] split = str.split(sep);
        return new Location(Bukkit.getWorld(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2]), Integer.parseInt(split[3]));
    }
}
