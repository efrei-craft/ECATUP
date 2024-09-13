package fr.efreicraft.ecatup.listeners;

import fr.efreicraft.ecatup.ECATUP;
import fr.efreicraft.ecatup.players.ECPlayer;
import fr.efreicraft.ecatup.utils.MessageUtils;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ChatListener implements Listener {

    @EventHandler
    public void onChat(AsyncChatEvent event) {
        event.setCancelled(true);
        ECPlayer player = ECATUP.getInstance().getPlayerManager().getPlayer(event.getPlayer());
        MessageUtils.broadcastMessage(player.getChatName() + "&8: &f" + LegacyComponentSerializer.legacyAmpersand().serialize(event.message()));
    }
}
