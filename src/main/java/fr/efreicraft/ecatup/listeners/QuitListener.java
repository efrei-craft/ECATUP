package fr.efreicraft.ecatup.listeners;

import fr.efreicraft.ecatup.ECATUP;
import fr.efreicraft.ecatup.PreferenceCache;
import fr.efreicraft.ecatup.utils.DiscordWebhook;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.awt.*;
import java.io.IOException;

public class QuitListener implements Listener {
    @EventHandler
    public void onQuit(PlayerQuitEvent event) throws IOException {
//        event.quitMessage(event.getPlayer().displayName().append(Component.text(ChatColor.GRAY + " a quitté le serveur !")));

        // Send log to Discord
//        DiscordWebhook webhook = new DiscordWebhook(ECATUP.getInstance().getConfig().getString("webhook"));
//        String playerName = event.getPlayer().getName();
//        webhook.addEmbed(new DiscordWebhook.EmbedObject()
//                .setTitle("Déconnexion")
//                .setDescription("**" + playerName + "** a quitté le serveur !")
//                .setColor(Color.decode("#ff0000"))
//                .setFooter("Efrei Craft", "https://efreicraft.fr/img/favicon.png")
//        );
//        webhook.execute();
//        PreferenceCache.unCache(event.getPlayer());
    }
}
