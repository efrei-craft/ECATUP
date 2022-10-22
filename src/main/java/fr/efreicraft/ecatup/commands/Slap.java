package fr.efreicraft.ecatup.commands;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import static fr.efreicraft.ecatup.utils.Msg.colorize;

public class Slap implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0 || args.length > 2) {
            sender.sendMessage(Component.text(colorize("&cUsage: /slap <player> [-a]\n-a = Pousse le joueur dans la direction opposée par rapport à votre position.")));
            return true;
        }

        boolean sendAway = args[1].equals("-a");
        Player player = Bukkit.getPlayer(args[0]);

        if (player == null) {
            sender.sendMessage(Component.text(colorize("&6"+args[0]+"&c n'est pas connecté !")));
            return true;
        }

        Vector direction;
        if (sendAway) {
            if (!(sender instanceof Player slapper)) {
                sender.sendMessage(Component.text(colorize("&cVous ne pouvez pas utiliser \"-a\" dans la console !")));
                return true;
            }
            direction = player.getLocation().toVector().subtract(slapper.getLocation().toVector()).normalize();
            direction.setY(direction.getY() * Math.random() * 2);

        } else {
            direction = Vector.getRandom().multiply(2);
        }

        player.setVelocity(direction);

        return true;
    }

}
