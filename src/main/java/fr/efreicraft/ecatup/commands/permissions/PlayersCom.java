package fr.efreicraft.ecatup.commands.permissions;

import fr.efreicraft.animus.endpoints.PlayerService;
import fr.efreicraft.animus.invoker.ApiException;
import fr.efreicraft.animus.models.PermissionInput;
import fr.efreicraft.ecatup.ECATUP;
import fr.efreicraft.ecatup.utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static fr.efreicraft.ecatup.commands.permissions.Common.ErrorMessages.*;

public class PlayersCom implements TabExecutor {

    private static final List<String> NEVER_EXPIRE = List.of(
            "null",
            "never",
            "-1",
            "0",
            "jamais"
    );

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            Common.sendPlayerHelp(sender);
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);

        if (target == null) {
            MessageUtils.sendMessage(sender, MessageUtils.ChatPrefix.PLUGIN, PLAYER_NOT_FOUND.format(args[0]));
            return true;
        }

        switch (args.length) {
            // Info joueur
            case 1 -> Common.printPlayerInfoTo(sender, target);

            case 2 -> {
                if (args[1].startsWith("+")) {
                    String permission = args[1].substring(1);
                    try {
                        PlayerService.addPermissions(target.getUniqueId().toString(), List.of(new PermissionInput().name(permission)));
                        ECATUP.getInstance().getPlayerManager().getPlayer(target).updatePermission(permission, true);
                    } catch (ApiException e) {
                        MessageUtils.sendMessage(sender, COULD_NOT_REACH_DATABASE.get());
                        Bukkit.getLogger().severe("=== ERREUR API ===");
                        Bukkit.getLogger().severe("%s a essayé d'ajouter la permission '%s' à %s".formatted(sender.getName(), permission, target.getName()));
                        Bukkit.getLogger().severe("Message d'exception API : %s".formatted(e.getMessage()));
                        Bukkit.getLogger().severe("Code de réponse API : %s".formatted(e.getCode()));
                        return true;
                    }

                    // Message de succès
                    MessageUtils.sendMessage(sender, "&8[&a+&8] &e%s &8<- &e%s".formatted(target.getName(), permission));
                }
                else if (args[1].startsWith("-")) {
                    String permission = args[1].substring(1);
                    try {
                        PermissionInput permissionInput = new PermissionInput().name(permission);

                        PlayerService.revokePermissions(target.getUniqueId().toString(), List.of(permissionInput));
                        ECATUP.getInstance().getPlayerManager().getPlayer(target).addPlayerPermissions();
                    } catch (ApiException e) {
                        MessageUtils.sendMessage(sender, COULD_NOT_REACH_DATABASE.get());
                        Bukkit.getLogger().severe("=== ERREUR API ===");
                        Bukkit.getLogger().severe("%s a essayé de révoquer la permission '%s' à %s".formatted(sender.getName(), permission, target.getName()));
                        Bukkit.getLogger().severe("Message d'exception API : %s".formatted(e.getMessage()));
                        Bukkit.getLogger().severe("Code de réponse API : %s".formatted(e.getCode()));
                        return true;
                    }

                    // Message de succès
                    MessageUtils.sendMessage(sender, "&8[&4-&8] &e%s &8<- &e%s".formatted(target.getName(), permission));
                } else if (args[1].equalsIgnoreCase("setPrefix")) {
                    //TODO: préfixes joueurs ajoutés ??
                    MessageUtils.sendMessage(sender, "&cSoon...");
                }
                else {
                    MessageUtils.sendMessage(sender, BAD_ARGUMENT.format(args[1]));
                }
            }

            // Ajout de préfixe OU ajout de perm avec contexte
            case 3 -> {
                if (args[1].equalsIgnoreCase("setPrefix")) {
                    //TODO: préfixes joueurs ajoutés ??
                } else
                    Common.sendPlayerHelp(sender);
            }

            default -> Common.sendPlayerHelp(sender);
        }
        return true;
    }
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 2)
            return List.of("+<perm>", "setPrefix", "-<perm>");
        return new ArrayList<>();
    }

}
