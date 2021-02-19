package com.system.managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import com.system.SystemX1;
import com.system.enums.GameStatus;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Location;
import org.bukkit.plugin.RegisteredServiceProvider;

import static com.system.SystemX1.*;

public class Game {
    private SystemX1 plugin = null;
    private GameStatus gameStatus = null;
    private Economy econ = null;
    private DatabasePlayer player;
    private ArrayList<UUID> desafiador;
    private ArrayList<UUID> desafiado;
    private HashMap<UUID, DatabasePlayer> playersInGame;
    private HashMap<String, Arena> arena;

    public Game(SystemX1 main) {
        this.plugin = main;
        this.desafiador = new ArrayList();
        this.desafiado = new ArrayList();
        this.playersInGame = new HashMap();
        this.arena = new HashMap();
        this.setStatus(GameStatus.OPEN);
        this.setupEconomy();
    }

    public ArrayList<UUID> getDesafiador() {
        return this.desafiador;
    }

    public ArrayList<UUID> getDesafiado() {
        return this.desafiado;
    }

    public void setStatus(GameStatus status) {
        this.gameStatus = status;
    }

    public GameStatus getStatus() {
        return this.gameStatus;
    }

    public boolean registrarArena(String nome) {
        if (!this.arena.containsKey(nome)) {
            Location loc1 = (Location) getLocations().getConfig().get("Arenas." + nome + "." + "Player1." + ".Location");
            Location loc2 = (Location) getLocations().getConfig().get("Arenas." + nome + "." + "Player2." + ".Location");
            Location saida = (Location) getLocations().getConfig().get("Arenas." + nome + "." + "Saida." + ".Location");
            Location camarote = (Location) getLocations().getConfig().get("Arenas." + nome + "." + "Camarote." + ".Location");
            this.arena.put(nome, new Arena(nome, loc1, loc2, saida, camarote, this.plugin, this.econ));
            return true;
        } else {
            return false;
        }
    }

    public Economy getEcon() {
        return this.econ;
    }

    public Arena getArena(String nome) {
        return this.arena.containsKey(nome) ? (Arena)this.arena.get(nome) : null;
    }

    public boolean registrarPlayerGame(UUID id) {
        if (!this.playersInGame.containsKey(id)) {
            this.playersInGame.put(id, this.player = new DatabasePlayer(id));
            return true;
        } else {
            return false;
        }
    }

    public DatabasePlayer getPlayerGame(UUID id) {
        return this.playersInGame.containsKey(id) ? (DatabasePlayer) this.playersInGame.get(id) : null;
    }

    public double getDinheiro(String nome) {
        return this.econ.getBalance(nome);
    }

    public double getAposta() {
        return getConfiguration().getConfig().getInt("Dinheiro");
    }

    public int getTempo(String string) {
        return getConfiguration().getConfig().getInt(string);
    }

    private boolean setupEconomy() {
        if (this.plugin.getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        } else {
            RegisteredServiceProvider<Economy> rsp = this.plugin.getServer().getServicesManager().getRegistration(Economy.class);
            if (rsp == null) {
                return false;
            } else {
                this.econ = (Economy)rsp.getProvider();
                return this.econ != null;
            }
        }
    }

    public void registrarJogadores(UUID idDesafiador, String arena, UUID idDesafiado) {
        this.desafiador.add(idDesafiador);
        this.desafiado.add(idDesafiado);
    }
}