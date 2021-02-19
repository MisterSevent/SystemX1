package com.system.commands.X1SubCommands;

import com.system.entities.SubCommand;
import com.system.managers.Arena;
import com.system.managers.DatabasePlayer;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import static com.system.SystemX1.getInstance;


public class DadosCommand implements SubCommand {

    private Arena arena;
    private static DatabasePlayer player = null;

    @Override
    public void onCommand(Player player, Command command, String label, String[] args) {
            OfflinePlayer alvo = Bukkit.getOfflinePlayer(args[1]);
            if (alvo.getName().equalsIgnoreCase(args[1]) && alvo.hasPlayedBefore()) {
                getInstance().game.registrarPlayerGame(alvo.getUniqueId());
                this.player = getInstance().game.getPlayerGame(alvo.getUniqueId());
                if (!this.player.getCarregado()) {
                    getInstance().methods.getDados(alvo.getUniqueId());
                }
                getInstance().dadosInventory.menuDados(alvo, player);
                return;
            }
        }
}
