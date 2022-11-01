package fr.efreicraft.ecatup.utils;

import net.kyori.adventure.text.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.Objects;

public class Msg {
    public static String colorize(String message) {
        return ChatColor.translateAlternateColorCodes('&', Objects.requireNonNullElse(message, "null"));
    }

    public static String colorize(TextComponent message) {
        return ChatColor.translateAlternateColorCodes('&', Objects.requireNonNullElse(message.content(), nullText()));
    }

    private static String nullText() {
        Bukkit.getLogger().warning("Tried to colorize a null text (LuckPerms is at it again...)");
        return "null";
    }
}
