package fr.efreicraft.ecatup.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static fr.efreicraft.ecatup.utils.Msg.colorize;

public class WhoIs implements CommandExecutor {

    /**
     * ======  [NOM]  ======
     * UUID: [uuid]
     * Rangs: [rang1, rang2...]
     * Serveur (si connecté): serveur
     **/

    final TextColor KEY_COLOR = NamedTextColor.AQUA;
    final TextColor VALUE_COLOR = NamedTextColor.WHITE;


    // Jsuis obligé
    final HoverEvent<Component> CLICK_TO_COPY = HoverEvent.hoverEvent(HoverEvent.Action.SHOW_TEXT, Component.text("Cliquez pour copier !"));

    @SuppressWarnings("ConstantConditions")
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
        //TODO finir ça
        String highestRank = "hm";

        message = Component.join(JoinConfiguration.newlines(),
                        Component.join(JoinConfiguration.noSeparators(), Component.text("UUID: ", KEY_COLOR), Component.text(String.valueOf(player.getUniqueId()), VALUE_COLOR, TextDecoration.BOLD)
                                .clickEvent(ClickEvent.copyToClipboard(player.getUniqueId().toString())))
                                .hoverEvent(CLICK_TO_COPY),
                        Component.join(JoinConfiguration.noSeparators(), Component.text("Rang: ", KEY_COLOR), Component.text(highestRank == null ? "rien" : colorize(highestRank), VALUE_COLOR, TextDecoration.BOLD)
                                .clickEvent(ClickEvent.copyToClipboard(highestRank == null ? "rien" : colorize(highestRank))))
                                .hoverEvent(CLICK_TO_COPY),
                        Component.join(JoinConfiguration.noSeparators(), Component.text("Pos: ", KEY_COLOR), Component.text(player.isOnline() ? ((Player) player).getLocation().toString() : "couldn't get pos...", VALUE_COLOR, TextDecoration.BOLD)
                                .clickEvent(ClickEvent.copyToClipboard(player.isOnline() ? ((Player) player).getLocation().toString() : "couldn't get pos...")))
                                .hoverEvent(CLICK_TO_COPY)
                        );

        sender.sendMessage(colorize((player.isOnline() ? "&8======  &a" : "&8======  &4") + player.getName() + "&8  ======"));
        sender.sendMessage(message);
        sender.sendMessage(colorize("&8========" + new String(new char[player.getName().length()]).replace("\0", "=") + "========"));

        return true;
    }

}
