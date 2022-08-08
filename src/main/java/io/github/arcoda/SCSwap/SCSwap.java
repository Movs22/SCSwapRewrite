package io.github.arcoda.SCSwap;

import com.nametagedit.plugin.NametagEdit;
import com.nametagedit.plugin.api.NametagAPI;
import io.github.arcoda.SCSwap.Commands.CMPCommand;
import io.github.arcoda.SCSwap.Commands.SCSWapCommand;
import io.github.arcoda.SCSwap.Commands.SMPCommand;
import io.github.arcoda.SCSwap.Commands.Tab.SCSwapTabComplete;
import io.github.arcoda.SCSwap.Library.TeleportLibrary;
import io.github.arcoda.SCSwap.Listener.JoinListener;
import io.github.arcoda.SCSwap.Listener.TeleportListener;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

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
    public File inventory;
    private static SCSwap instance;
    public String prefix = "[SCSwap] ";
    public NametagAPI nametagAPI;
    public List<World> smpWorlds;
    @Override
    public void onEnable() {
        instance = this;
        log = this.getLogger();
        Config = this.getConfig();
        getLuckPerms = LuckPermsProvider.get();
        getTeleportLib = new TeleportLibrary();
        nametagAPI = (NametagAPI) JavaPlugin.getPlugin(NametagEdit.class).getApi();
        loadConfiguration();
        inventory = new File("./plugins/SCSwap/inventory.yml");
        try {
            inventory.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        getTeleportLib.setInventory(YamlConfiguration.loadConfiguration(inventory));
        registerListener(new TeleportListener());
        registerListener(new JoinListener());
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

    private void loadConfiguration() {
        Config.addDefault("Portal.To", "TO_SMP");
        Config.addDefault("Portal.From", "FROM_SMP");
        Config.addDefault("Debug", false);
        List<String> defaultSmp = new ArrayList<>();
        defaultSmp.add("Survival1");
        defaultSmp.add("Survival1_nether");
        defaultSmp.add("Survival1_the_end");
        Config.addDefault("World.Survival", defaultSmp);
        Config.addDefault("World.Creative", "Main1");
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
