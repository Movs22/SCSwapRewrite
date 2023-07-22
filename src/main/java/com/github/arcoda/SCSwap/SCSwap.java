package com.github.arcoda.SCSwap;

import com.github.arcoda.SCSwap.Commands.SMPCommand;
import com.github.arcoda.SCSwap.Library.TeleportLibrary;
import com.nametagedit.plugin.NametagEdit;
import com.nametagedit.plugin.api.NametagAPI;
import com.github.arcoda.SCSwap.Commands.CMPCommand;
import com.github.arcoda.SCSwap.Commands.SCSWapCommand;
import com.github.arcoda.SCSwap.Commands.Tab.SCSwapTabComplete;
import com.github.arcoda.SCSwap.Listener.JoinListener;
import com.github.arcoda.SCSwap.Listener.LeaveListener;
import com.github.arcoda.SCSwap.Listener.TeleportListener;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
    public NametagAPI nametagAPI;
    public List<World> smpWorlds;
    public List<Player> staff = new ArrayList<Player>();
    
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
    
    public BukkitScheduler updateTask = Bukkit.getScheduler();
    @Override
    public void onEnable() {
        instance = this;
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
        JavaPlugin.getPlugin(NametagEdit.class);
		nametagAPI = (NametagAPI) NametagEdit.getApi();
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
        this.getCommand("smp").setExecutor(new SMPCommand());
        this.getCommand("cmp").setExecutor(new CMPCommand());
        this.getCommand("scswap").setExecutor(new SCSWapCommand());
        this.getCommand("scswap").setTabCompleter(new SCSwapTabComplete());
    }

    @Override
    public void onDisable() {
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
    

    @SuppressWarnings("unchecked")
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
        List<String> smpList = (List<String>) Config.getList("World.Survival");
        if (smpList != null) {
            smpWorlds = new ArrayList<>();
            for (String world : smpList) {
                smpWorlds.add(getServer().getWorld(world));
            }
        } else {
            log.warning("Please configure the World.Survival list in the config.yml");
        }
    }
}
