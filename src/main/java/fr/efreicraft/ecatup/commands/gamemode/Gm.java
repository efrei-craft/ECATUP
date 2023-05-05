package fr.efreicraft.ecatup.commands.gamemode;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static fr.efreicraft.ecatup.utils.Msg.colorize;

public class Gm implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player player;
        if (args.length == 1) {
            if (sender instanceof Player) {
                player = (Player) sender;
            }
            else {
                sender.sendMessage(colorize("&cVous devez être un joueur pour exécuter cette commande !"));
                return true;
            }
        }
        else if (args.length == 2) {
            player = Bukkit.getPlayer(args[1]);
            if (player == null) {
                sender.sendMessage(colorize("&cLe joueur &l&6" + args[1] + "&r&c n'est pas connecté !"));
                return true;
            }
        }
        else {
            return false;
        }
        switch (args[0]) {
            case "0", "s", "survival" -> {
                player.setGameMode(GameMode.SURVIVAL);
                player.sendMessage(colorize("&aVous êtes maintenant en mode survie !"));
            }
            case "1", "c", "creative" -> {
                player.setGameMode(GameMode.CREATIVE);
                player.sendMessage(colorize("&aVous êtes maintenant en mode créatif !"));
            }
            case "2", "a", "adventure" -> {
                player.setGameMode(GameMode.ADVENTURE);
                player.sendMessage(colorize("&aVous êtes maintenant en mode aventure !"));
            }
            case "3", "sp", "spectator" -> {
                player.setGameMode(GameMode.SPECTATOR);
                player.sendMessage(colorize("&aVous êtes maintenant en mode spectateur !"));
            }
            default -> {
                sender.sendMessage(colorize("&cLe mode de jeu &l&6" + args[0] + "&r&c n'existe pas !"));
                return true;
            }
        }
        return true;
    }
}
