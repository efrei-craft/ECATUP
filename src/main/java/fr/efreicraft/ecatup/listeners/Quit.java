package fr.efreicraft.ecatup.listeners;

import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class Quit implements Listener {
    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        event.quitMessage(event.getPlayer().displayName().append(Component.text(ChatColor.GRAY + " a quitté le serveur !")));
    }
}
