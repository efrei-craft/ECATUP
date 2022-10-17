package fr.efreicraft.ecatup.commands;

import fr.efreicraft.ecatup.Main;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.group.Group;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.CompletableFuture;

import static fr.efreicraft.ecatup.utils.Msg.colorize;

public class WhoIs implements CommandExecutor {

    /**
     * ======  [NOM]  ======
     * UUID: [uuid]
     * Rangs: [rang1, rang2...]
     * Serveur (si connecté): serveur
     **/

    LuckPerms LP = Main.LP;

    final TextColor KEY_COLOR = NamedTextColor.DARK_AQUA;
    final TextColor VALUE_COLOR = NamedTextColor.WHITE;

    // Jsuis obligé
    List<String> LPcollection = new ArrayList<>();
    HoverEvent<Component> CLICK_TO_COPY = HoverEvent.hoverEvent(HoverEvent.Action.SHOW_TEXT, Component.text("Cliquez pour copier !"));

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        // ========== PRECISION : ON PEUT S'AUTO-WHOIS (because I said so) ==========
        if (args.length > 1) return false;

        OfflinePlayer player;
        if (args.length == 0) {
            if (sender instanceof Player)
                player = (Player) sender;  // whois sur soi-même
            else {
                sender.sendMessage(colorize("&cVous devez etre un joueur (ou preciser le nom d'un joueur) pour executer cette commande !"));
                return true;
            }
        } else {
            player = Bukkit.getOfflinePlayerIfCached(args[0]);
            if (player == null) {
                sender.sendMessage(colorize("&cLe joueur &l&6" + args[0] + "&r&c n'est pas connecte (ou ne s'est pas connecte recemment) !"));
                return true;
            }
        }

        Component message;

        /* Obtenir les rangs */
        getRanks(player).thenAcceptAsync(groups -> {
            for (Group grp : groups) {
                if (grp != null)
                    LPcollection.add(grp.getName());
            }
        });
        
        message = Component.join(JoinConfiguration.newlines(),
                        Component.join(JoinConfiguration.noSeparators(), Component.text("UUID: ", KEY_COLOR), Component.text(String.valueOf(player.getUniqueId()), VALUE_COLOR, TextDecoration.BOLD)
                                .clickEvent(ClickEvent.copyToClipboard(player.getUniqueId().toString())))
                                .hoverEvent(CLICK_TO_COPY),
                        Component.join(JoinConfiguration.noSeparators(), Component.text("Rangs: ", KEY_COLOR), Component.text(Arrays.toString(LPcollection.toArray()), VALUE_COLOR, TextDecoration.BOLD)
                                .clickEvent(ClickEvent.copyToClipboard(Arrays.toString(LPcollection.toArray()))))
                                .hoverEvent(CLICK_TO_COPY)
                        );

        sender.sendMessage(colorize("&8======  &6" + player.getName() + "&8  ======"));
        sender.sendMessage(message);
        sender.sendMessage(colorize("&8========" + new String(new char[player.getName().length()]).replace("\0", "=") + "========"));

        return true;
    }

    private CompletableFuture<Collection<Group>> getRanks(OfflinePlayer player) {
        UUID uuid = player.getUniqueId();

        return LP.getUserManager().loadUser(uuid).thenApplyAsync(
                user -> user.getInheritedGroups(user.getQueryOptions())
        );
    }
}
