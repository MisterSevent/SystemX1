package com.system.apis;


import com.system.SystemX1;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import static com.system.SystemX1.*;

public class GhostMaker {
    private SystemX1 plugin;
    private Team team;

    public GhostMaker(SystemX1 main) {
        this.plugin = main;
        this.createGetTeam();
    }

    private void createGetTeam() {
        Scoreboard board = Bukkit.getServer().getScoreboardManager().getMainScoreboard();
        this.team = board.getTeam("camarote");
        if (this.team == null) {
            this.team = board.registerNewTeam("camarote");
        }

        this.team.setCanSeeFriendlyInvisibles(true);
    }

    public void setAsGhost(Player p, String arena) {
        this.team.addPlayer(p);
        PotionEffect potion = new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 15);
        p.addPotionEffect(potion);
        Location loc = (Location) getLocations().getConfig().get("Arenas." + arena + "." + "Camarote." + ".Location");
        p.setGameMode(GameMode.ADVENTURE);
        p.setAllowFlight(true);
        p.teleport(loc);
    }

    public void setAsNormal(Player p, String arena) {
        p.removePotionEffect(PotionEffectType.INVISIBILITY);
        this.team.removePlayer(p);
        Location loc = (Location) getLocations().getConfig().get("Arenas." + arena + "." + "Saida." + ".Location");
        p.setGameMode(GameMode.SURVIVAL);
        p.setAllowFlight(false);
        p.teleport(loc);
    }
}
