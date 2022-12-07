package fr.efreicraft.ecatup.commands;

import fr.efreicraft.ecatup.utils.Msg;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Speed implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            return false;
        }
        if (args.length < 1) { //TODO, faire une expérience pour trouver le fly speed par déf
            player.setFlySpeed(0.2f);
            player.sendMessage(Msg.colorize("&7Vitesse réinitialisée."));
            return true;
        }

        return true;
    }
}
