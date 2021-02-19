package com.system.commands.X1SubCommands.locations;

import com.system.entities.SubCommand;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import static com.system.SystemX1.*;
import static com.system.SystemX1.getInstance;

public class SetLoc1Command implements SubCommand {
    @Override
    public void onCommand(Player player, Command command, String label, String[] args) {
        if (args.length == 2) {
            if (player.hasPermission(getConfiguration().getString("Permissions.admin"))) {
                getLocations().set("Arenas." + args[1] + "." + "Player1." + ".Location", player.getLocation());
                getLocations().set("Arenas." + args[1] + "." + ".Name", args[1]);
                player.sendMessage(getMessages().getString("location1-seted").replace('&', '§').replace("{arena}", args[1]));
                getLocations().saveConfig();
                return;
            } else {
                player.sendMessage(getMessages().getString("no-permission").replace('&', '§'));
                return;
            }
        } else {
            player.sendMessage(getInstance().getMessages().getString("args-insufficient-setloc1").replace('&', '§'));
            return;
        }
    }
}
