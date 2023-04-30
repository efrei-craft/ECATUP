package fr.efreicraft.ecatup.utils;

import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

/**
 * Utilitaire d'animation de chaînes de caractères.
 *
 * @author Antoine B. {@literal <antoine@jiveoff.fr>}
 * @project EFREI-Minigames
 */
public class StringAnimationUtils {

    private StringAnimationUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Fonction d'animation de chaîne de caractères pour le scoreboard.
     * @param string Chaîne de caractères à animer.
     * @param charColor Couleur du caractère.
     * @param transitionColor Couleur de transition.
     * @param backgroundColor Couleur de fond.
     * @return Liste des chaînes de caractères animées.
     */
    public static List<String> generateColorStrings(String string, ChatColor charColor, ChatColor transitionColor, ChatColor backgroundColor) {
        ArrayList<String> strings = new ArrayList<>();
        strings.add(string);
        strings.add("" + transitionColor + string.charAt(0) + backgroundColor + string.substring(1));
        for(int i = 0; i < string.length(); i++) {
            StringBuilder builder = new StringBuilder();
            for(int j = 0; j < string.length(); j++) {
                if(j == i) {
                    builder.append(charColor).append(string.charAt(j));
                } else if(j == i - 1 || j == i + 1) {
                    builder.append(transitionColor).append(string.charAt(j));
                } else {
                    builder.append(backgroundColor).append(string.charAt(j));
                }
            }
            strings.add(builder.toString());
        }
        strings.add(string.substring(0, string.length() - 1) + transitionColor + string.charAt(string.length() - 1));
        strings.add(string);
        return strings;
    }

}
