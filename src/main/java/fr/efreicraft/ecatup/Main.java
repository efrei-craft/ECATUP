package fr.efreicraft.ecatup;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import fr.efreicraft.ecatup.commands.*;
import fr.efreicraft.ecatup.commands.gamemode.*;
import fr.efreicraft.ecatup.commands.speeds.FlySpeed;
import fr.efreicraft.ecatup.commands.speeds.ResetSpeed;
import fr.efreicraft.ecatup.commands.speeds.WalkSpeed;
import fr.efreicraft.ecatup.listeners.Chat;
import fr.efreicraft.ecatup.listeners.Join;
import fr.efreicraft.ecatup.listeners.Quit;
import fr.efreicraft.ecatup.utils.DiscordWebhook;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nullable;
import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@SuppressWarnings("UnstableApiUsage")
public final class Main extends JavaPlugin {

    public static JavaPlugin INSTANCE;
    public static FileConfiguration config;


    @Override
    public void onEnable() {

        INSTANCE = this;

        // Load config
        saveDefaultConfig();
        config = INSTANCE.getConfig();
        config.options().copyDefaults(true); // au cas où le fichier existe, mais est incomplet.
        INSTANCE.saveConfig();

        // Register BungeeCord channel
        getServer().getMessenger().registerOutgoingPluginChannel(INSTANCE, "BungeeCord");
        getServer().getMessenger().registerIncomingPluginChannel(INSTANCE, "BungeeCord", new Chat());

        // Register events
        Bukkit.getPluginManager().registerEvents(new Chat(), INSTANCE);
        Bukkit.getPluginManager().registerEvents(new Join(), INSTANCE);
        Bukkit.getPluginManager().registerEvents(new Quit(), INSTANCE);

        // Register commands
        if (!config.getString("server_name", "").equals("lobby")) {
            registerCommand("lobby", new Lobby()); // Only register /lobby if the server's name is "lobby"
        }

        registerCommand("chat", new fr.efreicraft.ecatup.commands.Chat());
        for (PreferenceCache.ChatChannel channel : PreferenceCache.ChatChannel.values()) {
            Bukkit.getPluginManager().addPermission(new Permission("ecatup.channel." + channel.toString().toLowerCase()));
        }

        registerCommand("flyspeed", new FlySpeed());
        registerCommand("gm", new Gm());
        registerCommand("gms", new Gms());
        registerCommand("gma", new Gma());
        registerCommand("gmc", new Gmc());
        registerCommand("gmsp", new Gmsp());
        registerCommand("resetspeed", new ResetSpeed());
        registerCommand("skull", new Skull());
        registerCommand("slap", new Slap());
        registerCommand("sudo", new Sudo());
        registerCommand("walkspeed", new WalkSpeed());
        registerCommand("whois", new WhoIs());

        // Send log to Discord
        DiscordWebhook webhook = new DiscordWebhook(config.getString("webhook"));
        webhook.addEmbed(new DiscordWebhook.EmbedObject()
            .setTitle("Serveur")
            .setDescription("Le serveur a démarré !")
            .setColor(Color.white)
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

        if (!config.getString("server_name", "").equalsIgnoreCase("lobby"))
            for (Player player : Bukkit.getOnlinePlayers()) {
                sendPlayerToServer(player, "lobby");
            }

        try {
            // Send log to Discord
            DiscordWebhook webhook = new DiscordWebhook(config.getString("webhook"));
            webhook.addEmbed(new DiscordWebhook.EmbedObject()
                    .setTitle("Serveur")
                    .setDescription("Le serveur s'est arrêté !")
                    .setColor(java.awt.Color.decode("#ffffff"))
                    .setFooter("Efrei Craft", "https://efreicraft.fr/img/favicon.png")
            );
//            webhook.execute();
        } catch (Throwable ignored) {}
    }

    void registerCommand(String command, CommandExecutor executor) {
        Objects.requireNonNull(Bukkit.getPluginCommand(command)).setExecutor(executor);
    }

    public static List<String> getPlayersForTabList(String[] args, int argPos) {
        List<Player> players = Bukkit.getOnlinePlayers().stream().filter(player -> player.getName().toLowerCase().startsWith(args[argPos].toLowerCase())).collect(Collectors.toList());
        List<String> results = new ArrayList<>();
        players.forEach(player -> results.add(player.getName()));
        return results.isEmpty() ? null : results;
    }

    public static void sendPlayerToServer(Player player, String server) {
        if (!player.hasPermission("server." + server.toLowerCase())) {
            Component nope = Component.text("Vous ne pouvez pas aller sur ce serveur !").color(NamedTextColor.RED);
            player.sendMessage(nope);
            INSTANCE.getLogger().info(player.getName() + " tried to join " + server + " but doesn't have permission to do so.");
            return;
        }

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
                Component failed = Component.text("Votre dernier message n'a pas été envoyé aux autres serveurs.").color(NamedTextColor.DARK_RED);
                player.sendMessage(failed);
            }
        }

        out.writeShort(msgbytes.toByteArray().length);
        out.write(msgbytes.toByteArray());

        Bukkit.getServer().sendPluginMessage(INSTANCE, "BungeeCord", out.toByteArray());
    }
}
