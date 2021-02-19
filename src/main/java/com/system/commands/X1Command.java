package com.system.commands;

import com.system.SystemX1;
import com.system.commands.X1SubCommands.*;
import com.system.commands.X1SubCommands.locations.SetCamaroteCommand;
import com.system.commands.X1SubCommands.locations.SetLoc1Command;
import com.system.commands.X1SubCommands.locations.SetLoc2Command;
import com.system.commands.X1SubCommands.locations.SetSaidaCommand;
import com.system.database.Methods;
import com.system.entities.SubCommand;
import com.system.enums.GameStatus;
import com.system.enums.PlayerStatus;
import com.system.inventorys.ArenasInventory;
import com.system.managers.Arena;
import com.system.managers.DatabasePlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

import static com.system.SystemX1.*;

public class X1Command implements CommandExecutor {

    private Arena arena;
    private static DatabasePlayer player = null;
    private static DatabasePlayer playerAlvo = null;
    private ArenasInventory arenasInventory;


    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String lb, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(getMessages().getString("console-execute").replace('&', '§'));
            return true;
        }
        Player p = (Player) sender;
        if (cmd.getName().equalsIgnoreCase("x1")) {
            if (args.length == 0) {
                if (p.hasPermission("x1.admin")) {
                    for (String helpadmin : getMessages().getConfig().getStringList("Helps.admin")) {
                        String replaced = helpadmin.replace('&', '§');
                        p.sendMessage(replaced);
                    }
                    return true;
                }
                for (String helpplayer : getMessages().getConfig().getStringList("Helps.player")) {
                    String replaced = helpplayer.replace('&', '§');
                    p.sendMessage(replaced);
                }
                return true;
            }

            HashMap<String, SubCommand> subCommands = new HashMap<>();

            subCommands.put("setcamarote", new SetCamaroteCommand());
            subCommands.put("setloc1", new SetLoc1Command());
            subCommands.put("setloc2", new SetLoc2Command());
            subCommands.put("setsaida", new SetSaidaCommand());
            subCommands.put("aceitar", new AceitarCommand());
            subCommands.put("arenas", new ArenasCommand());
            subCommands.put("camarote", new CamaroteCommand());
            subCommands.put("dados", new DadosCommand());
            subCommands.put("desafiar", new DesafiarCommand());
            subCommands.put("encerrar", new EncerrarCommand());

            if (!subCommands.containsKey(args[0].toLowerCase())) {
                if (p.hasPermission("x1.admin")) {
                    for (String helpadmin : getMessages().getConfig().getStringList("Helps.admin")) {
                        String replaced = helpadmin.replace('&', '§');
                        p.sendMessage(replaced);
                    }
                    return true;
                }
                for (String helpplayer : getMessages().getConfig().getStringList("Helps.player")) {
                    String replaced = helpplayer.replace('&', '§');
                    p.sendMessage(replaced);
                }
                return true;
            }
            SubCommand subCommand = subCommands.get(args[0].toLowerCase());
            subCommand.onCommand((Player) sender, cmd, lb ,args);
        }
        return false;
    }

}
