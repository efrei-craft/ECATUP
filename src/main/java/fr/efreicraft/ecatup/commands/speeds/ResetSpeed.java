package fr.efreicraft.ecatup.commands.speeds;

import fr.efreicraft.ecatup.utils.Msg;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ResetSpeed implements CommandExecutor, TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length > 1)
            return false;
        else if (args.length == 1) {
            try {
                List<Player> players = Bukkit.selectEntities(sender, args[0]).stream()
                        .filter(entity -> entity instanceof Player)
                        .map(entity -> (Player) entity)
                        .toList();
                players.forEach(player -> {
                    player.setFlySpeed(0.1f);
                    player.setWalkSpeed(0.2f);
                    player.sendMessage(Msg.colorize("&7Vos vitesses ont été réinitialisées par " + sender.getName()));
                });
                sender.sendMessage(Msg.colorize("&7Vitesses de %s réinitialisées.".formatted(args[0].replace("@a", "tout le monde")) ));
            } catch (IllegalArgumentException ignored) {
                sender.sendMessage(Msg.colorize("&cMauvais sélecteur &o" + args[0]));
            }
        } else {
            if (!(sender instanceof Player player)) {
                sender.sendMessage("Vous n'êtes pas un joueur !");
                return true;
            }
            player.setFlySpeed(0.1f);
            player.setWalkSpeed(0.2f);
            sender.sendMessage(Msg.colorize("&7Vitesses réinitialisées."));
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length != 1) return new ArrayList<>();

        return null;
    }
}
