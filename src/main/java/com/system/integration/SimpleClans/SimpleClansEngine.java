package com.system.integration.SimpleClans;

import com.system.integration.Integration;
import net.sacredlabyrinth.phaed.simpleclans.Clan;
import net.sacredlabyrinth.phaed.simpleclans.SimpleClans;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.Serializable;
import java.util.UUID;

public class SimpleClansEngine {

    private static SimpleClans simpleClansPlugin;

    public static SimpleClans getSimpleClans() {
        return simpleClansPlugin;
    }

    public static boolean setup() {
        SimpleClans simpleClans = JavaPlugin.getPlugin(SimpleClans.class);
        if (simpleClans == null) {
            return false;
        }// tenta ai
        simpleClansPlugin = (SimpleClans) simpleClans;
        return true;
    }

    public static boolean setFireFriendly(UUID player1, UUID player2, boolean state) {
        Clan clanPlayer1 = simpleClansPlugin.getClanManager().getClanPlayer(player1).getClan();
        boolean initialState = clanPlayer1.isFriendlyFire();
        Clan clanPlayer2 = simpleClansPlugin.getClanManager().getClanPlayer(player2).getClan();

       if (clanPlayer1 == clanPlayer2) {
           System.out.println("");
           clanPlayer1.setFriendlyFire(state);
       }
       return initialState;
    }
}
