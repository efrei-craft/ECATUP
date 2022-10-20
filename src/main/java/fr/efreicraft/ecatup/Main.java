package fr.efreicraft.ecatup;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import fr.efreicraft.ecatup.commands.*;
import fr.efreicraft.ecatup.listeners.*;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;

public final class Main extends JavaPlugin {

    public static JavaPlugin INSTANCE;
    public static FileConfiguration config;
    public static Connection connection;
    public static LuckPerms LP;

    @Override
    public void onEnable() {

        INSTANCE = this;
        LP = LuckPermsProvider.get();

        // Load config
        saveDefaultConfig();
        config = INSTANCE.getConfig();
        INSTANCE.saveConfig();

        // Connect to MariaDB database

        try {
            Class.forName("org.mariadb.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mariadb://" + config.getString("database.host") + ":" + config.getInt("database.port") + "/" + config.getString("database.database") + "?user=" + config.getString("database.user") + "&password=" + config.getString("database.password"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Register BungeeCord channel
        getServer().getMessenger().registerOutgoingPluginChannel(INSTANCE, "ecatup:sendserver");

        // Register events
        Bukkit.getPluginManager().registerEvents(new Chat(), INSTANCE);
        Bukkit.getPluginManager().registerEvents(new Join(), INSTANCE);
        Bukkit.getPluginManager().registerEvents(new Quit(), INSTANCE);
        Bukkit.getPluginManager().registerEvents(new LuckPermsListener((Main) INSTANCE, LP), INSTANCE);

        // Register commands
        if (Bukkit.getPluginManager().getPlugin("ECLobby") == null) {
            registerCommand("lobby", new Lobby()); // Only register /lobby if ECLobby is not installed
        }
        registerCommand("gm", new Gm());
        registerCommand("gms", new Gms());
        registerCommand("gma", new Gma());
        registerCommand("gmc", new Gmc());
        registerCommand("gmsp", new Gmsp());
        registerCommand("skull", new Skull());
        registerCommand("whois", new WhoIs());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        // Unregister BungeeCord channel
        getServer().getMessenger().unregisterOutgoingPluginChannel(INSTANCE);

        // Close database connection
        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    void registerCommand(String command, CommandExecutor executor) {
        Objects.requireNonNull(Bukkit.getPluginCommand(command)).setExecutor(executor);
    }

    public static void sendPlayerToServer(Player player, String server) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Connect");
        out.writeUTF(server);
        player.sendPluginMessage(INSTANCE, "ecatup:sendserver", out.toByteArray());
    }
}
