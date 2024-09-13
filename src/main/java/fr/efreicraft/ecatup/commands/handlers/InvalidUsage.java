package fr.efreicraft.ecatup.commands.handlers;

import dev.rollczi.litecommands.handler.result.ResultHandlerChain;
import dev.rollczi.litecommands.invalidusage.InvalidUsageHandler;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.schematic.Schematic;
import fr.efreicraft.ecatup.utils.MessageUtils;
import org.bukkit.command.CommandSender;

public class InvalidUsage implements InvalidUsageHandler<CommandSender> {

    @Override
    public void handle(Invocation<CommandSender> invocation, dev.rollczi.litecommands.invalidusage.InvalidUsage<CommandSender> result, ResultHandlerChain<CommandSender> chain) {
        Schematic schematic = result.getSchematic();
        CommandSender sender = invocation.sender();

        if (schematic.isOnlyFirst()) {
            MessageUtils.sendMessage(sender, MessageUtils.ChatPrefix.COMMAND, "&cUsage incorrect. &7Syntaxe: " + schematic.first());
            return;
        }

        MessageUtils.sendMessage(sender, MessageUtils.ChatPrefix.COMMAND, "&cUsage incorrect. &7Sous-commandes: ");
        for (String sch : schematic.all()) {
            MessageUtils.sendMessage(sender, MessageUtils.ChatPrefix.EMPTY, " &7- &c" + sch);
        }
    }
}