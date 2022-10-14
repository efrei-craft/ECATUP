package fr.efreicraft.ecatup.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Messages {
    public static void info(String s) {
        Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&',s));
    }

    public static void warn(String s) {
        Bukkit.getLogger().warning(ChatColor.translateAlternateColorCodes('&',s));
    }

    public static void severe(String s) {
        Bukkit.getLogger().severe(ChatColor.translateAlternateColorCodes('&',s));
    }

    public static void broadcast(String s) {
        // FICK DU DICH!!!!!
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&',s));
    }

    public static void sendMessage(Player p, String s) {
        p.sendMessage(ChatColor.translateAlternateColorCodes('&',s));
    }

    public static void sendMessage(CommandSender p, String s) {
        sendMessage((Player) p, s);
    }
}
