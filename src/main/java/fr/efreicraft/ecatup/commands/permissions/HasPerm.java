package fr.efreicraft.ecatup.commands.permissions;

import fr.efreicraft.animus.endpoints.PlayerService;
import fr.efreicraft.animus.invoker.ApiException;
import fr.efreicraft.animus.models.Permission;
import fr.efreicraft.ecatup.ECATUP;
import fr.efreicraft.ecatup.players.ECPlayer;
import fr.efreicraft.ecatup.utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class HasPerm implements CommandExecutor, TabExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        OfflinePlayer player;
        String permission;
        boolean fromDB = false;

        if (args.length == 2) {
            player = Bukkit.getOfflinePlayer(args[0]);
            permission = args[1];
        } else if (args.length == 3) {
            player = Bukkit.getOfflinePlayer(args[0]);
            permission = args[1];
            fromDB = Boolean.parseBoolean(args[2].toLowerCase());
        } else {
            MessageUtils.sendMessage(sender, MessageUtils.ChatPrefix.SERVER, "&cSyntaxe invalide : /hasperm <player> <permission> <fromDB:true|false>");
            return true;
        }

        if (!player.isOnline()) fromDB = true;

        permission = permission.toLowerCase();

        if (fromDB) {
            try {
                List<Permission> perms = PlayerService.getPermissionOfPlayer(player.getUniqueId().toString());
                for (Permission p : perms) {
                    if (p.getName().startsWith(permission)) {
                        MessageUtils.sendMessage(sender, MessageUtils.ChatPrefix.SERVER,
                                "%s possède la permission &a[%s]&r dans [%s].".formatted(player.getName(), permission, String.join(",", p.getServerTypes())));
                        return true;
                    }
                }

                MessageUtils.sendMessage(sender, MessageUtils.ChatPrefix.SERVER,
                        "%s ne possède pas la permission &a[%s]&r.".formatted(player.getName(), permission));
            } catch (ApiException e) {
                MessageUtils.sendMessage(sender, MessageUtils.ChatPrefix.SERVER,
                        "&cLa base de données n'a pas répondu, ou a retourné une erreur. Veuillez voir les logs.");
                Bukkit.getLogger().severe("Couldn't fetch %s's permissions".formatted(player.getName()));
                Bukkit.getLogger().severe("It was to test permission [%s] (initiated by %s)".formatted(permission, sender.getName()));
                Bukkit.getLogger().severe("Code: %s Message: %s".formatted(e.getCode(), e.getMessage()));
            }
        } else {
            ECPlayer ecPlayer = ECATUP.getInstance().getPlayerManager().getPlayer(player.getPlayer());
            if (ecPlayer == null) {
                MessageUtils.sendMessage(sender, MessageUtils.ChatPrefix.SERVER,
                        "&cWtf ? Joueur non trouvé...?");
                return true;
            }


            for (Permission p : ecPlayer.getAnimusPlayer().getPerms()) {
                if (p.getName().startsWith(permission)) {
                    MessageUtils.sendMessage(sender, MessageUtils.ChatPrefix.SERVER,
                            "%s possède la permission &a[%s]&r dans [%s].".formatted(player.getName(), permission, String.join(",", p.getServerTypes())));
                    return true;
                }
            }

            MessageUtils.sendMessage(sender, MessageUtils.ChatPrefix.SERVER,
                    "%s ne possède pas la permission &a[%s]&r.".formatted(player.getName(), permission));
        }

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) return Bukkit.getOnlinePlayers().stream().map(Player::getName).sorted().toList();
        if (args.length == 3) return List.of("true", "false");
        return null;
    }
}
