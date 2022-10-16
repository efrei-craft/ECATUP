package fr.efreicraft.ecatup.commands;

import fr.efreicraft.ecatup.Main;
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
            Main.sendPlayerToServer(player, "lobby");
            return true;
        }
        else {
            sender.sendMessage(ChatColor.RED + "Vous devez être un joueur pour exécuter cette commande !");
            return false;
        }
    }
}