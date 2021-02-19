package com.system.integration.yClans;

import org.bukkit.plugin.java.JavaPlugin;
import yclans.Main;
import yclans.api.yClansAPI;

public class yClansEngine {


    static yClansAPI clans1 = yClansAPI.yclansapi;
    private static Main mainyClans;
    private static yClansAPI yClansAPI;

    public static yClansAPI getyClans() {
        return yClansAPI;

    }


    public static boolean setup() {
        Main clans = JavaPlugin.getPlugin(clans1.class);
        if (clans == null) {
            return false;
        }
        clans = (Main) mainyClans;
        return true;
    }
}
