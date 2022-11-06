package fr.efreicraft.ecatup.listeners;

import fr.efreicraft.ecatup.Main;
import fr.efreicraft.ecatup.PreferenceCache;
import fr.efreicraft.ecatup.utils.DiscordWebhook;
import net.kyori.adventure.text.Component;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.awt.*;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static fr.efreicraft.ecatup.utils.Msg.colorize;

public class Join implements Listener {
    static final LuckPerms LP = Main.LP;
    static Set<UUID> isNotDone = new HashSet<>();

    // Histoire d'exécuter cet event APRES le onJoin de LP
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(org.bukkit.event.player.PlayerJoinEvent event) throws IOException {
        isNotDone.add(event.getPlayer().getUniqueId());
        User user = LP.getUserManager().loadUser(event.getPlayer().getUniqueId()).join();

        // Get player rank from Association Database
        ResultSet result = null;
        try {
            String rank;

            /* ===== ROLES STUFF ===== */
            PreparedStatement mcLinkStatement = Main.DB.openThenGetConnection().prepareStatement("SELECT * FROM `discordmclink` WHERE `mcaccount` = ?");
            mcLinkStatement.setString(1, event.getPlayer().getName());
            Bukkit.getLogger().info("Getting discord ID for " + event.getPlayer().getName());

            // Execute query
            result = mcLinkStatement.executeQuery();
            if (result.next()) {
                String discordId = result.getString("discordid");
                PreparedStatement memberDataStatement = Main.DB.openThenGetConnection().prepareStatement("SELECT * FROM `members` WHERE `discordid` = ?");
                memberDataStatement.setString(1, discordId);
                Bukkit.getLogger().info("Getting rank for " + discordId);
                // Execute query
                ResultSet memberDataResult = memberDataStatement.executeQuery();
                if (memberDataResult.next()) {
                    rank = memberDataResult.getString("rank");
                    Bukkit.getLogger().info("Rank for " + event.getPlayer().getName() + " is " + rank);
                    // Remove player permissions
                    user.data().clear();
                    // Set player rank
                    switch (rank) {
                        case "Membre" -> user.data().add(Node.builder("group.member").build());

                        case "Beta Tester" -> user.data().add(Node.builder("group.beta").build());

                        case "Builder" -> user.data().add(Node.builder("group.builder").build());

                        case "Responsable 1P" -> user.data().add(Node.builder("group.respo1p").build());

                        case "Responsable Event", "Responsable Dev", "Responsable Infra", "Responsable Comm", "Responsable Build", "Responsable Design" -> {
                            user.data().add(Node.builder("group.be").build());
                            user.data().add(Node.builder("prefix.10.&c&l[" + rank + "] &c").build());
                        }

                        case "Président", "Vice-Président", "Trésorier", "Secrétaire" -> {
                            user.data().add(Node.builder("group.br").build());
                            user.data().add(Node.builder("prefix.10.&4&l[" + rank + "] &4").build());
                        }

                        default -> user.data().add(Node.builder("group.visitor").build());
                    }
                    LP.getUserManager().saveUser(LP.getUserManager().loadUser(event.getPlayer().getUniqueId()).join()).join();
                    networkSync();

                    // Check if player has permission to connect to this server
                    if (!event.getPlayer().hasPermission("server." + Main.config.getString("server_name"))) {
                        event.getPlayer().kick(Component.text("§cVous n'avez pas la permission d'accéder ce serveur !"));
                        event.joinMessage(null);
                        return;
                    }
                    
                    memberDataStatement.close();
                    memberDataResult.close();
                } else {
                    Bukkit.getLogger().warning("No rank found for " + event.getPlayer().getName());
                    event.getPlayer().kick(Component.text(colorize("&cVeuillez lier votre compte Discord pour accéder au serveur !")));
                    event.joinMessage(null);
                    return;
                }
            } else {
                Bukkit.getLogger().warning("No rank found for " + event.getPlayer().getName());
                event.getPlayer().kick(Component.text(colorize("&cVeuillez lier votre compte Discord pour accéder au serveur !")));
                event.joinMessage(null);
                return;
            }

            mcLinkStatement.close();
            result.close();
            
            /* ===== PREFERENCES STUFF ===== */
            PreparedStatement userPrefsStatement = Main.DB.openThenGetConnection().prepareStatement("SELECT * FROM `usersPrefs` WHERE `mcUUID` = ?");
            userPrefsStatement.setString(1, event.getPlayer().getUniqueId().toString());
            Bukkit.getLogger().info("Getting " + event.getPlayer().getName() + "'s user preferences");

            ResultSet resultPrefs = userPrefsStatement.executeQuery();
            if (resultPrefs.next()) {
                // récup 1 par 1 les préférences de l'utilisateur
                int channel = resultPrefs.getInt("channel");

                PreferenceCache.cache(event.getPlayer(), channel);
            } else {
                Bukkit.getLogger().info("Player " + event.getPlayer().getName() + " has no settings. Maybe they're a newbie?");
            }

            resultPrefs.close();
            userPrefsStatement.close();
        } catch (SQLException e) {
            isNotDone.remove(event.getPlayer().getUniqueId());
            throw new RuntimeException(e);
        }
        user.getCachedData().invalidate();
        String prefix = LP.getUserManager().loadUser(event.getPlayer().getUniqueId()).join().getCachedData().getMetaData().getPrefix().replaceAll("&", "§");
        event.getPlayer().displayName(Component.text(prefix + event.getPlayer().getName()));
        event.joinMessage(event.getPlayer().displayName().append(Component.text(colorize("&7 a rejoint le serveur !"))));
        event.getPlayer().playerListName(event.getPlayer().displayName());


        // Send log to Discord
        DiscordWebhook webhook = new DiscordWebhook(Main.config.getString("webhook"));
        String playerName = event.getPlayer().getName();
        webhook.addEmbed(new DiscordWebhook.EmbedObject()
                .setTitle("Connexion")
                .setDescription("**" + playerName + "** s'est connecté au serveur !")
                .setColor(Color.decode("#00ff00"))
                .setFooter("Efrei Craft", "https://efreicraft.fr/img/favicon.png")
        );
        webhook.execute();

        isNotDone.remove(event.getPlayer().getUniqueId());
    }

    public static void networkSync() {
        LP.runUpdateTask();
    }

}
