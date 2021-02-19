package com.system.database;

import com.system.SystemX1;
import com.system.managers.DatabasePlayer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class Methods {
    private SystemX1 plugin;
    private static PreparedStatement stm = null;
    private DatabasePlayer player = null;
    private int resultado;
    private static Connection con = SystemX1.getStorageManager().getConnection();

    public Methods(SystemX1 plugin) {
        this.plugin = plugin;
    }

    public static boolean hasAccount(UUID uuid) {
        try {
            String sql = "SELECT * FROM `SystemX1` WHERE `id` = ?";
            stm = con.prepareStatement(sql);
            stm.setObject(1, uuid.toString());
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                return true;
            }
        }catch (SQLException var1) {
            var1.printStackTrace();
        }
        return false;
    }

    public static void createAccount(UUID uuid, String jogador, int vitorias, int derrotas, int kills, int mortes) {

        try {
            String sql = "INSERT INTO SystemX1(id, player_name, vitorias, derrotas, kills, mortes) VALUES (?,?,?,?,?,?)";
            stm = con.prepareStatement(sql);
            stm.setObject(1, uuid.toString());
            stm.setString(2, jogador);
            stm.setInt(3, vitorias);
            stm.setInt(4, derrotas);
            stm.setInt(5, kills);
            stm.setInt(6, mortes);
            stm.executeUpdate();
        }catch (SQLException var2) {
            var2.printStackTrace();
        }
    }

    public int getVitorias(UUID id) {
        try {
            String sql = "SELECT * FROM SystemX1 WHERE id = ?";
            stm = con.prepareStatement(sql);
            stm.setObject(1, id.toString());
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                this.resultado = rs.getInt("vitorias");
                return this.resultado;
            }
            return 0;
        }catch (SQLException var3) {
            var3.printStackTrace();
        }
        return 0;
    }

    public int getDerrotas(UUID id) {

        try {
            String sql = "SELECT * FROM SystemX1 WHERE id = ?";
            stm = con.prepareStatement(sql);
            stm.setString(1, id.toString());
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                this.resultado = rs.getInt("derrotas");
                return this.resultado;
            }
            return 0;
        }catch (SQLException var3) {
            var3.printStackTrace();
        }
        return 0;
    }

    public int getKills(UUID id) {

        try {
            String sql = "SELECT * FROM SystemX1 WHERE id = ?";
            stm = con.prepareStatement(sql);
            stm.setString(1, id.toString());
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                this.resultado = rs.getInt("kills");
                return this.resultado;
            }
            return 0;
        }catch (SQLException var3) {
            var3.printStackTrace();
        }
        return 0;
    }

    public int getMortes(UUID id) {

        try {
            String sql = "SELECT * FROM SystemX1 WHERE id = ?";
            stm = con.prepareStatement(sql);
            stm.setString(1, id.toString());
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                this.resultado = rs.getInt("mortes");
                return this.resultado;
            }
            return 0;
        }catch (SQLException var3) {
            var3.printStackTrace();
        }
        return 0;
    }

    public void getDados(UUID id) {
        this.player = this.plugin.game.getPlayerGame(id);
        this.player.setCarregado();
        this.player.setDerrotas(this.getDerrotas(id));
        this.player.setVitorias(this.getVitorias(id));
        this.player.setKills(this.getKills(id));
        this.player.setMortes(this.getMortes(id));

    }

    public void atualizarDadosVencedor(UUID idVencedor) {
        try {
            String sql = "UPDATE SystemX1 SET vitorias = vitorias + 1, kills = kills + 1 WHERE id = ?";
            stm = con.prepareStatement(sql);
            stm.setObject(1, idVencedor.toString());
            stm.executeUpdate();
            this.player = this.plugin.game.getPlayerGame(idVencedor);
            if (this.player.getCarregado()) {
                this.player.setVitorias(this.player.getVitorias() + 1);
                this.player.setKills(this.player.getKills() + 1);
            }
        }catch (SQLException var5) {
            var5.printStackTrace();
        }
    }
    public void atualizarDadosPerdedor(UUID idPerdedor) {
        try {
            String sql = "UPDATE SystemX1 SET derrotas = derrotas + 1, mortes = mortes + 1 WHERE id = ?";
            stm = con.prepareStatement(sql);
            stm.setObject(1, idPerdedor.toString());
            stm.executeUpdate();
            this.player = this.plugin.game.getPlayerGame(idPerdedor);
            if (this.player.getCarregado()) {
                this.player.setVitorias(this.player.getDerrotas() + 1);
                this.player.setMortes(this.player.getMortes() + 1);
            }
        }catch (SQLException var6) {
            var6.printStackTrace();
        }
    }
}
