package fr.efreicraft.ecatup.players.scoreboards.interfaces;

import fr.efreicraft.ecatup.players.Player;

/**
 * Interface fonctionnelle pour les lambda de récupération de valeur de scoreboard dynamique.
 *
 * @author Antoine B. {@literal <antoine@jiveoff.fr>}
 * @project EFREI-Minigames
 */
@FunctionalInterface
public interface IDynamicScoreboardFieldValue {

    /**
     * Renvoie la valeur du field. Cette valeur est dynamique et peut changer en fonction du joueur passé par la lambda.
     * @param player Le joueur
     * @return La chaîne de caractère de valeur du field.
     * La coloration se fait avec les {@link net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer#legacyAmpersand()}.
     */
    String value(Player player);

}
