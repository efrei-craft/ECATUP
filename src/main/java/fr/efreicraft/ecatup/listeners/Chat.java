package fr.efreicraft.ecatup.listeners;

import fr.efreicraft.ecatup.Main;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.luckperms.api.LuckPerms;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class Chat implements Listener {
    LuckPerms LP = Main.LP;
    @EventHandler
    public void onChat(AsyncChatEvent event) {
        event.setCancelled(true);
        String playerPrefix = "";
        try {
             playerPrefix = LP.getUserManager().getUser(event.getPlayer().getUniqueId()).getCachedData().getMetaData().getPrefix().replaceAll("&", "§");
        } catch (NullPointerException ignored) {}
        Bukkit.broadcast(event.getPlayer().displayName().append(Component.text(ChatColor.GRAY + ": ")).append(event.message()));
    }
}
