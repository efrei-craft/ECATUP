package fr.efreicraft.ecatup.commands;

import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.permission.Permission;
import fr.efreicraft.ecatup.players.ECPlayer;
import fr.efreicraft.ecatup.utils.MessageUtils;
import org.bukkit.entity.Player;

@Command(name = "hasperm")
@Permission("ecatup.hasperm")
public class HasPermCommand {
    @Execute
    void execute(@Context Player player, @Arg Player target, @Arg String permission) {
        ECPlayer ecPlayer = ECPlayer.get(target);

        if (target.hasPermission(permission)) {
            MessageUtils.sendMessage(
                    player,
                    MessageUtils.ChatPrefix.SERVER,
                    ecPlayer.getChatName() + " &apossède&7 la permission " + permission + "."
            );
        } else {
            MessageUtils.sendMessage(
                    player,
                    MessageUtils.ChatPrefix.SERVER,
                    ecPlayer.getChatName() + " &7ne &cpossède pas&7 la permission " + permission + "."
            );
        }
    }
}
