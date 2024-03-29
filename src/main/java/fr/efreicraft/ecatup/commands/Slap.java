package fr.efreicraft.ecatup.commands;

import fr.efreicraft.ecatup.ECATUP;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

import static fr.efreicraft.ecatup.utils.Msg.colorize;

public class Slap implements CommandExecutor, TabExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0 || args.length > 2 || args[0].equals("-a")) {

            sender.sendMessage(Component.text(colorize("&c&lUsage: &r&c/slap <player> [-a]\n-a = Pousse le joueur dans la direction opposée par rapport à votre position.")));
            return true;
        }

        boolean sendAway = args.length == 2 && args[1].equals("-a");
        @NotNull List<Entity> players = Bukkit.selectEntities(sender, args[0]);

        if (players.isEmpty()) {
            sender.sendMessage(Component.text(colorize("&6"+args[0]+"&c n'est pas connecté !")));
            return true;
        }

        Vector direction;
        double force = 2.6;

        for (Entity p: players) {
            if (sendAway) {
                //noinspection ALL
                if (!(sender instanceof Player slapper)) {
                    sender.sendMessage(Component.text("Vous ne pouvez pas utiliser \"-a\" dans la console !").color(NamedTextColor.RED));
                    return true;
                }

                if (slapper.getUniqueId() == p.getUniqueId()) {
                    sender.sendMessage(Component.text(colorize("&4Wtf ??")));
                    continue;
                }

                direction = p.getLocation().toVector().subtract(slapper.getLocation().toVector()).normalize();
                direction.setY(direction.getY() * Math.random() * 2);

            } else {
                direction = Vector.getRandom();
                direction.setX(direction.getX() * ((Math.random() * (2 * force)) - force));
                direction.setY(direction.getY() * (Math.random() * force * 1.5));
                direction.setZ(direction.getZ() * ((Math.random() * (2 * force)) - force));
            }

            p.setVelocity(direction);
        }


        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            return ECATUP.getPlayersForTabList(args, 0);
        } else if (args.length == 2) {
            return Collections.singletonList("-a");
        }

        return null;
    }
}
