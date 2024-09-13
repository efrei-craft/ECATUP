package fr.efreicraft.ecatup.commands.validator;

import dev.rollczi.litecommands.annotations.validator.requirment.AnnotatedValidator;
import dev.rollczi.litecommands.command.executor.CommandExecutor;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.requirement.Requirement;
import dev.rollczi.litecommands.validator.ValidatorResult;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class NotTheSamePlayerValidator implements AnnotatedValidator<CommandSender, Player, NotTheSamePlayer> {

    @Override
    public ValidatorResult validate(
            Invocation<CommandSender> invocation,
            CommandExecutor<CommandSender> executor,
            Requirement<Player> requirement,
            Player player,
            NotTheSamePlayer annotation
    ) {
        if (invocation.sender() == player) {
            return ValidatorResult.invalid("Vous ne pouvez pas vous cibler vous-même.");
        }

        return ValidatorResult.valid();
    }

}