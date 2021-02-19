package com.system.commands.X1SubCommands;

import com.system.entities.SubCommand;
import com.system.enums.PlayerStatus;
import com.system.inventorys.ArenasInventory;
import com.system.managers.Arena;
import com.system.managers.DatabasePlayer;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import static com.system.SystemX1.*;
import static com.system.SystemX1.getMessages;



public class CamaroteCommand implements SubCommand {

    private Arena arena;
    private static DatabasePlayer player = null;

    @Override
    public void onCommand(Player player, Command command, String label, String[] args) {
        boolean CamaroteItemVazio = getConfiguration().getConfig().getBoolean("CamaroteItemVazio");

        if (player.hasPermission(getConfiguration().getConfig().getString("Permissions.camarote"))) {
            if (args.length == 2) {
                if (!getLocations().getConfig().contains("Arenas." + args[1])) {
                    player.sendMessage(getMessages().getConfig().getString("arena-invalid").replace('&', '§'));
                    return;
                }
                if (CamaroteItemVazio) {
                    if (!this.checarInv(player) && this.checarArmadura(player)) {
                        getInstance().getGame().registrarArena(args[1]);
                        arena = getInstance().getGame().getArena(args[1]);
                        getInstance().getGame().registrarPlayerGame(player.getUniqueId());
                        this.player = getInstance().getGame().getPlayerGame(player.getUniqueId());
                        if (this.player.getStatus() == PlayerStatus.ONLINE) {
                            this.player.setStatus(PlayerStatus.CAMAROTE);
                            getInstance().getGhostMaker().setAsGhost(player, args[1]);
                            return;
                        } else {
                            this.player.setStatus(PlayerStatus.ONLINE);
                            getInstance().getGhostMaker().setAsNormal(player, args[1]);
                            return;
                        }
                    } else {
                        player.sendMessage(getMessages().getConfig().getString("inventory-void").replace('&', '§'));
                        return;
                    }
                } else {
                    player.sendMessage(args[1]);
                    getInstance().getGame().registrarArena(args[1]);
                    arena = getInstance().getGame().getArena(args[1]);
                    getInstance().getGame().registrarPlayerGame(player.getUniqueId());
                    this.player = getInstance().getGame().getPlayerGame(player.getUniqueId());
                    if (this.player.getStatus() == PlayerStatus.ONLINE) {
                        this.player.setStatus(PlayerStatus.CAMAROTE);
                        getInstance().getGhostMaker().setAsGhost(player, args[1]);
                        return;
                    } else {
                        this.player.setStatus(PlayerStatus.ONLINE);
                        getInstance().getGhostMaker().setAsNormal(player, args[1]);
                        return;
                    }
                }
            } else {
                player.sendMessage(getMessages().getString("args-insufficient-camarote").replace('&', '§'));
                return;
            }
        } else {
            player.sendMessage(getMessages().getString("no-permission").replace('&', '§'));
            return;
        }
    }

    public boolean checarInv(Player p) {
        ItemStack[] var5;
        int var4 = (var5 = p.getInventory().getContents()).length;

        for (int var3 = 0; var3 < var4; ++var3) {
            ItemStack item = var5[var3];
            if (item != null) {
                return true;
            }
        }

        return false;
    }

    public boolean checarArmadura(Player p) {
        return p.getInventory().getHelmet() == null && p.getInventory().getChestplate() == null && p.getInventory().getLeggings() == null && p.getInventory().getBoots() == null;
    }

}
