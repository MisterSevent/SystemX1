package com.system.commands.X1SubCommands;

import com.system.entities.SubCommand;
import com.system.inventorys.ArenasInventory;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import static com.system.SystemX1.*;

public class ArenasCommand implements SubCommand {
    @Override
    public void onCommand(Player player, Command command, String label, String[] args) {
        if (player.hasPermission(getConfiguration().getConfig().getString("Permissions.arenas"))) {
            if (getLocations().getConfig().getConfigurationSection("Arenas") == null) {
                player.sendMessage("§cNenhuma arena foi setada ainda.");
                return;
            }
            player.openInventory(new ArenasInventory(0, player).getInventory());
        } else {
            player.sendMessage(getMessages().getString("no-permission").replace('&', '§'));
            return;
        }
    }
}
