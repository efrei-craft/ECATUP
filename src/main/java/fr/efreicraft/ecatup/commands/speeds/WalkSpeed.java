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

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class WalkSpeed implements CommandExecutor, TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length >= 1) {
            Player p = Bukkit.getPlayer(args[0]);
            if (p != null) {
                sender.sendMessage(Msg.colorize("&7Vitesse de marche de " + p.getName() + " actuelle : &r&l" + (int) (p.getFlySpeed() * 10)));
                return true;
            }
        }
        if (!(sender instanceof Player self)) {
            sender.sendMessage("Vous n'êtes pas un joueur !");
            return true;
        }
        if (args.length < 1) {
            self.sendMessage(Msg.colorize("&7Vitesse de marche actuelle : &r&l" + (int) (self.getFlySpeed() * 10)));
        } else {
            if (args.length == 1) {

                float num;
                try {
                    if (args[0].equalsIgnoreCase("reset") || args[0].equalsIgnoreCase("r")) {
                        self.setWalkSpeed(0.2f);
                        self.sendMessage(Msg.colorize("&7Vitesse de marche réinitialisée."));
                        return true;
                    }
                    num = NumberFormat.getInstance().parse(args[0]).floatValue();
                } catch (ParseException ignored) {
                    sender.sendMessage(Msg.colorize("&c\"" + args[0] + "\" n'est pas un nombre, ni \"reset\" !"));
                    return true;
                }

                if (num > 10) {
                    sender.sendMessage(Msg.colorize("&cVotre vitesse doit être comprise entre -10 et 10 !\n &7&o→ Elle a été mise à &a&o10"));
                    self.setWalkSpeed(1);
                } else if (num < -10) {
                    sender.sendMessage(Msg.colorize("&cVotre vitesse doit être comprise entre -10 et 10 !\n &7&o→ Elle a été mise à &a&o-10"));
                    self.setWalkSpeed(-1);
                } else {
                    sender.sendMessage(Msg.colorize("&7Votre vitesse de marche a été réglée à &r" + num));
                    self.setWalkSpeed(num / 10);
                }
            } else {
                List<Player> players;
                try {
                    players = Bukkit.selectEntities(sender, args[1]).stream()
                            .filter(entity -> entity instanceof Player)
                            .map(entity -> (Player) entity)
                            .toList();
                } catch (IllegalArgumentException ignored) {
                    sender.sendMessage(Msg.colorize("&cMauvais sélecteur &o" + args[1]));
                    return true;
                }

                float num;
                try {
                    if (args[0].equalsIgnoreCase("reset") || args[0].equalsIgnoreCase("r")) {
                        players.forEach(player -> player.setWalkSpeed(0.2f));
                        sender.sendMessage(Msg.colorize("&7Vitesses de marche réinitialisées."));
                        return true;
                    }
                    num = NumberFormat.getInstance().parse(args[0]).floatValue();
                } catch (ParseException ignored) {
                    sender.sendMessage(Msg.colorize("&c\"" + args[0] + "\" n'est pas un nombre, ni \"reset\" !"));
                    return false;
                }

                if (num > 10) {
                    sender.sendMessage(Msg.colorize("&cLa vitesse doit être comprise entre -10 et 10 !\n &7&o→ Elle a été mise à &a&o10"));
                    players.forEach(player -> player.setWalkSpeed(1));
                } else if (num < -10) {
                    sender.sendMessage(Msg.colorize("&cLa vitesse doit être comprise entre -10 et 10 !\n &7&o→ Elle a été mise à &a&o-10"));
                    players.forEach(player -> player.setWalkSpeed(-1));
                } else {
                    sender.sendMessage(Msg.colorize("&7Vitesse de marche de %s réglée à %s".formatted(args[1].replace("@a", "tout le monde"), num) ));
                    players.forEach(player -> player.setWalkSpeed(num / 10));
                }
            }
        }

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) return List.of("reset");
        if (args.length != 2) return new ArrayList<>();

        return null;
    }
}
