package fr.efreicraft.ecatup.commands;

import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.optional.OptionalArg;
import dev.rollczi.litecommands.annotations.permission.Permission;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.shortcut.Shortcut;
import fr.efreicraft.ecatup.ECATUP;
import fr.efreicraft.ecatup.players.ECPlayer;
import fr.efreicraft.ecatup.utils.MessageUtils;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

@Command(name = "gamemode", aliases = "gm")
@Permission("ecatup.gamemode")
public class GameModeCommand {

    @Execute
    public void execute(@Context Player player, @Arg GameMode gameMode) {
        player.setGameMode(gameMode);
        MessageUtils.sendMessage(player, MessageUtils.ChatPrefix.SERVER,"&7Votre mode de jeu a été mis à jour en &a" + gameMode.name().toLowerCase() + "&7.");
    }

    @Execute
    public void execute(@Context Player player, @Arg GameMode gameMode, @Arg Player target) {
        target.setGameMode(gameMode);

        ECPlayer ecPlayer = ECPlayer.get(target);
        MessageUtils.sendMessage(player, MessageUtils.ChatPrefix.SERVER,"&7Le mode de jeu de " + ecPlayer.getChatName() + "&7 a été mis à jour en &a" + gameMode.name().toLowerCase() + "&7.");
    }

    @Execute(name = "survival")
    @Shortcut("gms")
    public void setSurvival(@Context Player player, @OptionalArg Player target) {
        if (target != null) {
            execute(player, GameMode.SURVIVAL, target);
            return;
        }
        execute(player, GameMode.SURVIVAL);
    }

    @Execute(name = "creative")
    @Shortcut("gmc")
    public void setCreative(@Context Player player, @OptionalArg Player target) {
        if (target != null) {
            execute(player, GameMode.CREATIVE, target);
            return;
        }
        execute(player, GameMode.CREATIVE);
    }

    @Execute(name = "adventure")
    @Shortcut("gma")
    public void setAdventure(@Context Player player, @OptionalArg Player target) {
        if (target != null) {
            execute(player, GameMode.ADVENTURE, target);
            return;
        }
        execute(player, GameMode.ADVENTURE);
    }

    @Execute(name = "spectator")
    @Shortcut("gmsp")
    public void setSpectator(@Context Player player, @OptionalArg Player target) {
        if (target != null) {
            execute(player, GameMode.SPECTATOR, target);
            return;
        }
        execute(player, GameMode.SPECTATOR);
    }

}