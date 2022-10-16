package fr.efreicraft.ecatup.utils;

import org.bukkit.ChatColor;

public class Msg {
    public static String cl(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}
