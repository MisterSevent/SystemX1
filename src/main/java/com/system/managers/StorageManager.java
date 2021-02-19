package com.system.managers;

import com.system.SystemX1;
import com.system.database.Storage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class StorageManager {
    private Storage storage;
    SystemX1 plugin;

    public StorageManager(SystemX1 plugin) {
        this.plugin = plugin;

        String host = plugin.getConfiguration().getString("Database.storage.host");
        int port = plugin.getConfiguration().getInt("Database.storage.port");
        String user = plugin.getConfiguration().getString("Database.storage.user");
        String pass = plugin.getConfiguration().getString("Database.storage.password");
        String database = plugin.getConfiguration().getString("Database.storage.database");
        boolean mysql = false;
        if (plugin.getConfiguration().getString("Database.storage.type").equals("mysql")) {
            mysql = true;
        }

        boolean showErrors = plugin.getConfiguration().getBoolean("Database.storage.show-errors");
        this.storage = new Storage(host, port, user, pass, database, mysql, showErrors, true, plugin);
        this.storage.createTable("SystemX1", "id varchar(36), player_name varchar(16), vitorias int, derrotas int, kills int, mortes int");
    }

    public boolean executeCommand(String sqlCommand) {
        try {
            PreparedStatement psm = this.getConnection().prepareStatement(sqlCommand.replace("%s%", "SystemX1"));
            psm.execute();
            psm.close();
            return true;
        } catch (SQLException var3) {
            var3.printStackTrace();
            return false;
        }
    }

    public ResultSet executeQuery(String sqlCommand) {
        try {
            PreparedStatement psm = this.getConnection().prepareStatement(sqlCommand.replace("%s%", "SystemX1"));
            ResultSet rs = psm.executeQuery();
            return rs;
        } catch (SQLException var4) {
            return null;
        }
    }

    public void closeConnection() {
        if (this.storage.isConnected()) {
            this.storage.closeConnection();
        }

    }

    public Connection getConnection() {
        return this.storage != null && this.storage.isConnected() ? this.storage.getConnection() : null;
    }
}
