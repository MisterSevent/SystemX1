package com.system.commands.X1SubCommands;

import com.system.entities.SubCommand;
import com.system.enums.GameStatus;
import com.system.enums.PlayerStatus;
import com.system.managers.Arena;
import com.system.managers.DatabasePlayer;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import static com.system.SystemX1.*;
import static com.system.SystemX1.getMessages;

public class DesafiarCommand implements SubCommand {

    private Arena arena;
    private static DatabasePlayer player = null;
    private static DatabasePlayer playerAlvo = null;

    @Override
    public void onCommand(Player player, Command command, String label, String[] args) {

        OfflinePlayer alvo;
        if (args.length < 3) {
            player.sendMessage(getMessages().getConfig().getString("args-insufficient-desafiar").replace('&', '§'));
            return;
        }
        alvo = Bukkit.getOfflinePlayer(args[1]);
        if (player.getName().equalsIgnoreCase(args[1])) {
            player.sendMessage(getMessages().getConfig().getString("challenged-you").replace('&', '§'));
            return;
        }
        if (alvo.getName().equalsIgnoreCase(args[1])) {
            if (alvo.isOnline()) {
                if (getInstance().getGame().getDinheiro(alvo.getName()) >= getInstance().getGame().getAposta()) {
                    if (getInstance().getGame().getDinheiro(player.getName()) >= getInstance().getGame().getAposta()) {
                        if (!getLocations().getConfig().contains("Arenas." + args[2])) {
                            player.sendMessage(getMessages().getConfig().getString("arena-invalid").replace('&', '§'));
                            return;
                        }
                        getInstance().getGame().registrarPlayerGame(player.getUniqueId());
                        this.player = getInstance().getGame().getPlayerGame(player.getUniqueId());
                        getInstance().getGame().registrarPlayerGame(alvo.getUniqueId());
                        playerAlvo = getInstance().getGame().getPlayerGame(alvo.getUniqueId());
                        getInstance().getGame().registrarArena(args[2]);
                        this.arena = getInstance().getGame().getArena(args[2]);
                        if (playerAlvo.getStatus() != PlayerStatus.ONLINE) {
                            player.sendMessage(getMessages().getConfig().getString("playerin-combat").replace('&', '§'));
                            return;
                        }
                        if (this.arena.getStatus() != GameStatus.OPEN) {
                            player.sendMessage(getMessages().getConfig().getString("arenain-combat").replace('&', '§'));
                            return;
                        }
                        if (this.player.getStatus() != PlayerStatus.ONLINE) {
                            player.sendMessage(getMessages().getConfig().getString("playerin-combat").replace('&', '§'));
                            return;
                        }
                        getInstance().getGame().registrarJogadores(player.getUniqueId(), args[2], alvo.getUniqueId());
                        getInstance().getGame().registrarArena(args[2]);
                        this.player.setArena(args[2]);
                        playerAlvo.setArena(args[2]);
                        this.player.setStatus(PlayerStatus.PENDING);
                        playerAlvo.setStatus(PlayerStatus.PENDING);
                        this.arena.setStatus(GameStatus.PENDING);
                        Bukkit.broadcastMessage(getMessages().getConfig().getString("challenged-player").replace('&', '§').replace("@desafiador", player.getName()).replace("@desafiado", alvo.getName()));
                        Bukkit.getPlayer(args[1]).sendMessage(getMessages().getConfig().getString("challenged-message").replace('&', '§'));
                        this.arena.registrarJogadores(player.getUniqueId(), alvo.getUniqueId());
                        this.arena.tempoArregar(player, alvo);
                        return;
                    }
                    player.sendMessage(getMessages().getConfig().getString("challenger-no-money").replace('&', '§'));
                    return;
                }
                player.sendMessage(getMessages().getConfig().getString("challenged-no-money").replace('&', '§'));
                return;
            }
            player.sendMessage(getMessages().getConfig().getString("player-invalid").replace('&', '§'));
            return;
        }
    }
}
