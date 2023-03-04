package fr.efreicraft.ecatup.utils;

import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Material;

import java.util.EnumMap;
import java.util.Map;

/**
 * Utilitaire de gestion des couleurs.
 *
 * @author Antoine B. {@literal <antoine@jiveoff.fr>}
 * @project EFREI-Minigames
 */
public class ColorUtils {

    /**
     * Record de TeamColorSet.
     */
    public record TeamColorSet(NamedTextColor textColor, DyeColor dyeColor, Color bukkitColor, String code) {

        public TeamColorSet(TeamColors teamColors) {
            this(
                    teamColors.getTeamColorSet().textColor,
                    teamColors.getTeamColorSet().dyeColor,
                    teamColors.getTeamColorSet().bukkitColor,
                    teamColors.getTeamColorSet().code
            );
        }

    }

    /**
     * TeamColors données par défaut.
     */
    public enum TeamColors {
        AQUA(NamedTextColor.AQUA, DyeColor.CYAN, Color.AQUA, "b"),
        BLACK(NamedTextColor.BLACK, DyeColor.BLACK, Color.BLACK, "0"),
        BLUE(NamedTextColor.BLUE, DyeColor.BLUE, Color.BLUE, "9"),
        DARK_AQUA(NamedTextColor.DARK_AQUA, DyeColor.LIGHT_BLUE, Color.TEAL, "3"),
        DARK_BLUE(NamedTextColor.DARK_BLUE, DyeColor.BLUE, Color.NAVY, "1"),
        DARK_GRAY(NamedTextColor.DARK_GRAY, DyeColor.GRAY, Color.GRAY, "8"),
        DARK_GREEN(NamedTextColor.DARK_GREEN, DyeColor.GREEN, Color.GREEN, "2"),
        DARK_PURPLE(NamedTextColor.DARK_PURPLE, DyeColor.PURPLE, Color.PURPLE, "5"),
        DARK_RED(NamedTextColor.DARK_RED, DyeColor.RED, Color.MAROON, "4"),
        GOLD(NamedTextColor.GOLD, DyeColor.ORANGE, Color.ORANGE, "6"),
        GRAY(NamedTextColor.GRAY, DyeColor.LIGHT_GRAY, Color.SILVER, "7"),
        GREEN(NamedTextColor.GREEN, DyeColor.LIME, Color.LIME, "a"),
        LIGHT_PURPLE(NamedTextColor.LIGHT_PURPLE, DyeColor.MAGENTA, Color.FUCHSIA, "d"),
        RED(NamedTextColor.RED, DyeColor.RED, Color.RED, "c"),
        WHITE(NamedTextColor.WHITE, DyeColor.WHITE, Color.WHITE, "f"),
        YELLOW(NamedTextColor.YELLOW, DyeColor.YELLOW, Color.YELLOW, "e");

        private final TeamColorSet teamColorSet;

        /**
         * Constructeur de TeamColors.
         * @param textColor Couleur du texte.
         * @param dyeColor Couleur de la laine.
         * @param bukkitColor Couleur Bukkit (utilisable pour des particules...).
         */
        TeamColors(NamedTextColor textColor, DyeColor dyeColor, Color bukkitColor, String code) {
            this.teamColorSet = new TeamColorSet(textColor, dyeColor, bukkitColor, code);
        }

        /**
         * Récupérer le TeamColorSet.
         * @return TeamColorSet.
         */
        public TeamColorSet getTeamColorSet() {
            return teamColorSet;
        }
    }

    public static TeamColorSet getTeamColorSetFromCode(String code) {
        String codeToUse = code;
        if(codeToUse.length() > 1) {
            codeToUse = codeToUse.substring(1, 2);
        }

        for (TeamColors teamColors : TeamColors.values()) {
            if (teamColors.getTeamColorSet().code.equals(codeToUse)) {
                return teamColors.getTeamColorSet();
            }
        }
        return null;
    }

}

