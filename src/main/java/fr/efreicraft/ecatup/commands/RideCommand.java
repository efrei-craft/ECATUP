package fr.efreicraft.ecatup.commands;

import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.permission.Permission;
import fr.efreicraft.ecatup.commands.exceptions.CommandException;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

@Command(name = "ride")
@Permission("ecatup.ride")
public class RideCommand {

    @Execute
    void ride(@Context Player player, @Arg Player target) throws CommandException {
        target.addPassenger(player);
    }

    @Execute(name = "off")
    void off(@Context Player player) {
        Entity vehicle = player.getVehicle();

        if (vehicle != null) {
            vehicle.eject();
        }
    }
}
