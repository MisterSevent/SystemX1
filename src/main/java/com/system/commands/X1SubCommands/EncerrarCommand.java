package com.system.commands.X1SubCommands;

import com.system.entities.SubCommand;
import com.system.enums.GameStatus;
import com.system.managers.Arena;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import static com.system.SystemX1.*;
import static com.system.SystemX1.getMessages;

public class EncerrarCommand implements SubCommand {

    private Arena arena;

    @Override
    public void onCommand(Player player, Command command, String label, String[] args) {
        if (player.hasPermission(getConfiguration().getConfig().getString("Permissions.admin"))) {
            if (args.length == 2) {
                if (!getLocations().getConfig().contains("Arenas."+args[1])) {
                    player.sendMessage(getMessages().getConfig().getString("arena-invalid").replace('&', '§'));
                    return;
                }
                arena = getInstance().getGame().getArena(args[1]);
                if (arena.getStatus() == GameStatus.IN_GAME) {
                    arena.tempoAcabou();
                    return;
                }else {
                    player.sendMessage(getMessages().getConfig().getString("not-x1-arena").replace('&', '§'));
                    return;
                }

            } else {
                player.sendMessage(getMessages().getString("args-insufficient-encerrar").replace('&', '§'));
                return;
            }
        } else {
            player.sendMessage(getMessages().getString("no-permission").replace('&', '§'));
            return;
        }
    }
}
