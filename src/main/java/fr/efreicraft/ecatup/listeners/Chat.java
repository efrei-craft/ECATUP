package fr.efreicraft.ecatup.listeners;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class Chat implements Listener {
    @EventHandler
    public void onChat(AsyncChatEvent event) {
        event.setCancelled(true);
        Bukkit.broadcast(event.getPlayer().displayName().append(Component.text(ChatColor.GRAY + ": ")).append(event.message()));
    }
}
