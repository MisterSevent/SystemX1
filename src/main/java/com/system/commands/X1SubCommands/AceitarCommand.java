package com.system.commands.X1SubCommands;

import com.system.entities.SubCommand;
import com.system.enums.PlayerStatus;
import com.system.managers.Arena;
import com.system.managers.DatabasePlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import java.util.UUID;

import static com.system.SystemX1.*;

public class AceitarCommand implements SubCommand {

    private Arena arena;
    private static DatabasePlayer player = null;
    private static DatabasePlayer playerAlvo = null;

    @Override
    public void onCommand(Player player, Command command, String label, String[] args) {
        if (getInstance().getGame().getDesafiado().contains(player.getUniqueId())) {
            this.player = getInstance().getGame().getPlayerGame(player.getUniqueId());
            String arena = this.player.getArena();
            this.arena = getInstance().getGame().getArena(arena);
            Player desafiador = Bukkit.getPlayer((UUID) this.arena.getDesafiador().get(0));
            playerAlvo = getInstance().getGame().getPlayerGame(desafiador.getUniqueId());
            this.player.setStatus(PlayerStatus.IN_GAME);
            playerAlvo.setStatus(PlayerStatus.IN_GAME);
            this.arena.jogoAceito(player);
            Location loc = (Location) getLocations().getConfig().get("Arenas." + arena + "." + "Player1." + ".Location");
            Location loc2 = (Location) getLocations().getConfig().get("Arenas." + arena + "." + "Player2." + ".Location");
            player.teleport(loc2);
            desafiador.teleport(loc);
            return;
        }
        player.sendMessage(getMessages().getConfig().getString("not-solicitation").replace('&', '§'));
        return;
    }
}
