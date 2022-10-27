package fr.efreicraft.ecatup;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import fr.efreicraft.ecatup.commands.*;
import fr.efreicraft.ecatup.listeners.*;
import fr.efreicraft.ecatup.utils.DiscordWebhook;
import fr.efreicraft.ecatup.listeners.Chat;
import fr.efreicraft.ecatup.listeners.Join;
import fr.efreicraft.ecatup.listeners.LuckPermsListener;
import fr.efreicraft.ecatup.listeners.Quit;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nullable;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
        getServer().getMessenger().registerIncomingPluginChannel(INSTANCE, "BungeeCord", new Chat());

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

        // Send log to Discord
        DiscordWebhook webhook = new DiscordWebhook(config.getString("webhook"));
        webhook.addEmbed(new DiscordWebhook.EmbedObject()
            .setTitle("Serveur")
            .setDescription("Le serveur a démarré !")
            .setColor(java.awt.Color.decode("#ffffff"))
            .setFooter("Efrei Craft", "https://efreicraft.fr/img/favicon.png")
        );
        try {
            webhook.execute();
        } catch (IOException ignored) {}
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic

        // Unregister BungeeCord channel
        getServer().getMessenger().unregisterOutgoingPluginChannel(INSTANCE);
        getServer().getMessenger().unregisterIncomingPluginChannel(INSTANCE);

        // Close database connection
        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // Send log to Discord
        DiscordWebhook webhook = new DiscordWebhook(config.getString("webhook"));
        webhook.addEmbed(new DiscordWebhook.EmbedObject()
                .setTitle("Serveur")
                .setDescription("Le serveur s'est arrêté !")
                .setColor(java.awt.Color.decode("#ffffff"))
                .setFooter("Efrei Craft", "https://efreicraft.fr/img/favicon.png")
        );
        try {
            webhook.execute();
        } catch (IOException ignored) {}
    }

    void registerCommand(String command, CommandExecutor executor) {
        Objects.requireNonNull(Bukkit.getPluginCommand(command)).setExecutor(executor);
    }

    public static List<String> getPlayersForTabList(String[] args, int argPos) {
        List<Player> players = Bukkit.getOnlinePlayers().stream().filter(player -> player.getName().toLowerCase().startsWith(args[argPos].toLowerCase())).collect(Collectors.toList());
        List<String> results = new ArrayList<>();
        players.forEach(player -> results.add(player.getName()));
        players.clear(); // get rid of some space & memory
        return results.isEmpty() ? null : results;
    }

    public static void sendPlayerToServer(Player player, String server) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Connect");
        out.writeUTF(server);
        player.sendPluginMessage(INSTANCE, "BungeeCord", out.toByteArray());
    }

    public static void sendGlobalChat(String msg, @Nullable Player player) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();

        out.writeUTF("Forward");
        out.writeUTF("ONLINE");
        out.writeUTF("ecatup:globalchat");

        ByteArrayOutputStream msgbytes = new ByteArrayOutputStream();
        DataOutputStream msgout = new DataOutputStream(msgbytes);

        try {
            msgout.writeUTF(msg);
        } catch (IOException e) {
            e.printStackTrace();

            INSTANCE.getLogger().severe("Couldn't send " +
                    (player == null ? "a player's" : (player.getName() + "'s")) +
                    " global message: " + msg);
            if (player != null) {
                Component failed = Component.text("Votre dernier message n'a pas été envoyé aux autres serveur.").color(NamedTextColor.DARK_RED);
                player.sendMessage(failed);
            }
        }

        out.writeShort(msgbytes.toByteArray().length);
        out.write(msgbytes.toByteArray());

        Bukkit.getServer().sendPluginMessage(INSTANCE, "BungeeCord", out.toByteArray());
    }
}
