package com.github.arcoda.SCSwap;

import com.github.arcoda.SCSwap.Commands.SMPCommand;
import com.github.arcoda.SCSwap.Library.Graveyard;
import com.github.arcoda.SCSwap.Library.TeleportLibrary;
import com.github.arcoda.SCSwap.Commands.CMPCommand;
import com.github.arcoda.SCSwap.Commands.SCSWapCommand;
import com.github.arcoda.SCSwap.Commands.Tab.SCSwapTabComplete;
import com.github.arcoda.SCSwap.Listener.InventoryListener;
import com.github.arcoda.SCSwap.Listener.JoinListener;
import com.github.arcoda.SCSwap.Listener.LeaveListener;
import com.github.arcoda.SCSwap.Listener.TeleportListener;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public final class SCSwap extends JavaPlugin {
    //private Logger log;
    public Logger log;
    public FileConfiguration Config;
    public LuckPerms getLuckPerms;
    public TeleportLibrary getTeleportLib;
    public File smpInvFile;
    public File cmpInvFile;
    private static SCSwap instance;
    public String prefix = "[SCSwap] ";
    
    public HashMap<Player, Graveyard> graveyards;
    
    public List<Player> staff;
    
    public Map<Location, Graveyard> graveyardLocs;
    
    public Boolean enableStaff(Player p) {
    	staff.add(p);
    	return true;
    }
    
    public Boolean disableStaff(Player p) {
    	staff.remove(p);
    	return true;
    }
    
    public Boolean isStaff(Player p) {
    	return staff.contains(p);
    }
    
    public ItemStack remoteGraveyard;
    
    public NamespacedKey useGraveyard = NamespacedKey.fromString("minecraft:adventure/graveyard");
    public NamespacedKey stealGraveyard = NamespacedKey.fromString("minecraft:adventure/steal_graveyard");
    public NamespacedKey useRGraveyard = NamespacedKey.fromString("minecraft:adventure/remote_graveyard");
    
    public BukkitScheduler updateTask = Bukkit.getScheduler();
    
    NamespacedKey key = new NamespacedKey(this, "remote_graveyard");
    
    @Override
    public void onEnable() {
    	remoteGraveyard = new ItemStack(Material.NETHER_STAR);
    	ItemMeta rgMeta = remoteGraveyard.getItemMeta();
    	rgMeta.setDisplayName("§dRemote Graveyard");
    	Damageable dm = (Damageable) rgMeta;
    	dm.setMaxDamage(11);
    	dm.setMaxStackSize(1);
    	dm.setDamage(1);
    	dm.addEnchant(Enchantment.VANISHING_CURSE, 1, true);
    	rgMeta.setLore(Arrays.asList("Use this item to access your loot remotely"));
    	remoteGraveyard.setItemMeta(rgMeta);
    	
    	ShapedRecipe recipe = new ShapedRecipe(key, remoteGraveyard);
    	
    	recipe.shape("NWN","DSD","NWN");
    	recipe.setIngredient('N', Material.CRYING_OBSIDIAN);
    	recipe.setIngredient('W', Material.NETHER_STAR);
    	recipe.setIngredient('D', Material.NETHERITE_SCRAP);
    	recipe.setIngredient('S', Material.HEAVY_CORE);
    	
    	Bukkit.addRecipe(recipe);
    	
        instance = this;
        graveyards = new HashMap<Player, Graveyard>();
        graveyardLocs = new HashMap<Location, Graveyard>();
        staff = new ArrayList<Player>();
        updateTask.runTaskTimer(this, new Runnable() {
			@Override
			public void run() {
	        	staff.forEach(p -> {
	        		p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("§6You are on SMP staff mode."));
	        	});
			}
        }, 0L, 20L);
        log = this.getLogger();
        Config = this.getConfig();
        getLuckPerms = LuckPermsProvider.get();
        getTeleportLib = new TeleportLibrary();
        loadConfiguration();
        smpInvFile = new File("./plugins/SCSwap/survival.yml");
        cmpInvFile = new File("./plugins/SCSwap/creative.yml");
        try {
        	smpInvFile.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
        	cmpInvFile.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        getTeleportLib.setInventory(YamlConfiguration.loadConfiguration(smpInvFile), "Survival");
        getTeleportLib.setInventory(YamlConfiguration.loadConfiguration(cmpInvFile), "Creative");
        /*registerListener(new TeleportListener());*/
        registerListener(new JoinListener());
        registerListener(new LeaveListener());
        registerListener(new TeleportListener());
        registerListener(new InventoryListener());
        this.getCommand("smp").setExecutor(new SMPCommand());
        this.getCommand("cmp").setExecutor(new CMPCommand());
        this.getCommand("scswap").setExecutor(new SCSWapCommand());
        this.getCommand("scswap").setTabCompleter(new SCSwapTabComplete());
    }

    @Override
    public void onDisable() {
    	Bukkit.removeRecipe(key);
    	graveyards = null;
    	staff = null;
    	graveyardLocs = null;
        HandlerList.unregisterAll(this);
    }

    public void devLog(String text) {
        if (Config.getBoolean("Debug")) {
            log.info(text);
        }
    }
    public static SCSwap getInstance() {
        return instance;
    }
    private void registerListener(Listener listener) {
        getServer().getPluginManager().registerEvents(listener, this);
    }
    
	private void loadConfiguration() {
        Config.addDefault("Portal.To", "TO_SMP");
        Config.addDefault("Portal.From", "FROM_SMP");
        Config.addDefault("Debug", false);
        List<String> defaultSmp = new ArrayList<>();
        defaultSmp.add("Survival1");
        defaultSmp.add("Survival1_nether");
        defaultSmp.add("Survival1_the_end");
        Config.addDefault("World.Survival", defaultSmp);
        Config.addDefault("World.Survival.Gamemode", "Survival");
        Config.addDefault("World.Survival.Permission", "scswap.smp");   //this permission will be given to players who leave this world (and go to the CMP)
        Config.addDefault("World.Creative", "Main1");
        Config.addDefault("World.Creative.Gamemode", "Creative");
        Config.addDefault("World.Creative.Permission", "scswap.cmp");   //this permission will be given to players who leave this world (and go to the SMP)
        Config.options().copyDefaults(true);
        this.saveConfig();
    }
}
