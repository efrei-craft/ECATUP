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

        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
//        this.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", this);

        /* Commandes */
        registerCom(new Lobby(), "lobby");
        registerCom(new Skull(), "skull");
    }

    @Override
    public void onDisable() {
        this.getServer().getMessenger().unregisterOutgoingPluginChannel(this);
        this.getServer().getMessenger().unregisterIncomingPluginChannel(this);
    }

    void registerCom(CommandExecutor handler, String com) {
        Objects.requireNonNull(Bukkit.getPluginCommand(com)).setExecutor(handler);
    }
}
