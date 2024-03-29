package fr.efreicraft.ecatup.commands.gamemode;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static fr.efreicraft.ecatup.utils.Msg.colorize;

public class Gma implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player player;
        if (args.length == 0) {
            if (sender instanceof Player) {
                player = (Player) sender;
            }
            else {
                sender.sendMessage(colorize("&cVous devez être un joueur pour exécuter cette commande !"));
                return true;
            }
        }
        else {
            player = Bukkit.getPlayer(args[0]);
            if (player == null) {
                sender.sendMessage(colorize("&cLe joueur &l&6" + args[0] + "&r&c n'est pas connecté !"));
                return true;
            }
        }
        player.setGameMode(GameMode.ADVENTURE);
        player.sendMessage(colorize("&aVous êtes maintenant en mode aventure !"));
        return true;
    }
}
