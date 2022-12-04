package fr.efreicraft.ecatup.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class Sudo implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length <= 1) return false;
        @NotNull List<Player> playerList = Bukkit.selectEntities(sender, args[0]).stream()
                .filter(entity -> entity instanceof Player)
                .map(entity -> (Player) entity)
                .toList();

        String message = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
        for (Player player : playerList) {
            if (!sender.equals(player)) // il faut éviter l'auto sudo comme /sudo <moi-même> /sudo <moi-même> /say hi!
                player.chat(message);
        }
        return true;
    }
}
