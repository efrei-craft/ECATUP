package fr.efreicraft.ecatup.listeners;

import fr.efreicraft.ecatup.Main;
import net.kyori.adventure.text.Component;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.node.Node;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Join implements Listener {
    LuckPerms LP = Main.LP;
    @EventHandler
    public void onJoin(org.bukkit.event.player.PlayerJoinEvent event) {
        // Get player rank from Association Database
        try {
            String rank = "Visiteur";
            PreparedStatement mcLinkStatement = Main.connection.prepareStatement("SELECT * FROM `discordmclink` WHERE `mcaccount` = ?");
            mcLinkStatement.setString(1, event.getPlayer().getName());
            Bukkit.getLogger().info("Getting discord ID for " + event.getPlayer().getName());
            // Execute query
            ResultSet result = mcLinkStatement.executeQuery();
            if (result.next()) {
                String discordId = result.getString("discordid");
                PreparedStatement memberDataStatement = Main.connection.prepareStatement("SELECT * FROM `members` WHERE `discordid` = ?");
                memberDataStatement.setString(1, discordId);
                Bukkit.getLogger().info("Getting rank for " + discordId);
                // Execute query
                ResultSet memberDataResult = memberDataStatement.executeQuery();
                if (memberDataResult.next()) {
                    rank = memberDataResult.getString("rank");
                    Bukkit.getLogger().info("Rank for " + event.getPlayer().getName() + " is " + rank);
                    // Remove player permissions
                    LP.getUserManager().getUser(event.getPlayer().getUniqueId()).data().clear();
                    // Set player rank
                    switch (rank) {
                        case "Membre" -> LP.getUserManager().getUser(event.getPlayer().getUniqueId()).data().add(Node.builder("group.member").build());
                        case "Beta Tester" -> LP.getUserManager().getUser(event.getPlayer().getUniqueId()).data().add(Node.builder("group.beta").build());
                        case "Responsable Event", "Responsable Dev", "Responsable Infra", "Responsable Comm", "Responsable Build", "Responsable Design" -> {
                            LP.getUserManager().getUser(event.getPlayer().getUniqueId()).data().add(Node.builder("group.be").build());
                            LP.getUserManager().getUser(event.getPlayer().getUniqueId()).data().add(Node.builder("prefix.10.&c&l[" + rank + "] &c").build());
                        }
                        case "Président", "Vice-Président", "Trésorier", "Secrétaire" -> {
                            LP.getUserManager().getUser(event.getPlayer().getUniqueId()).data().add(Node.builder("group.br").build());
                            LP.getUserManager().getUser(event.getPlayer().getUniqueId()).data().add(Node.builder("prefix.10.&4&l[" + rank + "] &4").build());
                        }
                        default -> LP.getUserManager().getUser(event.getPlayer().getUniqueId()).data().add(Node.builder("group.visitor").build());
                    }
                    LP.getUserManager().saveUser(LP.getUserManager().getUser(event.getPlayer().getUniqueId()));
                    // Check if player has permission to connect to this server
                    if (!event.getPlayer().hasPermission("server." + Main.config.getString("server_name"))) {
                        event.getPlayer().kick(Component.text("§cVous n'avez pas la permission d'accéder ce serveur !"));
                        event.joinMessage(null);
                        return;
                    }
                }
                else {
                    Bukkit.getLogger().warning("No rank found for " + event.getPlayer().getName());
                    event.getPlayer().kick(Component.text("§cVeuillez lier votre compte Discord pour accéder au serveur !"));
                    event.joinMessage(null);
                    return;
                }
            }
            else {
                Bukkit.getLogger().warning("No rank found for " + event.getPlayer().getName());
                event.getPlayer().kick(Component.text("§cVeuillez lier votre compte Discord pour accéder au serveur !"));
                event.joinMessage(null);
                return;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        String prefix = LP.getUserManager().getUser(event.getPlayer().getUniqueId()).getCachedData().getMetaData().getPrefix().replaceAll("&", "§");
        event.getPlayer().displayName(Component.text(prefix + event.getPlayer().getName()));
        event.joinMessage(event.getPlayer().displayName().append(Component.text("§7 a rejoint le serveur !")));
        event.getPlayer().playerListName(event.getPlayer().displayName());
    }
}
