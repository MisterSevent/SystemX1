package com.system.entities;

import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public interface SubCommand {

    void onCommand(Player player, Command command, String label, String[] args);

}
