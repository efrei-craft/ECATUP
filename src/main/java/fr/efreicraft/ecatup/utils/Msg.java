package fr.efreicraft.ecatup.utils;

import org.bukkit.ChatColor;

public class Msg {
    public static String colorize(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}
