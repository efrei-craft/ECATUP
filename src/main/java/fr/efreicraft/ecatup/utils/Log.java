package fr.efreicraft.ecatup.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Log {
    public static void broadcast(String s) {
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&',s));
    }

    public static void sendMessage(Player ply, String s) {
        ply.sendMessage(ChatColor.translateAlternateColorCodes('&',s));
    }
}
