package fr.efreicraft.ecatup.utils;

import fr.efreicraft.ecatup.ECATUP;
import fr.efreicraft.ecatup.players.ECPlayer;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

/**
 * Classe utilitaire pour l'affichage des titres.
 *
 * @author Aurélien D. {@literal <aurelien.dasse@efrei.net>}
 * @project Ludos
 */
public class ActionBarUtils {

    private ActionBarUtils() {
        throw new IllegalStateException("Utility class");
    }


    /**
     * Envoie un action bar à un joueur
     * @param player le joueur qui va recevoir action bar
     * @param message le message envoyé au joueur
     */
    public static void sendActionBar(ECPlayer player, String message){
        player.entity().sendActionBar(LegacyComponentSerializer.legacyAmpersand().deserialize(message));
    }

    /**
     * Envoie un action bar à tous les joueurs
     * @param message le message qui va être envoyé
     */
    public static void broadcastActionBar(String message){
        for(ECPlayer player : ECATUP.getInstance().getPlayerManager().getPlayers()) {
            sendActionBar(player, message);
        }
    }

}
