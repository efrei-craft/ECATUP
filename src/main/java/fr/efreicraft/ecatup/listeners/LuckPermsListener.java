package fr.efreicraft.ecatup.listeners;

import fr.efreicraft.ecatup.Main;
import net.kyori.adventure.text.Component;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.event.EventBus;
import net.luckperms.api.event.user.UserDataRecalculateEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.Objects;

import static fr.efreicraft.ecatup.utils.Msg.colorize;

public class LuckPermsListener implements Listener {

    public LuckPermsListener(Main plugin, LuckPerms LP) {
        EventBus eventBus = LP.getEventBus();
        eventBus.subscribe(plugin, UserDataRecalculateEvent.class, this::onUserDataRecalculate);
    }

    private void onUserDataRecalculate(UserDataRecalculateEvent event) {
        Player player = Bukkit.getPlayer(Objects.requireNonNull(event.getUser().getUsername()));
        if (player != null) {
            player.displayName(Component.text(colorize(event.getUser().getCachedData().getMetaData().getPrefix()) + player.getName()));
            player.playerListName(player.displayName());
        }
    }
}
