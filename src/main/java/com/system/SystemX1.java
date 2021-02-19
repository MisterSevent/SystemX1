package com.system;

import com.system.apis.ConfigAPI;
import com.system.apis.GhostMaker;
import com.system.apis.Metrics;
import com.system.commands.X1Command;
import com.system.database.Methods;
import com.system.events.PlayerClickEvent;
import com.system.events.PlayerJoinListener;
import com.system.events.X1Events;
import com.system.integration.Integration;
import com.system.integration.Integrations;
import com.system.integration.SimpleClans.SimpleClansIntegration;
import com.system.integration.yClans.yClansIntegration;
import com.system.inventorys.DadosInventory;
import com.system.managers.Game;
import com.system.managers.StorageManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class SystemX1 extends JavaPlugin {

    private static SystemX1 instance;
    private static ConfigAPI configuration;
    private static ConfigAPI locations;
    private static ConfigAPI messages;
    public Game game;
    public Methods methods;
    public DadosInventory dadosInventory;
    public GhostMaker ghostMaker;
    public X1Command x1Command;
    private static StorageManager storageManager;
    private static final int PLUGIN_ID = 10201;

    @Override
    public void onEnable() {
        instance = this;
        bStats();
        loadConfigurations();
        loadEvents();
        loadManagers();
        loadIntegrations();
        loadCommands();

    }

    @Override
    public void onDisable() {

    }

    boolean loadConfigurations() {
        this.configuration = new ConfigAPI("config.yml");
        this.locations = new ConfigAPI("locations.yml");
        this.messages = new ConfigAPI("messages.yml");
        boolean continueFlux = true;
        ConfigAPI[] var2 = new ConfigAPI[]{this.configuration, this.locations, this.messages};
        int var3 = var2.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            ConfigAPI configAPI = var2[var4];
            if (!configAPI.exists()) {
                continueFlux = false;
                configAPI.saveDefaultConfig();
            }
        }
        if (!continueFlux) {
            this.getLogger().info("Plugin desligado para configuração, reiniciei novamente.");
            return true;
        } else {
            return false;
        }
    }

    void loadCommands() {
        getCommand("x1").setExecutor(new X1Command());
    }

    void loadEvents() {
        Bukkit.getPluginManager().registerEvents(new PlayerClickEvent(), this);
        Bukkit.getPluginManager().registerEvents(new X1Events(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(), this);
    }

    public void loadIntegrations() {
        Integrations.add(SimpleClansIntegration.get(), yClansIntegration.get());
    }

    void loadManagers() {
        game = new Game(this);
        ghostMaker = new GhostMaker(this);
        x1Command = new X1Command();
        storageManager = new StorageManager(this);
        methods = new Methods(this);
        dadosInventory = new DadosInventory(this);
    }

    private void bStats() {
        new Metrics(this, PLUGIN_ID);
        Bukkit.getConsoleSender().sendMessage("§a[SystemX1] bStats enabled successfully.");
    }


    public static ConfigAPI getConfiguration() {
        return configuration;
    }

    public static ConfigAPI getLocations() {
        return locations;
    }

    public static ConfigAPI getMessages() {
        return messages;
    }

    public static SystemX1 getInstance() {
        return instance;
    }

    public Game getGame() {
        return game;
    }

    public GhostMaker getGhostMaker() {
        return ghostMaker;
    }

    public static StorageManager getStorageManager() {
        return storageManager;
    }

}
