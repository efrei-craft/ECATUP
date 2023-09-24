package fr.efreicraft.ecatup.utils;

import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.title.Title;
import net.kyori.adventure.util.Ticks;
import org.jetbrains.annotations.NotNull;

public class TimeUtils {

    private TimeUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Construis une chaîne de caractères correspondant au temps donné.
     * @param time Temps à formater en secondes.
     * @return La chaîne de caractères formatée.
     */
    public static @NotNull String formatTime(long time) {
        // if days > 0, then format like this: 0j 00h 00m 00s
        // else hours > 0, then format like this: 00h 00m 00s
        // else minutes > 0, then format like this: 00m 00s
        // else seconds > 0, then format like this: 00s
        // else format like this: 0s

        long days = time / 86400;
        long hours = (time % 86400) / 3600;
        long minutes = (time % 3600) / 60;
        long seconds = time % 60;

        if (days > 0) {
            return days + "j " + hours + "h " + minutes + "m " + seconds + "s";
        } else if (hours > 0) {
            return hours + "h " + minutes + "m " + seconds + "s";
        } else if (minutes > 0) {
            return minutes + "m " + seconds + "s";
        } else if (seconds > 0) {
            return seconds + "s";
        } else {
            return "0s";
        }
    }

}
