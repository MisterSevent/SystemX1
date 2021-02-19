package com.system.integration.yClans;

import com.system.integration.Integration;
import com.system.integration.SimpleClans.SimpleClansEngine;
import com.system.integration.SimpleClans.SimpleClansIntegration;
import org.bukkit.Bukkit;

public class yClansIntegration extends Integration {

    private static yClansIntegration i = new yClansIntegration();
    public static yClansIntegration get() {
        return i;
    }

    @Override
    public String getName() {
        return "yClans";
    }

    @Override
    public boolean isEnabled() {
        return Bukkit.getPluginManager().isPluginEnabled(this.getName());
    }

    @Override
    public void init() {
        yClansEngine.setup();
        this.notifyEnabled();
    }
}
