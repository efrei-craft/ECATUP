package fr.efreicraft.ecatup.listeners;

import fr.efreicraft.ecatup.ECATUP;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class LeaveListener implements Listener {

    @EventHandler
    public static void onLeave(PlayerQuitEvent event) {
        event.quitMessage(null);
        ECATUP.getInstance().getPlayerManager().removePlayer(event.getPlayer());
    }

}
