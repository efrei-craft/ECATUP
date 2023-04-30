package fr.efreicraft.ecatup.utils;

import net.kyori.adventure.text.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.Objects;

public class Msg {
    //TODO: refactor pour delete cette classe !!
    public static String colorize(String message) {
        return ChatColor.translateAlternateColorCodes('&', Objects.requireNonNullElse(message, ""));
    }

    public static String colorize(TextComponent message) {
        return ChatColor.translateAlternateColorCodes('&', Objects.requireNonNullElse(message.content(), ""));
    }
}
