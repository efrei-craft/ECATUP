package fr.efreicraft.ecatup.commands.permissions;

import fr.efreicraft.animus.models.PermGroupPlayer;
import fr.efreicraft.ecatup.ECATUP;
import fr.efreicraft.ecatup.utils.MessageUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Common {

    public static void sendGroupHelp(CommandSender player) {
        MessageUtils.sendMessage(player, MessageUtils.ChatPrefix.PLUGIN, "&3&l======= AIDE =======");

        MessageUtils.sendMessage(player, "  &8» &e/gperms groups &3Afficher les groupes");
        MessageUtils.sendMessage(player, "  &8» &e/gperms <group> &3Afficher les infos sur <group>");
        MessageUtils.sendMessage(player, "  &8» &e/gperms <group> add <player> &3Ajoute <player> à <group>");
        MessageUtils.sendMessage(player, "  &8» &e/gperms <group> remove <player> &3Supprime <player> de <group>");
        MessageUtils.sendMessage(player, "  &8» &e/gperms <group> +<perm> [expiresIn] [context] &3Ajoute une permission au joueur");
        MessageUtils.sendMessage(player, "  &8» &e/gperms <group> setPrefix [newPrefix] &3Change ou annule le préfixe du groupe (entre ')");
        MessageUtils.sendMessage(player, "  &8» &e/gperms <group> -<perm> &3Retire une permission au joueur");
        MessageUtils.sendMessage(player, "");
        MessageUtils.sendMessage(player, "  &8» &e/gperms add <group> &3Crée un groupe");
        MessageUtils.sendMessage(player, "  &8» &e/gperms remove <group> &3Supprime un groupe");
    }

    public static void sendPlayerHelp(CommandSender player) {
        MessageUtils.sendMessage(player, MessageUtils.ChatPrefix.PLUGIN, "&3&l======= AIDE =======");

        MessageUtils.sendMessage(player, "  &8» &e/pperms <player> &3Afficher les groupes du joueur");
        MessageUtils.sendMessage(player, "  &8» &e/pperms <player> +<perm> [expiresIn] [[context1],[context2],[context3] OR ALL] &3Ajoute une permission au joueur");
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

}
