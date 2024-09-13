package fr.efreicraft.ecatup.commands;

import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.permission.Permission;
import fr.efreicraft.ecatup.ECATUP;
import fr.efreicraft.ecatup.players.ECPlayer;
import fr.efreicraft.ecatup.utils.MessageUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command(name = "fly", aliases = {"f"})
@Permission("ecatup.fly")
public class FlyCommand {

    @Execute
    void execute(@Context Player sender) {
        sender.setAllowFlight(!sender.getAllowFlight());
        MessageUtils.sendMessage(
                sender,
                MessageUtils.ChatPrefix.SERVER,
                "&7Vous " + (sender.getAllowFlight() ? "&apouvez désormais" : "&cne pouvez plus") + "&7 voler!"
        );
    }

    @Execute
    void executeOther(@Context CommandSender sender, @Arg Player target) {
        target.setAllowFlight(!target.getAllowFlight());

        ECPlayer ecPlayer = ECPlayer.get(target);

        MessageUtils.sendMessage(
                sender,
                MessageUtils.ChatPrefix.SERVER,
                "&7Le joueur " + ecPlayer.getChatName() + " " + (target.getAllowFlight() ? "&apossède désormais" : "&cne possède plus") + "&7 la capacité de voler!"
        );

        MessageUtils.sendMessage(
                target,
                MessageUtils.ChatPrefix.SERVER,
                "&7Vous " + (target.getAllowFlight() ? "&apouvez désormais" : "&cne pouvez plus") + "&7 voler!"
        );
    }

}
