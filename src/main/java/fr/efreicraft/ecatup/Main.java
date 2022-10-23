package fr.efreicraft.ecatup;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import fr.efreicraft.ecatup.commands.*;
import fr.efreicraft.ecatup.listeners.Chat;
import fr.efreicraft.ecatup.listeners.Join;
import fr.efreicraft.ecatup.listeners.LuckPermsListener;
import fr.efreicraft.ecatup.listeners.Quit;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Properties;

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
        config = INSTANCE.getConfig();
        config.addDefault("server_name", "lobby");
        config.addDefault("database.host", "127.0.0.1");
        config.addDefault("database.port", 3306);
        config.addDefault("database.database", "db");
        config.addDefault("database.user", "user");
        config.addDefault("database.password", "pwd");

        config.options().copyDefaults(true);
        saveConfig();

        // Connect to MariaDB database

        try {
            Class.forName("org.mariadb.jdbc.Driver");
            getLogger().severe("IP:"+config.getString("database.host"));
            connection = DriverManager.getConnection("jdbc:mariadb://"
                    + config.getString("database.host")
                    + ":" + config.getInt("database.port") + "/" + config.getString("database.database")
                    + "?user=" + config.getString("database.user")
                    + "&password=" + config.getString("database.password")
                    + "&autoReconnect=true");
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Register BungeeCord channel
        getServer().getMessenger().registerOutgoingPluginChannel(INSTANCE, "BungeeCord");
        getServer().getMessenger().registerOutgoingPluginChannel(INSTANCE, "ecatup:globalchat");

        // Register events
        Bukkit.getPluginManager().registerEvents(new Chat(), INSTANCE);
        Bukkit.getPluginManager().registerEvents(new Join(), INSTANCE);
        Bukkit.getPluginManager().registerEvents(new Quit(), INSTANCE);
        Bukkit.getPluginManager().registerEvents(new LuckPermsListener((Main) INSTANCE, LP), INSTANCE);

        // Register commands
        if (Bukkit.getPluginManager().getPlugin("ECLobby") == null) {
            registerCommand("lobby", new Lobby()); // Only register /lobby if ECLobby is not installed
        }

        registerCommand("chat", new fr.efreicraft.ecatup.commands.Chat());
        for (PreferenceCache.ChatChannel channel : PreferenceCache.ChatChannel.values()) {
            Bukkit.getPluginManager().addPermission(new Permission("ecatup.channel." + channel.toString().toLowerCase()));
        }

        registerCommand("gm", new Gm());
        registerCommand("gms", new Gms());
        registerCommand("gma", new Gma());
        registerCommand("gmc", new Gmc());
        registerCommand("gmsp", new Gmsp());
        registerCommand("skull", new Skull());
        registerCommand("slap", new Slap());
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
        player.sendPluginMessage(INSTANCE, "BungeeCord", out.toByteArray());
    }
}
