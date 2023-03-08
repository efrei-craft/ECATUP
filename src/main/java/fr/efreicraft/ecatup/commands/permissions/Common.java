package fr.efreicraft.ecatup.commands.permissions;

import fr.efreicraft.ecatup.utils.MessageUtils;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.command.CommandSender;

public class Common {

    public static void sendGroupHelp(CommandSender player) {
        MessageUtils.sendMessage(player, MessageUtils.ChatPrefix.PLUGIN, "&3&l======= AIDE =======");

        LegacyComponentSerializer serializer = LegacyComponentSerializer.legacyAmpersand();
        player.sendMessage(serializer.deserialize("  &8» &e/gperms groups &3Afficher les groupes"));
        player.sendMessage(serializer.deserialize("  &8» &e/gperms <group> &3Afficher les infos sur <group>"));
        player.sendMessage(serializer.deserialize("  &8» &e/gperms <group> +<perm> [expiresIn] [context] &3Ajoute une permission au joueur"));
        player.sendMessage(serializer.deserialize("  &8» &e/gperms <group> setPrefix [newPrefix] &3Change ou annule le préfixe du groupe (entre ')"));
        player.sendMessage(serializer.deserialize("  &8» &e/gperms <group> -<perm> &3Retire une permission au joueur"));
    }

    public static void sendPlayerHelp(CommandSender player) {
        MessageUtils.sendMessage(player, MessageUtils.ChatPrefix.PLUGIN, "&3&l======= AIDE =======");

        LegacyComponentSerializer serializer = LegacyComponentSerializer.legacyAmpersand();
        player.sendMessage(serializer.deserialize("  &8» &e/pperms <player> &3Afficher les groupes du joueur"));
        player.sendMessage(serializer.deserialize("  &8» &e/pperms <player> +<perm> [expiresIn] [context] &3Ajoute une permission au joueur"));
        player.sendMessage(serializer.deserialize("  &8» &e/pperms <player> setPrefix [newPrefix] &3Change ou réinitialise le préfixe du joueur (entre ')"));
        player.sendMessage(serializer.deserialize("  &8» &e/pperms <player> -<perm> &3Retire une permission au joueur"));
    }

}
