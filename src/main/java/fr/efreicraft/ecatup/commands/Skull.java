package fr.efreicraft.ecatup.commands;

import fr.efreicraft.ecatup.utils.Messages;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public class Skull implements CommandExecutor {
    /**
     * Executes the given command, returning its success.
     * <br>
     * If false is returned, then the "usage" plugin.yml entry for this command
     * (if defined) will be sent to the player.
     *
     * @param sender  Source of the command
     * @param command Command which was executed
     * @param label   Alias of the command which was used
     * @param args    Passed command arguments
     * @return true if a valid command, otherwise false
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) return false;
        String name = sender.getName();

        if (args.length >= 1) name = args[0];

        // :trollmc:
        ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
        ItemMeta meta = skull.getItemMeta();
        meta.getPersistentDataContainer().set(new NamespacedKey("minecraft", "skullowner"), PersistentDataType.STRING, name);
        System.out.println(meta.);
        skull.setItemMeta(meta);

        player.getInventory().addItem(skull);
//        player.chat("/give @s player_head{SkullOwner:" + name + "}");
        return true;
    }
}
