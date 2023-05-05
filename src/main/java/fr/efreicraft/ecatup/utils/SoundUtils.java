package fr.efreicraft.ecatup.utils;

import fr.efreicraft.ecatup.ECATUP;
import fr.efreicraft.ecatup.players.ECPlayer;
import org.bukkit.Sound;

/**
 * Classe utilitaire pour jouer un son.
 *
 * @author Antoine B. {@literal <antoine@jiveoff.fr>}
 * @project Ludos
 */
public class SoundUtils {

    private SoundUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Joue un son à un joueur.
     * @param player Joueur à qui envoyer le son.
     * @param sound Son à envoyer.
     */
    public static void playSound(ECPlayer player, Sound sound, float volume, float pitch) {
        player.entity().playSound(player.entity().getLocation(), sound, volume, pitch);
    }

    /**
     * Joue un son à tous les joueurs.
     * @param sound Son à jouer.
     */
    public static void broadcastSound(Sound sound, float volume, float pitch) {
        for(ECPlayer player : ECATUP.getInstance().getPlayerManager().getPlayers()) {
            playSound(player, sound, volume, pitch);
        }
    }

}
