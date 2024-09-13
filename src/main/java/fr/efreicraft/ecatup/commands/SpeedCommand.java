package fr.efreicraft.ecatup.commands;

import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.permission.Permission;
import dev.rollczi.litecommands.annotations.shortcut.Shortcut;
import fr.efreicraft.ecatup.commands.validator.Speed;
import fr.efreicraft.ecatup.utils.MessageUtils;
import org.bukkit.entity.Player;

@Command(name = "speed")
@Permission("ecatup.speed")
public class SpeedCommand {

    @Execute
    void execute(@Context Player player, @Arg @Speed Float speed) {
        player.setWalkSpeed(speed / 10);
        player.setFlySpeed(speed / 10);
        MessageUtils.sendMessage(
                player,
                MessageUtils.ChatPrefix.SERVER,
                "&7Votre vitesse de déplacement (marche & vol) a été mise à jour en &a" + speed + "&7."
        );
    }

    @Execute(name = "reset")
    @Shortcut("reset")
    void reset(@Context Player player) {
        player.setWalkSpeed(0.2f);
        player.setFlySpeed(0.1f);
        MessageUtils.sendMessage(
                player,
                MessageUtils.ChatPrefix.SERVER,
                "&7Votre vitesse de déplacement (marche & vol) a été réinitialisée."
        );
    }

    @Shortcut("ws")
    void setWalkSpeed(@Context Player player, @Arg @Speed Float speed) {
        player.setWalkSpeed(speed / 10);
        MessageUtils.sendMessage(
                player,
                MessageUtils.ChatPrefix.SERVER,
                "&7Votre vitesse de déplacement a été mise à jour en &a" + speed + "&7."
        );
    }

    @Shortcut("fs")
    void setFlySpeed(@Context Player player, @Arg @Speed Float speed) {
        player.setFlySpeed(speed / 10);
        MessageUtils.sendMessage(
                player,
                MessageUtils.ChatPrefix.SERVER,
                "&7Votre vitesse de vol a été mise à jour en &a" + speed + "&7."
        );
    }

}
