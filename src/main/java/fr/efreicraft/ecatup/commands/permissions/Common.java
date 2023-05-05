package fr.efreicraft.ecatup.commands.permissions;

import fr.efreicraft.animus.models.PermGroup;
import fr.efreicraft.animus.models.PermGroupPlayer;
import fr.efreicraft.ecatup.ECATUP;
import fr.efreicraft.ecatup.utils.MessageUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Common {

    // Copilot va bientôt nous remplacer, je vous le dis.
    public enum ErrorMessages {
        NO_PERMISSION("&cVous n'avez pas la permission d'utiliser cette commande."),
        NOT_ENOUGH_ARGUMENTS("&cPas assez d'arguments."),
        TOO_MANY_ARGUMENTS("&cTrop d'arguments."),
        BAD_ARGUMENT("&cMauvais argument : '%s'"),
        PLAYER_NOT_FOUND("&cLe joueur %s n'est pas connecté."),
        GROUP_NOT_FOUND("&e%s &cn'existe pas dans la base de données !"),
        PLAYER_NOT_IN_GROUP("&e%s&c n'est pas dans ce groupe."),
        PLAYER_ALREADY_IN_GROUP("&cLe joueur %s est déjà dans ce groupe."),
        GROUP_ALREADY_EXISTS("&e%s &cexiste déjà dans la base de données !"),
        COULD_NOT_REACH_DATABASE("&cBase de données inaccessible."),
        BAD_DURATION("&cMauvaise durée donnée : '%s'");

        private String msg = "";
        ErrorMessages(String s) {
            msg = s;
        }
        public String get() {
            return msg;
        }

        public String format(String... args) {
            return msg.formatted(args);
        }
    }

    public static void sendGroupHelp(CommandSender player) {
        MessageUtils.sendMessage(player, MessageUtils.ChatPrefix.PLUGIN, "&3&l======= AIDE =======");

        MessageUtils.sendMessage(player, "  &8» &e/gperms groups &3Afficher les groupes");
        MessageUtils.sendMessage(player, "  &8» &e/gperms <group> &3Afficher les infos sur <group>");
        MessageUtils.sendMessage(player, "  &8» &e/gperms <group> add <player> &3Ajoute <player> à <group>");
        MessageUtils.sendMessage(player, "  &8» &e/gperms <group> remove <player> &3Supprime <player> de <group>");
        MessageUtils.sendMessage(player, "  &8» &e/gperms <group> +<perm> &3Ajoute une permission au joueur");
        MessageUtils.sendMessage(player, "  &8» &e/gperms <group> setPrefix [newPrefix] &3Change ou annule le préfixe du groupe (entre ')");
        MessageUtils.sendMessage(player, "  &8» &e/gperms <group> -<perm> &3Retire une permission au joueur");
        MessageUtils.sendMessage(player, "");
        MessageUtils.sendMessage(player, "  &8» &e/gperms add <group> &3Crée un groupe");
        MessageUtils.sendMessage(player, "  &8» &e/gperms remove <group> &3Supprime un groupe");
    }

    public static void sendPlayerHelp(CommandSender player) {
        MessageUtils.sendMessage(player, MessageUtils.ChatPrefix.PLUGIN, "&3&l======= AIDE =======");

        MessageUtils.sendMessage(player, "  &8» &e/pperms <player> &3Afficher les groupes du joueur");
        MessageUtils.sendMessage(player, "  &8» &e/pperms <player> +<perm> &3Ajoute une permission au joueur");
        MessageUtils.sendMessage(player, "  &8» &e/pperms <player> setPrefix [newPrefix] &3Change ou réinitialise le préfixe du joueur (entre ')");
        MessageUtils.sendMessage(player, "  &8» &e/pperms <player> -<perm> &3Retire une permission au joueur");
    }

    // TODO: ajouter des infos ?
    public static void printPlayerInfoTo(CommandSender to, Player player) {
        MessageUtils.sendMessage(to, MessageUtils.ChatPrefix.PLUGIN, "&3======= &r%s&l%s &3=======".formatted(player.isOnline() ? "&a" : "&4", player.getName()));
        // ew.
        MessageUtils.sendMessage(to, "  &8• Groupes : " + String.join(", ",
                ECATUP.getInstance()
                .getPlayerManager()
                .getPlayer(player)
                .getAnimusPlayer()
                .getPermGroups()
                        .stream().map(PermGroupPlayer::getName)
                        .toList()
        ));
    }

    public static void printGroupInfoTo(CommandSender to, PermGroup group) {
        MessageUtils.sendMessage(to, MessageUtils.ChatPrefix.PLUGIN, "&3======= &r&l%s &r&3=======".formatted(group.getName()));
        // ew.
        MessageUtils.sendMessage(to, "  &8• Préfixe : &r%s".formatted(group.getPrefix()));
        MessageUtils.sendMessage(to, "  &8• Priorité : &r%s".formatted(group.getPriority()));
        PermGroup parentGroup = ECATUP.getInstance().getGroupManager().getGroup(group.getParentGroupId());
        MessageUtils.sendMessage(to, "  &8• Group parent : &r%s".formatted(
                parentGroup == null ? "Aucun" : (parentGroup.getName() + " &8&o(id " + parentGroup.getId() + ")")
        ));

        if (group.getPermissions().isEmpty())
            MessageUtils.sendMessage(to, "  &8• &cAucune permission.");
        else {
            MessageUtils.sendMessage(to, "  &8• Permission(s) :");
            final int displayed = 10; // Nombre de permissions affichées dans le tchat
            for (int i = 0; i < Math.min(group.getPermissions().size(), displayed); i++) {
                MessageUtils.sendMessage(to, "   &8- &r%s".formatted(group.getPermissions().get(i).getName().replaceAll("\\*", "&e*&r")));
            }
            if (group.getPermissions().size() > displayed)
                MessageUtils.sendMessage(to, "   &8&o...%s en plus".formatted(group.getPermissions().size() - displayed));
        }

        MessageUtils.sendMessage(to, "  &8• Groupe par défaut ? " + (group.isDefaultGroup() ? "&a&oOui" : "&c&oNon"));
    }

}
