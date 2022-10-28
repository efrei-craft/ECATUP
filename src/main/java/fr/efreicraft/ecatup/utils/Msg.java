package fr.efreicraft.ecatup.utils;

import net.kyori.adventure.text.TextComponent;
import org.bukkit.ChatColor;

public class Msg {
    public static String colorize(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static String colorize(TextComponent message) {
        return ChatColor.translateAlternateColorCodes('&', message.content());
    }
}
