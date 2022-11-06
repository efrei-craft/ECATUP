package fr.efreicraft.ecatup.utils;

import org.bukkit.Bukkit;
import org.mariadb.jdbc.Configuration;
import org.mariadb.jdbc.Connection;
import org.mariadb.jdbc.Driver;

import javax.annotation.Nullable;
import java.sql.SQLException;

public class DBConnection {

    private Connection connection = null;
    private final String HOST;
    private final String DATABASE;
    private final String USER;
    private final String PASSWORD;
    private final int PORT;

    public DBConnection(String host, int port, String database, String user, String password) {
        this.HOST = host;
        this.PORT = port;
        this.DATABASE = database;
        this.USER = user;
        this.PASSWORD = password;
    }

    public boolean open() {
        try {
            connection = Driver.connect(new Configuration.Builder()
                    .addHost(this.HOST, this.PORT)
                    .database(this.DATABASE)
                    .user(this.USER)
                    .password(this.PASSWORD)
                    .socketTimeout(0)
                    .maxIdleTime(0)
                    .build());
        } catch (SQLException e) {
            e.printStackTrace();
            Bukkit.getLogger().severe("Couldn't open connection to database!");
            return false;
        }
        return true;
    }

    public void close() {
        try {
            if (connection != null)
                connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            Bukkit.getLogger().severe("Couldn't close connection to database!");
        }
    }

    @Nullable
    public Connection openThenGetConnection() {
        if (connection == null || connection.isClosed()) {
            if (!this.open()) {
                Bukkit.getLogger().severe("Tried to reopen a connection, and failed.");
                return null;
            }
        }

        return connection;
    }

    /**
     * Cette fonction retourne directement la connexion, sans vérifier si elle existe. Elle est bien plus dangereuse !
     * @return connection La connexion à la connexion à la DB
     */
    @Nullable
    public Connection getConnection() {
        return connection;
    }
}
