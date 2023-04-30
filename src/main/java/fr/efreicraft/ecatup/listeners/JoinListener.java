package fr.efreicraft.ecatup.listeners;

import fr.efreicraft.animus.endpoints.PlayerService;
import fr.efreicraft.animus.invoker.ApiException;
import fr.efreicraft.animus.models.Player;
import fr.efreicraft.ecatup.ECATUP;
import fr.efreicraft.ecatup.players.ECPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {

    @EventHandler
    public static void onJoin(PlayerJoinEvent event) {
        event.joinMessage(null);
        try {
            final Player animusPlayer = PlayerService.getPlayer(event.getPlayer().getUniqueId().toString());

            ECPlayer ecPlayer = new ECPlayer(event.getPlayer(), animusPlayer);

            ECATUP.getInstance().getPlayerManager().addPlayer(ecPlayer);
        } catch (ApiException | RuntimeException e) {
            event.getPlayer().kick();
        }
    }

}
