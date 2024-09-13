package fr.efreicraft.ecatup.commands;

import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.join.Join;
import dev.rollczi.litecommands.annotations.permission.Permission;
import fr.efreicraft.ecatup.commands.validator.NotTheSamePlayer;
import org.bukkit.entity.Player;

@Command(name = "sudo")
@Permission("ecatup.sudo")
public class SudoCommand {

    @Execute(name = "chat")
    void executeChat(@Context Player player, @Arg @NotTheSamePlayer Player target, @Join String message) {
        target.chat(message);
    }

    @Execute(name = "command")
    void executeCommand(@Context Player player, @Arg @NotTheSamePlayer Player target, @Join String command) {
        target.performCommand(command);
    }
}
