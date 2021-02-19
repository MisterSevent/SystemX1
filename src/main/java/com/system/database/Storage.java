package com.system.database;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Storage {
    public Connection connection;
    private JavaPlugin plugin;
    private boolean debug;
    private boolean useMySQL;
    private boolean showErrors;
    private boolean connected;
    private String host;
    private String user;
    private String password;
    private String database;
    private int port;

    public Storage(String host, int port, String user, String password, String database, boolean mysql, boolean showErrors, boolean debug, JavaPlugin pl) {
        this.useMySQL = mysql;
        this.connected = false;
        this.host = host;
        this.port = port;
        this.password = password;
        this.user = user;
        this.database = database;
        this.plugin = pl;
        this.showErrors = showErrors;
        this.debug = debug;
        if (debug) {
            Bukkit.getConsoleSender().sendMessage("§a[SystemX1] Iniciando conexao com a database...");
        }

        this.loadDatabase();
    }

    public void openConnectionMySQL() {
        try {
            if (this.connection != null && !this.connection.isClosed()) {
                return;
            }

            Class.forName("com.mysql.jdbc.Driver");
            this.connection = DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database + "?autoReconnect=true", this.user, this.password);
            if (this.debug) {
                this.plugin.getLogger().info("O plugin se conectou ao MySQL.");
            }

            this.connected = true;
        } catch (ClassNotFoundException | SQLException var2) {
            Bukkit.getConsoleSender().sendMessage("§c[SystemX1] Ocorreu um erro ao se conectar ao MySQL, mudando armazenamento para SQLite.");
            if (this.showErrors) {
                var2.printStackTrace();
            }

            this.openConnectionSQLite();
        }

    }

    public void openConnectionSQLite() {
        try {
            File file = new File(this.plugin.getDataFolder(), "database.db");
            Class.forName("org.sqlite.JDBC");
            this.connection = DriverManager.getConnection("jdbc:sqlite:" + file);
            if (this.debug) {
                this.plugin.getLogger().info("O plugin se conectou ao SQLite.");
            }

            this.connected = true;
        } catch (Exception var2) {
            Bukkit.getConsoleSender().sendMessage("§c[SystemX1] Ocorreu um erro ao se conectar ao SQLite, desativando plugin.");
            if (this.showErrors) {
                var2.printStackTrace();
            }

            Bukkit.getPluginManager().disablePlugin(this.plugin);
        }

    }

    public void closeConnection() {
        this.plugin.getLogger().info("Fechando conexao com banco de dados...");

        try {
            if (this.connection != null && !this.connection.isClosed()) {
                this.connection.close();
            }

            this.plugin.getLogger().info("Conexao com o banco de dados fechada!");
        } catch (Exception var2) {
            this.plugin.getLogger().info("Falha ao fechar conexao com banco de dados");
            if (this.showErrors) {
                var2.printStackTrace();
            }
        }

    }

    public void loadDatabase() {
        if (this.useMySQL) {
            this.openConnectionMySQL();
        } else {
            this.openConnectionSQLite();
        }

    }

    public void createTable(String table, String colluns) {
        try {
            if (!this.connection.isClosed() && this.connection != null) {
                Statement stm = this.connection.createStatement();
                stm.execute("CREATE TABLE IF NOT EXISTS " + table + " (" + colluns + ");");
            }
        } catch (SQLException var4) {
            this.plugin.getLogger().info("Erro ao criar as tabelas do banco de dados");
            if (this.showErrors) {
                var4.printStackTrace();
            }
        }

    }

    public boolean isConnected() {
        return this.connected;
    }

    public Connection getConnection() {
        return this.connection;
    }

    public boolean isMysql() {
        return this.useMySQL;
    }
}
