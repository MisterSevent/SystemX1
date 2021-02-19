package com.system.integration.SimpleClans;

import com.system.integration.Integration;
import org.bukkit.Bukkit;

public class SimpleClansIntegration extends Integration {

    private static SimpleClansIntegration i = new SimpleClansIntegration();
    public static SimpleClansIntegration get() {
        return i;
    }

    @Override
    public String getName() {
        return "SimpleClans";
    }

    @Override
    public boolean isEnabled() {
        return Bukkit.getPluginManager().isPluginEnabled(this.getName());
    }

    @Override
    public void init() {
        SimpleClansEngine.setup();
        this.notifyEnabled();
    }
}
