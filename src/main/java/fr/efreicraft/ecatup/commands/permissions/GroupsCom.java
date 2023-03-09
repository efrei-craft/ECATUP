package fr.efreicraft.ecatup.commands.permissions;

import fr.efreicraft.animus.endpoints.PermGroupService;
import fr.efreicraft.animus.invoker.ApiException;
import fr.efreicraft.animus.models.PermGroup;
import fr.efreicraft.animus.models.PermGroupPlayer;
import fr.efreicraft.ecatup.ECATUP;
import fr.efreicraft.ecatup.players.ECPlayer;
import fr.efreicraft.ecatup.utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GroupsCom implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length < 1) {
            Common.sendGroupHelp(sender);
            return true;
        } else if (args[0].equalsIgnoreCase("groups")) {
            try {
                List<PermGroup> permGroups = PermGroupService.getPermGroups();

                MessageUtils.sendMessage(sender, MessageUtils.ChatPrefix.PLUGIN, "&3======= GROUPS &3=======");
                for (PermGroup group : permGroups) {
                    MessageUtils.sendMessage(sender, "  &8• &e&l%s &r&8(%s&r&8)".formatted(group.getName(), group.getPrefix()));
                }
            } catch (ApiException e) {
                MessageUtils.sendMessage(sender, "&cPas pu accéder à la base de données pour récupérer les groupes.");
            }
            return true;
        }

        PermGroup group;
        if (!("add".equalsIgnoreCase(args[0]) || "remove".equalsIgnoreCase(args[0]))) {
            try {
                Optional<PermGroup> maybeGroup;
                maybeGroup = PermGroupService.getPermGroups().stream().filter(permGroup -> permGroup.getName().equalsIgnoreCase(args[0])).findFirst();
                if (maybeGroup.isEmpty()) {
                    MessageUtils.sendMessage(sender, "&cCe groupe n'existe pas dans la base de données !");
                    return true;
                } else
                    group = maybeGroup.get();

            } catch (ApiException e) {
                MessageUtils.sendMessage(sender, "&cPas pu accéder à la base de données pour récupérer les groupes.");
                return true;
            }
        }
Double.MAX_VALUE
        switch (args.length) {
            case 2 -> {
                if (args[0].equalsIgnoreCase("add")) {
                    try {
                        PermGroupService.createPermGroup(args[1], "", "", false, false);
                        MessageUtils.sendMessage(sender, "&aLe groupe &e%s &aa bien été créé !".formatted(args[1]));
                    } catch (ApiException e) {
                        MessageUtils.sendMessage(sender, "&cPas pu accéder à la base de données pour créer le groupe.");
                    }
                } else if (args[0].equalsIgnoreCase("remove")) {
                    //TODO: Remove group when endpoint is created
                    //try {} catch(ApiException e) {}

                    for (ECPlayer ecPlayer : ECATUP.getInstance().getPlayerManager().getPlayers()) {
                        List<PermGroupPlayer> permGroups = ecPlayer.getAnimusPlayer().getPermGroups();

                        // Toutes les autres perms qui ne sont pas dans le groupe à supprimer
                        List<String> allOtherPerms = new ArrayList<>();

                    }
                } else {

                }
            }
        }
        
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return null;
    }
}
