package fr.efreicraft.ecatup.commands.validator;

import dev.rollczi.litecommands.annotations.validator.requirment.AnnotatedValidator;
import dev.rollczi.litecommands.command.executor.CommandExecutor;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.requirement.Requirement;
import dev.rollczi.litecommands.validator.ValidatorResult;
import org.bukkit.command.CommandSender;

public class SpeedValidator implements AnnotatedValidator<CommandSender, Float, Speed> {

    @Override
    public ValidatorResult validate(
            Invocation<CommandSender> invocation,
            CommandExecutor<CommandSender> executor,
            Requirement<Float> requirement,
            Float speed,
            Speed annotation
    ) {
        if (speed < -10 || speed > 10) {
            return ValidatorResult.invalid("Valeur invalide! La vitesse doit être comprise entre -10 et 10.");
        }

        return ValidatorResult.valid();
    }

}