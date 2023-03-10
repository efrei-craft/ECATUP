package fr.efreicraft.ecatup.commands.permissions;

import fr.efreicraft.animus.endpoints.PermGroupService;
import fr.efreicraft.animus.invoker.ApiException;
import fr.efreicraft.animus.models.PermGroup;
import fr.efreicraft.animus.models.PermGroupPlayer;
import fr.efreicraft.animus.models.PermissionInput;
import fr.efreicraft.ecatup.ECATUP;
import fr.efreicraft.ecatup.players.ECPlayer;
import fr.efreicraft.ecatup.utils.MessageUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static fr.efreicraft.ecatup.commands.permissions.Common.ErrorMessages.*;

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
                MessageUtils.sendMessage(sender, COULD_NOT_REACH_DATABASE.get());
            }
            return true;
        }

        PermGroup group = null;
        if (!("add".equalsIgnoreCase(args[0]) || "remove".equalsIgnoreCase(args[0]) || "groups".equalsIgnoreCase(args[0]))) {
            try {
                Optional<PermGroup> maybeGroup;
                maybeGroup = PermGroupService.getPermGroups().stream().filter(permGroup -> permGroup.getName().equalsIgnoreCase(args[0])).findFirst();
                if (maybeGroup.isEmpty()) {
                    MessageUtils.sendMessage(sender, GROUP_NOT_FOUND.format(args[0]));
                    return true;
                } else
                    group = maybeGroup.get();

            } catch (ApiException e) {
                MessageUtils.sendMessage(sender, COULD_NOT_REACH_DATABASE.get());
                return true;
            }
        }

        switch (args.length) {
            // Info groupes
            case 1 -> {
                if ("groups".equalsIgnoreCase(args[0]))
                    printGroups(sender);
            }
            // Créer/supprimer un groupe OU obtenir des infos sur un groupe
            // ...OU reset préfixe
            // OU ajouter/supprimer une perm
            case 2 -> {
                if ("add".equalsIgnoreCase(args[0])) {
                    try {
                        if (ECATUP.getInstance().getGroupManager().getGroup(args[1]) == null) {
                            PermGroupService.createPermGroup(args[1], "", "", false, false);
                            MessageUtils.sendMessage(sender, "&aLe groupe &e%s &aa bien été créé !".formatted(args[1]));
                        } else {
                            MessageUtils.sendMessage(sender, GROUP_ALREADY_EXISTS.format(args[1]));
                        }
                    } catch (ApiException e) {
                        MessageUtils.sendMessage(sender, COULD_NOT_REACH_DATABASE.get());
                    }
                } else if ("remove".equalsIgnoreCase(args[0])) {
                    removeGroup(args[1], sender);
                } else {
                    String permission = args[1].substring(1);
                    if (args[1].charAt(0) == '+') {

                    } else if (args[1].charAt(0) == '-') {

                    } else if (args[1].equalsIgnoreCase("setPrefix")) {
                        //TODO updatePermGroup()
                    } else {
                        MessageUtils.sendMessage(sender, BAD_ARGUMENT.format(args[1]));
                    }
                }
            }

            // Ajouter/Supprimer un joueur d'un groupe
            case 3 -> {
                if ("add".equalsIgnoreCase(args[1])) {

                } else if ("remove".equalsIgnoreCase(args[1])) {

                }
            }
        }
        
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return null;
    }

    public static void printGroups(CommandSender to) {
        List<PermGroup> permGroups;
        try {
            permGroups = PermGroupService.getPermGroups();
        } catch (ApiException e) {
            MessageUtils.sendMessage(to, COULD_NOT_REACH_DATABASE.get());
            return;
        }

        MessageUtils.sendMessage(to, MessageUtils.ChatPrefix.PLUGIN, "&3======= GROUPES &3=======");

        Component.join(JoinConfiguration.separators(Component.text("&8, "), Component.text(" &8et ")),
                permGroups.stream().map(permGroup -> Component.text("&a%s".formatted(permGroup.getName()))).toList()
        );
    }

    public static void removeGroup(String groupName, CommandSender sender) {
        //TODO réviser la logique de suppression de groupe

        if (sender == null) sender = Bukkit.getConsoleSender();

        // Toutes les autres perms qui ne sont pas dans le groupe à supprimer
        List<PermissionInput> allOtherPerms = new ArrayList<>();
        try {
            List<PermGroup> permGroupList = PermGroupService.getPermGroups();

            PermGroup groupToDelete = ECATUP.getInstance().getGroupManager().getGroup(groupName);
            if (groupToDelete == null) throw new NoSuchElementException();

            PermGroupService.deletePermGroup(groupToDelete.getId());

            for (PermGroup permGroup : permGroupList) {
                if (!permGroup.getName().equalsIgnoreCase(groupName)) {
                    allOtherPerms.addAll(permGroup.getPermissions());
                }
            }
        } catch (ApiException ignored) {
            MessageUtils.sendMessage(sender, "&cPas pu accéder à la base de données pour supprimer le groupe.");
            return;
        } catch (NoSuchElementException ignored) {
            MessageUtils.sendMessage(sender, "&cCe groupe n'existe pas dans la base de données !");
            return;
        }

        for (ECPlayer ecPlayer : ECATUP.getInstance().getPlayerManager().getPlayers()) {
            List<PermGroupPlayer> permGroups = ecPlayer.getAnimusPlayer().getPermGroups();
            permGroups.removeIf(grp -> grp.getName().equalsIgnoreCase(groupName));
            ecPlayer.getAnimusPlayer().setPermGroups(permGroups);
        }
    }
}
