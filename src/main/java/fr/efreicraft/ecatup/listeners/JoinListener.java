package fr.efreicraft.ecatup.listeners;

import fr.efreicraft.animus.invoker.ApiException;
import fr.efreicraft.ecatup.ECATUP;
import fr.efreicraft.ecatup.groups.GroupManager;
import fr.efreicraft.ecatup.players.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {

    @EventHandler
    public static void onJoin(PlayerJoinEvent event) {
        event.joinMessage(null);
        try {
            ECATUP.getInstance().getPlayerManager().addPlayer(new Player(event.getPlayer()));
        } catch (ApiException e) {
            event.getPlayer().kick();
        }
    }

}
