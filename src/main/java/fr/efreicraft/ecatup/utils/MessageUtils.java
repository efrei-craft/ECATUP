package fr.efreicraft.ecatup.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Utilitaire de formatteur de messages.
 *
 * @author Antoine B. {@literal <antoine@jiveoff.fr>}
 */
public class MessageUtils {

    private static final String SEPARATOR = " &8» &r";

    /**
     * Enum des préfixes de messages.
     */
    public enum ChatPrefix {

        /**
         * Préfixe basique pour le plugin.
         */
        MINIGAMES("&bLudos"),

        /**
         * Préfixe pour le serveur.
         */
        SERVER("&6Serveur"),

        /**
         * Préfixe pour les messages correspondant au jeu.
         */
        GAME,

        /**
         * Préfixe pour les messages venant de commandes admin.
         */
        ADMIN("&cAdmin"),

        /**
         * Préfixes pour les messages concernant les cartes.
         */
        MAP("&5Carte"),

        /**
         * Préfixe pour les messages concernant les équipes (assignations aux équipes etc).
         */
        TEAM("&dÉquipe"),

        /**
         * Préfixe vide.
         */
        EMPTY("");

        private final String prefix;

        /**
         * Constructeur de ChatPrefix.
         * @param prefix Préfixe du message.
         */
        ChatPrefix(String prefix) {
            this.prefix = prefix;
        }

        /**
         * Constructeur de ChatPrefix pour les messages de jeu.
         */
        ChatPrefix() {
            this.prefix = "&6Jeu";
        }

        /**
         * Retourne le préfixe formaté.
         * @return Préfixe formaté.
         */
        @Override
        public String toString() {
            if(this == EMPTY) {
                return "";
            }
            return this.prefix + SEPARATOR;
        }
    }

    /**
     * Récupérer le texte d'un message.
     * @param prefix Préfixe du message.
     * @param text Texte du message (coloration par esperluette).
     * @return Texte du message.
     */
    public static String getText(ChatPrefix prefix, String text) {
        return ChatColor.translateAlternateColorCodes('&', prefix + text);
    }

    /**
     * Envoyer un message à des joueurs.
     * @param players Joueurs.
     * @param prefix Préfixe du message.
     * @param message Message (coloration par esperluette).
     */
    public static void sendMessage(CommandSender[] players, ChatPrefix prefix, String message) {
        for (CommandSender player : players) {
            player.sendMessage(getText(prefix, message));
        }
    }

    /**
     * Envoyer un message à un joueur.
     * @param player Joueur.
     * @param prefix Préfixe du message.
     * @param message Message (coloration par esperluette).
     */
    public static void sendMessage(CommandSender player, ChatPrefix prefix, String message) {
        sendMessage(new CommandSender[]{player}, prefix, message);
    }

    /**
     * Envoyer un message à un joueur.
     * @param player Joueur.
     * @param prefix Préfixe du message.
     * @param message Message (coloration par esperluette).
     */
    public static void sendMessage(CommandSender player, String message) {
        sendMessage(new CommandSender[]{player}, ChatPrefix.EMPTY, message);
    }

    /**
     * Envoyer un message à tous les joueurs.
     * @param prefix Préfixe du message.
     * @param message Message (coloration par esperluette).
     */
    public static void broadcastMessage(ChatPrefix prefix, String message) {
        sendMessage(Bukkit.getOnlinePlayers().toArray(new Player[0]), prefix, message);
    }

    /**
     * Envoyer un message à tous les joueurs sans préfixe.
     * @param message Message (coloration par esperluette).
     */
    public static void broadcastMessage(String message) {
        sendMessage(Bukkit.getOnlinePlayers().toArray(new Player[0]), ChatPrefix.EMPTY, message);
    }

}
