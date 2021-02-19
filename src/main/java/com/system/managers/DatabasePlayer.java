package com.system.managers;

import com.system.enums.PlayerStatus;

import java.util.UUID;

public class DatabasePlayer {

    private UUID uuid;
    private int vitorias;
    private int derrotas;
    private int kills;
    private int mortes;
    private boolean loaded;
    private PlayerStatus status;
    private String arena;

    public DatabasePlayer(UUID uuid) {
        this.uuid = uuid;
        this.vitorias = 0;
        this.derrotas = 0;
        this.kills = 0;
        this.mortes = 0;
        this.loaded = false;
        this.status = PlayerStatus.ONLINE;
    }

    public int getVitorias() {
        return vitorias;
    }

    public int getDerrotas() {
        return derrotas;
    }

    public int getKills() {
        return kills;
    }

    public int getMortes() {
        return mortes;
    }

    public String getArena() {
        return arena;
    }

    public PlayerStatus getStatus() {
        return status;
    }

    public boolean getCarregado() {
        return loaded;

    }

    public void setStatus(PlayerStatus status) {
        this.status = status;

    }

    public void setArena(String nome) {
        this.arena = nome;

    }

    public void setCarregado() {
        this.loaded = true;

    }

    public void setVitorias(int resultado) {
        this.vitorias = resultado;

    }

    public void setDerrotas(int resultado) {
        this.derrotas = resultado;

    }

    public void setKills(int resultado) {
        this.kills = resultado;

    }

    public void setMortes(int resultado) {
        this.mortes = resultado;

    }
}
