package fr.efreicraft.ecatup.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;

public class Skull implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 1) {
                ItemStack skull = new ItemStack(Material.PLAYER_HEAD, 1);
                SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
                skullMeta.setOwningPlayer(Bukkit.getOfflinePlayer(args[0]));
                skull.setItemMeta(skullMeta);
                player.getInventory().addItem(skull);
            }
            else {
                sender.sendMessage(ChatColor.RED + "Usage: /skull <player>");
            }
        }
        else {
            sender.sendMessage(ChatColor.RED + "Vous devez être un joueur pour exécuter cette commande !");
        }
        return true;
    }
}
