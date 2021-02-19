package com.system.events;

import com.system.SystemX1;
import com.system.database.Methods;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.lang.reflect.Method;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void jogadorEntrar(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        if (!Methods.hasAccount(p.getUniqueId())) {
            Methods.createAccount(p.getUniqueId(), p.getName(), 1, 1, 0, 0);
        }
    }
}
