package fr.efreicraft.ecatup;

import dev.rollczi.litecommands.LiteCommands;
import dev.rollczi.litecommands.bukkit.LiteBukkitFactory;
import dev.rollczi.litecommands.schematic.SchematicFormat;
import fr.efreicraft.animus.invoker.ApiException;
import fr.efreicraft.ecatup.commands.*;
import fr.efreicraft.ecatup.commands.arguments.GameModeArgument;
import fr.efreicraft.ecatup.commands.exceptions.CommandException;
import fr.efreicraft.ecatup.commands.handlers.InvalidUsage;
import fr.efreicraft.ecatup.commands.handlers.PermissionHandler;
import fr.efreicraft.ecatup.commands.validator.NotTheSamePlayer;
import fr.efreicraft.ecatup.commands.validator.NotTheSamePlayerValidator;
import fr.efreicraft.ecatup.commands.validator.Speed;
import fr.efreicraft.ecatup.commands.validator.SpeedValidator;
import fr.efreicraft.ecatup.groups.GroupManager;
import fr.efreicraft.ecatup.listeners.ChatListener;
import fr.efreicraft.ecatup.listeners.JoinListener;
import fr.efreicraft.ecatup.listeners.LeaveListener;
import fr.efreicraft.ecatup.players.PlayerManager;
import fr.efreicraft.ecatup.players.menus.MenuListener;
import fr.efreicraft.ecatup.utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.awt.*;
import java.io.IOException;

@SuppressWarnings("UnstableApiUsage")
public final class ECATUP extends JavaPlugin {

    private static ECATUP INSTANCE;

    private FileConfiguration config;

    private PlayerManager playerManager;

    private GroupManager groupManager;

    private LiteCommands<CommandSender> liteCommands;

    @Override
    public void onEnable() {

        INSTANCE = this;

        // Load config
        saveDefaultConfig();
        config = INSTANCE.getConfig();
        config.options().copyDefaults(true); // au cas où le fichier existe, mais est incomplet.
        INSTANCE.saveConfig();

        // Register events
        Bukkit.getPluginManager().registerEvents(new ChatListener(), INSTANCE);
        Bukkit.getPluginManager().registerEvents(new JoinListener(), INSTANCE);
        Bukkit.getPluginManager().registerEvents(new LeaveListener(), INSTANCE);

        this.liteCommands = LiteBukkitFactory.builder()
                .settings(settings -> settings
                        .fallbackPrefix("ECATUP")
                        .nativePermissions(false)
                )

                .argument(GameMode.class, new GameModeArgument())

                .annotations(configuration -> configuration
                        .validator(Player.class, NotTheSamePlayer.class, new NotTheSamePlayerValidator())
                        .validator(Float.class, Speed.class, new SpeedValidator())
                )

                .commands(
                        new GameModeCommand(),
                        new SpeedCommand(),
                        new FlyCommand(),
                        new HasPermCommand(),
                        new SkullCommand(),
                        new SlapCommand(),
                        new RideCommand(),
                        new SudoCommand()
                )

                .exception(ApiException.class, (invocation, exception, chain) -> {
                    if (invocation.sender() instanceof Player) {
                        MessageUtils.sendMessage(
                                invocation.sender(),
                                MessageUtils.ChatPrefix.PLUGIN,
                                "&cErreur API: &7%s".formatted(exception.getMessage())
                        );
                    }
                })

                .exception(CommandException.class, (invocation, exception, chain) -> {
                    if (invocation.sender() instanceof Player) {
                        MessageUtils.sendMessage(
                                invocation.sender(),
                                MessageUtils.ChatPrefix.COMMAND,
                                "&cErreur: &7%s".formatted(exception.getMessage())
                        );
                    }
                })

                .missingPermission(new PermissionHandler())
                .invalidUsage(new InvalidUsage())

                .schematicGenerator(SchematicFormat.angleBrackets())

                .build();

        playerManager = new PlayerManager();
        groupManager = new GroupManager();

        Bukkit.getPluginManager().registerEvents(new MenuListener(), INSTANCE);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static ECATUP getInstance() {
        return INSTANCE;
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    public GroupManager getGroupManager() {
        return groupManager;
    }
}
