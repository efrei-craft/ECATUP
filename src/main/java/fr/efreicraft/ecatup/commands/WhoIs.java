package fr.efreicraft.ecatup.commands;

import fr.efreicraft.ecatup.Main;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.group.Group;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.UUID;
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
    Group[] ranks;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        // ========== PRECISION : ON PEUT S'AUTO-WHOIS (because I said so) ==========
        if (args.length > 1) return false;

        OfflinePlayer player;
        if (args.length == 0) {
            if (sender instanceof Player)
                player = (Player) sender;  // whois sur soi-même
            else {
                sender.sendMessage(colorize("&cVous devez être un joueur (ou préciser le nom d'un joueur) pour exécuter cette commande !"));
                return true;
            }
        } else {
            player = Bukkit.getOfflinePlayerIfCached(args[0]);
            if (player == null) {
                sender.sendMessage(colorize("&cLe joueur &l&6" + args[0] + "&r&c n'est pas connecté (ou ne s'est pas connecté récemment) !"));
                return true;
            }
        }

        Component message;
        getRanks(player).thenAcceptAsync(groups -> ranks = groups.toArray(new Group[0]));

        message = Component.join(JoinConfiguration.newlines(),
                        Component.join(JoinConfiguration.noSeparators(), Component.text("UUID: ", KEY_COLOR), Component.text(String.valueOf(player.getUniqueId()), VALUE_COLOR)
                                .clickEvent(ClickEvent.copyToClipboard(player.getUniqueId().toString()))),
                        Component.join(JoinConfiguration.noSeparators(), Component.text("Rangs: ", KEY_COLOR), Component.text(Arrays.toString(ranks), VALUE_COLOR)
                                .clickEvent(ClickEvent.copyToClipboard(Arrays.toString(ranks))))
                        );


        sender.sendMessage(colorize("&8======  &6" + sender.getName() + "&8  ======"));
        sender.sendMessage(message);
        sender.sendMessage(colorize("&8========" + new String(new char[sender.getName().length()]).replace("\0", "=") + "========"));

        return true;


    }

    private CompletableFuture<Collection<Group>> getRanks(OfflinePlayer player) {
        UUID uuid = player.getUniqueId();

        return LP.getUserManager().loadUser(uuid).thenApplyAsync(
                user -> user.getInheritedGroups(user.getQueryOptions())
        );
    }
}
