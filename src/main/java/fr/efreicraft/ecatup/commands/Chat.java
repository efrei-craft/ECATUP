package fr.efreicraft.ecatup.commands;

import fr.efreicraft.animus.invoker.ApiException;
import fr.efreicraft.ecatup.PreferenceCache;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static fr.efreicraft.ecatup.utils.Msg.colorize;

@SuppressWarnings("ConstantConditions")
public class Chat implements CommandExecutor, TabExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player self)) {
            sender.sendMessage(colorize("&cVous devez être un joueur pour exécuter cette commande !"));
            return true;
        }

        PreferenceCache.ChatChannel channelActuel = (PreferenceCache.ChatChannel) PreferenceCache.getPrefs(self).get(0);
        if (args.length == 0) {
            sender.sendMessage(colorize("&bVotre canal actuel est: &r&l" + channelActuel));
            return true;
        }

        if (channelActuel.toString().equals(args[0].toUpperCase())) {
            sender.sendMessage(colorize("&cVous êtes déjà sur le canal &r&l" + channelActuel));
            return true;
        }

        boolean canUseGlobal = self.hasPermission("ecatup.channel.global");

        if (args[0].equalsIgnoreCase("help") || args.length > 1) {
            sender.sendMessage(colorize("&c&lUsage: &r&c/chat [" + (canUseGlobal ? "g|global OR " : "") + "s|server|normal OR t|team]"));
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "g","global" -> {
                if (canUseGlobal) {
                    sendChannelPrefToDB(self, PreferenceCache.ChatChannel.GLOBAL);
                    PreferenceCache.setPref(self, 0, PreferenceCache.ChatChannel.GLOBAL);
                    sender.sendMessage(colorize("&aVous basculez sur le canal &e[GLOBAL]"));
                } else sender.sendMessage(colorize("&c&lUsage: &r&c/chat [s|server|normal OR t|team]"));
            }
            case "s","server","normal" -> {
                sendChannelPrefToDB(self, PreferenceCache.ChatChannel.SERVER);
                PreferenceCache.setPref(self, 0, PreferenceCache.ChatChannel.SERVER);
                sender.sendMessage(colorize("&aVous basculez sur le canal &e[SERVER]"));
            }
            case "t","team" -> {
                sendChannelPrefToDB(self, PreferenceCache.ChatChannel.TEAM);
                PreferenceCache.setPref(self, 0, PreferenceCache.ChatChannel.TEAM);

                sender.sendMessage(colorize("&aVous basculez sur le canal &e[TEAM]"));
            }
            default -> sender.sendMessage(colorize("&c&lUsage: &r&c/chat [" + (canUseGlobal ? "g|global OR " : "") + "s|server|normal OR t|team]"));
        }

        return true;
    }

    public void sendChannelPrefToDB(Player player, PreferenceCache.ChatChannel channel) {
        try {
            //TODO bien sur supprimer ça quand la fonction pour update le channel sera prête
            throw new ApiException();
        } catch (ApiException e) {

        }
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length != 1) return Collections.emptyList();

        return Arrays.stream(PreferenceCache.ChatChannel.values())
                .filter(channel -> channel.toString().startsWith(args[0].toUpperCase()))
                .filter(channel -> sender.hasPermission("ecatup.channel.global") || channel != PreferenceCache.ChatChannel.GLOBAL).map(Enum::toString).collect(Collectors.toList());
    }
}
