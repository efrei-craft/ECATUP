package fr.efreicraft.ecatup.players.menus;

import fr.efreicraft.ecatup.players.ECPlayer;
import fr.efreicraft.ecatup.players.menus.interfaces.Menu;
import fr.efreicraft.ecatup.players.menus.interfaces.MenuItem;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * Classe pour l'instanciation d'un menu de type inventaire de joueur. <br />
 * <p> Contrairement au menu de type <b>chest</b>, aucun refresh automatique n'est effectué et un appel à la méthode
 * {@link #prepareMenuItems()} est nécessaire pour rafraichir l'inventaire.</p>
 * <p>Exemple de code pour refresh l'inventaire : <code>Core.get().getPlayerManager().getPlayer("JiveOff")
 * .getPlayerMenus().getMenu("LOBBY_ITEMS").prepareMenuItems()</code></p><br />
 *
 * <p>Si le besoin d'un refresh automatique est nécessaire, il est possible d'imiter le comportement du menu de type
 * <b>chest</b> mais il faudra essayer de ne pas avoir d'animation intempestive qu'on a quand on est give un item.</p>
 *
 * @author Antoine B. {@literal <antoine@jiveoff.fr>}
 * @project Ludos
 */
public class PlayerInventoryMenu extends Menu {

    /**
     * Constructeur du menu.
     * @param player Le joueur concerné par le menu.
     * @param items Les items à afficher dans le menu.
     */
    public PlayerInventoryMenu(ECPlayer player, List<MenuItem> items) {
        super(player, null, items);
    }

    @Override
    public void show() {
        this.prepareMenuItems();
    }

    @Override
    public void close() {
        // Fermer l'inventaire du joueur est impossible.
    }

    @Override
    protected void prepareMenuItems() {
        player.entity().getInventory().clear();

        for (MenuItem item : this.items) {
            ItemStackMenuItem menuItem = (ItemStackMenuItem) item;
            ItemStack itemStack = menuItem.getItemStack();

            player.entity().getInventory().setItem(menuItem.getSlot(), itemStack);
        }
    }
}
