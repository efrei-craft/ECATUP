package fr.efreicraft.ecatup.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Gms implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player player = null;
        if (args.length == 1) {
            if (sender instanceof Player) {
                player = (Player) sender;
            }
            else {
                sender.sendMessage(ChatColor.RED + "Vous devez être un joueur pour exécuter cette commande !");
                return false;
            }
        }
        else {
            player = Bukkit.getPlayer(args[1]);
            if (player == null) {
                sender.sendMessage(ChatColor.RED + "Le joueur " + args[1] + " n'est pas connecté !");
                return false;
            }
        }
        player.setGameMode(GameMode.SURVIVAL);
        player.sendMessage(ChatColor.GREEN + "Vous êtes maintenant en mode survie !");
        return true;
    }
}
