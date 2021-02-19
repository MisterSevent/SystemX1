package com.system.managers;

import com.system.SystemX1;
import com.system.enums.GameStatus;
import com.system.enums.PlayerStatus;
import com.system.integration.SimpleClans.SimpleClansEngine;
import net.milkbowl.vault.economy.Economy;
import net.sacredlabyrinth.phaed.simpleclans.Clan;
import net.sacredlabyrinth.phaed.simpleclans.ClanPlayer;
import net.sacredlabyrinth.phaed.simpleclans.SimpleClans;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.UUID;

import static com.system.SystemX1.*;

public class Arena {
    private String nome;
    private GameStatus status;
    private int tempoRestante;
    private ArrayList<UUID> desafiador = new ArrayList();
    private ArrayList<UUID> desafiado = new ArrayList();
    private Location loc1;
    private Location loc2;
    private Location saida;
    private Location camarote;
    private Economy econ;
    private SystemX1 plugin;
    private BukkitTask task;
    private BukkitTask task1;
    private SimpleClans simpleClans = SimpleClansEngine.getSimpleClans();
    private boolean stateFireFriendly;

    public Arena(String nome, Location loc1, Location loc2, Location saida, Location camarote, SystemX1 main, Economy econ) {
        this.nome = nome;
        this.econ = econ;
        this.plugin = main;
        this.tempoRestante = getConfiguration().getConfig().getInt("Tempo");
        this.status = GameStatus.OPEN;
        this.loc1 = loc1;
        this.loc2 = loc2;
        this.saida = saida;
        this.camarote = camarote;
    }

    public void registrarJogadores(UUID idDesafiador, UUID idAlvo) {
        this.desafiador.add(idDesafiador);
        this.desafiado.add(idAlvo);
    }
    public void setStatus(GameStatus status) {
        this.status = status;
    }

    public GameStatus getStatus() {
        return this.status;
    }

    public int getTempo() {
        return this.tempoRestante;
    }

    public ArrayList<UUID> getDesafiado() {
        return this.desafiado;
    }

    public ArrayList<UUID> getDesafiador() {
        return this.desafiador;
    }

    public void desregistrarJogadores() {
        this.desafiado.clear();
        this.desafiador.clear();
    }

    public void jogoAceito(Player p) {
        ClanPlayer clanPlayer = simpleClans.getClanManager().getClanPlayer(this.getDesafiado().get(0));
        if (clanPlayer != null) {
            stateFireFriendly = SimpleClansEngine.setFireFriendly(this.getDesafiado().get(0), this.getDesafiador().get(0), true);
        }

        Bukkit.getScheduler().cancelTasks(this.plugin);
        setStatus(GameStatus.IN_GAME);
        Bukkit.broadcastMessage(getMessages().getConfig().getString("Aceitou").replace("@desafiado", p.getName()).replace('&', '§'));
        Bukkit.getPlayer((UUID) this.desafiador.get(0)).teleport(this.loc1);
        Bukkit.getPlayer((UUID) this.desafiado.get(0)).teleport(this.loc2);
        Bukkit.broadcastMessage(getMessages().getConfig().getString("AssistirCamarote").replace('&', '§').replace("{arena}", this.nome));
        this.task = Bukkit.getScheduler().runTaskLaterAsynchronously(this.plugin, new Runnable() {
            public void run() {
                Arena.this.tempoAcabou();
            }
        }, (long) (20 * getInstance().getGame().getTempo("Tempo")));
    }

    public void tempoAcabou() {
        Bukkit.getScheduler().cancelTask(task.getTaskId());
        Bukkit.getPlayer((UUID) desafiado.get(0)).teleport(saida);
        Bukkit.getPlayer((UUID) desafiador.get(0)).teleport(saida);
        getInstance().getGame().getPlayerGame((UUID) this.getDesafiador().get(0)).setStatus(PlayerStatus.ONLINE);
        getInstance().getGame().getPlayerGame((UUID) this.getDesafiado().get(0)).setStatus(PlayerStatus.ONLINE);
        desregistrarJogadores();
        setStatus(GameStatus.OPEN);
        tempoRestante = getConfiguration().getConfig().getInt("Tempo");
        Bukkit.broadcastMessage(getMessages().getConfig().getString("Tempo-acabou").replace('&', '§'));
        Bukkit.broadcastMessage(getMessages().getConfig().getString("Arena-disponivel").replace('&', '§').replace("{arena}", nome));
    }

    public void aoVencer(final Player vencedor, Player perdedor) {
        Bukkit.getScheduler().cancelTask(task.getTaskId());
        vencedor.sendMessage(getMessages().getConfig().getString("Vencedor-message").replace('&', '§').replace("@tempo", String.valueOf(getConfiguration().getConfig().getInt("Tempo-coleta"))));
        Bukkit.broadcastMessage(getMessages().getConfig().getString("Ganhador").replace('&', '§').replace("@vencedor", vencedor.getName()).replace("@perdedor", perdedor.getName()));
        Bukkit.getScheduler().runTaskLaterAsynchronously(getInstance(), new Runnable() {
            public void run() {
                vencedor.teleport(Arena.this.saida);
            }
        }, (long) (20 * getConfiguration().getConfig().getInt("Tempo-coleta")));
        econ.withdrawPlayer(perdedor.getName(), getInstance().getGame().getAposta());
        econ.depositPlayer(vencedor.getName(), getInstance().getGame().getAposta());
        this.desregistrarJogadores();
        tempoRestante = this.plugin.getConfig().getInt("Tempo");
        this.desregistrarJogadores();
        this.setStatus(GameStatus.OPEN);
        Bukkit.broadcastMessage(getMessages().getConfig().getString("Arena-disponivel").replace('&', '§').replace("{arena}", nome));
        this.plugin.game.getPlayerGame(vencedor.getUniqueId()).setStatus(PlayerStatus.ONLINE);
        this.plugin.game.getPlayerGame(perdedor.getUniqueId()).setStatus(PlayerStatus.ONLINE);

        SimpleClansEngine.setFireFriendly(vencedor.getUniqueId(), perdedor.getUniqueId(), stateFireFriendly);
    }

    public void tempoArregar(final Player p, final OfflinePlayer alvo) {
        this.task1 = Bukkit.getScheduler().runTaskLaterAsynchronously(this.plugin, new Runnable() {
            public void run() {
                Arena.this.aoArregar(p, alvo);
            }
        }, (long) (20 * getConfiguration().getConfig().getInt("Tempo-aceitar")));
    }

    public void aoArregar(Player p, OfflinePlayer alvo) {
        this.desregistrarJogadores();
        this.plugin.game.getPlayerGame(p.getUniqueId()).setStatus(PlayerStatus.ONLINE);
        this.plugin.game.getPlayerGame(alvo.getUniqueId()).setStatus(PlayerStatus.ONLINE);
        Bukkit.broadcastMessage(getMessages().getConfig().getString("Arregou").replace("@desafiador", p.getName()).replace("@desafiado", alvo.getName()).replace('&', '§'));
        Bukkit.getScheduler().cancelTask(this.task1.getTaskId());
        this.setStatus(GameStatus.OPEN);
        Bukkit.broadcastMessage(getMessages().getConfig().getString("Arena-disponivel").replace('&', '§').replace("{arena}", nome));
    }

}
