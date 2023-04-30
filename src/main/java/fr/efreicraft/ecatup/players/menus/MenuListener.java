package fr.efreicraft.ecatup.players.menus;

import fr.efreicraft.ecatup.ECATUP;
import fr.efreicraft.ecatup.players.ECPlayer;
import fr.efreicraft.ecatup.utils.NBTUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.UUID;

public class MenuListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onInventoryClick(InventoryClickEvent event) {
        ECPlayer player = ECATUP.getInstance().getPlayerManager().getPlayer((org.bukkit.entity.Player) event.getWhoClicked());
        if(player != null && event.getCurrentItem() != null) {
            String nbtValue = NBTUtils.getNBT(event.getCurrentItem(), "menu_item_uuid");
            if(nbtValue != null) {
                event.setCancelled(true);
                player.getPlayerMenus().getMenuItemFromUUID(UUID.fromString(nbtValue)).getCallback().run(event);
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerInteract(PlayerInteractEvent event) {
        ECPlayer player = ECATUP.getInstance().getPlayerManager().getPlayer(event.getPlayer());
        if(player != null
                && event.getItem() != null
                && (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)) {
            String nbtValue = NBTUtils.getNBT(event.getItem(), "menu_item_uuid");
            if(nbtValue != null) {
                event.setCancelled(true);
                player.getPlayerMenus().getMenuItemFromUUID(UUID.fromString(nbtValue)).getCallback().run(event);
            }
        }
    }

}
