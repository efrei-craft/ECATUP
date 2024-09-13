package fr.efreicraft.ecatup.commands;

import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.flag.Flag;
import dev.rollczi.litecommands.annotations.permission.Permission;
import fr.efreicraft.ecatup.commands.exceptions.CommandException;
import fr.efreicraft.ecatup.commands.validator.NotTheSamePlayer;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

@Command(name = "slap")
@Permission("ecatup.slap")
public class SlapCommand {

    @Execute
    void slap(@Context Player player, @Arg @NotTheSamePlayer Player target, @Flag("-a") boolean sendAway) throws CommandException {
        Vector direction;
        double force = 2.6;

        if (sendAway) {
            direction = target.getLocation().toVector().subtract(player.getLocation().toVector()).normalize();
            direction.setY(direction.getY() * Math.random() * 2);
        } else {
            direction = Vector.getRandom();
            direction.setX(direction.getX() * ((Math.random() * (2 * force)) - force));
            direction.setY(direction.getY() * (Math.random() * force * 1.5));
            direction.setZ(direction.getZ() * ((Math.random() * (2 * force)) - force));
        }

        target.setVelocity(direction);
    }
}
