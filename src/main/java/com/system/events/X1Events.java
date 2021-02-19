package com.system.events;

import com.system.SystemX1;
import com.system.enums.GameStatus;
import com.system.enums.PlayerStatus;
import com.system.inventorys.ArenasInventory;
import com.system.managers.Arena;
import com.system.managers.DatabasePlayer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Iterator;
import java.util.UUID;

import static com.system.SystemX1.*;

public class X1Events implements Listener {
    private SystemX1 plugin;
    private DatabasePlayer player;
    private DatabasePlayer playerAlvo;
    private Arena arena;
    private ArenasInventory arenasInventory;

    @EventHandler
    private void jogadorMorre(PlayerDeathEvent e) {
        Player p = e.getEntity();
        Player killer = e.getEntity().getKiller();
        if (getInstance().getGame().getDesafiado().contains(p.getUniqueId()) || getInstance().getGame().getDesafiador().contains(p.getUniqueId())) {
            player = getInstance().getGame().getPlayerGame(p.getUniqueId());
            arena = getInstance().getGame().getArena(player.getArena());
            if (arena.getDesafiado().contains(p.getUniqueId())) {
                arena.aoVencer(killer, p);
            }

            if (arena.getDesafiador().contains(p.getUniqueId())) {
                arena.aoVencer(killer, p);
            }

        }
    }

    @EventHandler
    private void jogadorSai(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        if (getInstance().getGame().getDesafiado().contains(p.getUniqueId()) || getInstance().getGame().getDesafiador().contains(p.getUniqueId())) {
            player = getInstance().getGame().getPlayerGame(p.getUniqueId());
            arena = getInstance().getGame().getArena(player.getArena());
            Player p2;
            if (arena.getDesafiado().contains(p.getUniqueId())) {
                p2 = Bukkit.getPlayer((UUID) arena.getDesafiador().get(0));
                arena.aoVencer(p2, p);
            }

            if (arena.getDesafiador().contains(p.getUniqueId())) {
                p2 = Bukkit.getPlayer((UUID) arena.getDesafiado().get(0));
                arena.aoVencer(p, p2);
            }
        }

    }

    @EventHandler
    private void jogadorTentarPegarDoChao(PlayerPickupItemEvent e) {
        Player p = e.getPlayer();
        getInstance().getGame().registrarPlayerGame(p.getUniqueId());
        player = getInstance().getGame().getPlayerGame(p.getUniqueId());
        if (player.getStatus() == PlayerStatus.CAMAROTE) {
            e.setCancelled(true);
        }

    }

    @EventHandler
    private void aoBaterJogador(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player && e.getEntity() instanceof Player) {
            Player p = (Player) e.getDamager();
            Player alvo = (Player) e.getEntity();
            getInstance().getGame().registrarPlayerGame(p.getUniqueId());
            player = getInstance().getGame().getPlayerGame(p.getUniqueId());
            if (player.getStatus() == PlayerStatus.CAMAROTE) {
                e.setCancelled(true);
            }

            getInstance().getGame().registrarPlayerGame(alvo.getUniqueId());
            playerAlvo = getInstance().getGame().getPlayerGame(alvo.getUniqueId());
            if (player.getStatus() == PlayerStatus.ONLINE && playerAlvo.getStatus() == PlayerStatus.IN_GAME) {
                e.setCancelled(true);
            }
        }

    }

    @EventHandler
    private void clicarInventario(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if (e.getInventory().getTitle().contains("§1§2§3§8")) {
            if(e.getClickedInventory() == null) {
                return;
            }
            if (!(e.getCurrentItem().getType() == Material.AIR)) {
                if (e.getCurrentItem() != null) {
                    if (e.getCurrentItem().getItemMeta().getLore().contains("§7Status: §cEm Andamento!")) {
                        String nome = e.getCurrentItem().getItemMeta().getDisplayName().replace("§aArena §e", "");
                        getInstance().getGame().registrarArena(nome);
                        if (getInstance().getGame().getArena(nome).getStatus() == GameStatus.IN_GAME) {
                            p.openInventory(new ArenasInventory(0, p).informacoesArena(p, nome));
                            return;
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    private void ClickarInventario1(InventoryClickEvent e) {
        if (e.getInventory().getTitle().contains("§7Informações Arena:")) {
            e.setCancelled(true);
        } else {
            return;
        }

    }

    @EventHandler(ignoreCancelled = true)
    void blockComands(PlayerCommandPreprocessEvent e) {
        Player p = e.getPlayer();
        getInstance().getGame().registrarPlayerGame(p.getUniqueId());
        player = getInstance().getGame().getPlayerGame(p.getUniqueId());
        if (player.getStatus() == PlayerStatus.IN_GAME) {
            if (!p.hasPermission("x1.admin")) {
                Iterator var3 = getConfiguration().getConfig().getStringList("ComandosLiberados").iterator();

                //Enquanto/Enquanto houver um elemento dentro desta lista de strings ele retornara TRUE!
                while (var3.hasNext()) {
                    String comando = (String) var3.next();

                    //Ele verifica a palavra e se aquela palavra inicia com tal palavra ele retorna true ou false
                    //MÉTODO case-sensitive!!!! tomar cuidado.
                    if (e.getMessage().startsWith(comando)) {
                        return;
                    }
                }
                e.getPlayer().sendMessage(getMessages().getConfig().getString("commands-in-x1").replace('&', '§').replace("{comando}", e.getMessage()));
                e.setCancelled(true);
            } else {
                return;
            }
        } else {
            return;
        }
    }
}
