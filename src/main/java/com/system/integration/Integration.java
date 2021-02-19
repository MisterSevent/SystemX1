package com.system.integration;

import org.bukkit.Bukkit;
import com.system.integration.yClans.yClansEngine;

public abstract class Integration {

    public abstract String getName();

    public abstract void init();

    public boolean isEnabled() {
        return Bukkit.getPluginManager().isPluginEnabled(this.getName());
    }

    public void notifyEnabled() {
        Bukkit.getConsoleSender().sendMessage("§a[SystemX1] Foi encontrado §c" + this.getName() + " §arealizando hook.");
    }

    public void notifyDisabled() {
        System.out.println(yClansEngine.getyClans());
        Bukkit.getConsoleSender().sendMessage("§c[SystemX1] Não foi encontrado §c" + this.getName() + " §apara realizar hook.");
    }
}
