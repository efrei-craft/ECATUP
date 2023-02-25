package fr.efreicraft.ecatup.commands;

import fr.efreicraft.ecatup.ECATUP;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Lobby implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (args.length > 0) return false;

        if (sender instanceof Player player) {
            ECATUP.sendPlayerToServer(player, "lobby");
        }
        else {
            sender.sendMessage(ChatColor.RED + "Vous devez être un joueur pour exécuter cette commande !");
        }
        return true;
    }
}