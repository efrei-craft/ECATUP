package fr.efreicraft.ecatup;

import fr.efreicraft.ecatup.commands.*;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class Main extends JavaPlugin {

    public static JavaPlugin INSTANCE;
    LuckPerms LP;

    @Override
    public void onEnable() {
        INSTANCE = this;
        LP = LuckPermsProvider.get();
        registerCom(new Lobby(), "lobby");

        // Commandes
        registerCommand("gm", new Gm());
        registerCommand("gms", new Gms());
        registerCommand("gma", new Gma());
        registerCommand("gmc", new Gmc());
        registerCommand("gmsp", new Gmsp());
    }

    @Override
    public void onDisable() {

    }

    void registerCom(CommandExecutor handler, String com) {
        Objects.requireNonNull(Bukkit.getPluginCommand(com)).setExecutor(handler);
    }
}
