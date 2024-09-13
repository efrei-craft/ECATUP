package fr.efreicraft.ecatup.commands.handlers;

import dev.rollczi.litecommands.handler.result.ResultHandlerChain;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.permission.MissingPermissions;
import dev.rollczi.litecommands.permission.MissingPermissionsHandler;
import fr.efreicraft.ecatup.utils.MessageUtils;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;

public class PermissionHandler implements MissingPermissionsHandler<CommandSender> {

    private static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();

    @Override
    public void handle(Invocation<CommandSender> invocation, MissingPermissions missingPermissions, ResultHandlerChain<CommandSender> chain) {
        CommandSender sender = invocation.sender();

        MessageUtils.sendMessage(
                sender,
                MessageUtils.ChatPrefix.COMMAND,
                MINI_MESSAGE.deserialize("<red>Vous n'avez pas la permission d'utiliser cette commande.")
        );
    }
}
