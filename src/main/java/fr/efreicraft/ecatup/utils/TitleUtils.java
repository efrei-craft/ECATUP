package fr.efreicraft.ecatup.utils;

import fr.efreicraft.ecatup.ECATUP;
import fr.efreicraft.ecatup.players.ECPlayer;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.title.Title;
import net.kyori.adventure.util.Ticks;
import org.jetbrains.annotations.NotNull;

/**
 * Classe utilitaire pour l'affichage des titres.
 *
 * @author Antoine B. {@literal <antoine@jiveoff.fr>}
 * @project Ludos
 */
public class TitleUtils {

    private TitleUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Construis un title à l'écran.
     *
     * @param title    Title à afficher.
     * @param subtitle Subtitle à afficher.
     * @param fadeIn   Temps d'apparition du title en secondes.
     * @param stay     Temps d'affichage du title en secondes.
     * @param fadeOut  Temps de disparition du title en secondes.
     * @return Le title construit.
     */
    public static @NotNull Title buildTitle(String title, String subtitle, float fadeIn, float stay, float fadeOut) {
        return Title.title(
                LegacyComponentSerializer.legacyAmpersand().deserialize(title),
                LegacyComponentSerializer.legacyAmpersand().deserialize(subtitle),
                Title.Times.times(
                        Ticks.duration((long) (20L * fadeIn)),
                        Ticks.duration((long) (20L * stay)),
                        Ticks.duration((long) (20L * fadeOut))
                )
        );
    }

    /**
     * Envoie un title à un joueur.
     * @param player    Joueur à qui envoyer le title.
     * @param title     Title à envoyer.
     * @param subtitle  Subtitle à envoyer.
     * @param fadeIn    Temps d'apparition du title en secondes.
     * @param stay      Temps d'affichage du title en secondes.
     * @param fadeOut   Temps de disparition du title en secondes.
     */
    public static void sendTitle(ECPlayer player, String title, String subtitle, float fadeIn, float stay, float fadeOut) {
        player.entity().showTitle(buildTitle(title, subtitle, fadeIn, stay, fadeOut));
    }

    /**
     * Envoie un title à tous les joueurs.
     * @param title     Title à envoyer.
     * @param subtitle  Subtitle à envoyer.
     * @param fadeIn    Temps d'apparition du title en secondes.
     * @param stay      Temps d'affichage du title en secondes.
     * @param fadeOut   Temps de disparition du title en secondes.
     */
    public static void broadcastTitle(String title, String subtitle, float fadeIn, float stay, float fadeOut) {
        for(ECPlayer player : ECATUP.getInstance().getPlayerManager().getPlayers()) {
            sendTitle(player, title, subtitle, fadeIn, stay, fadeOut);
        }
    }

}
