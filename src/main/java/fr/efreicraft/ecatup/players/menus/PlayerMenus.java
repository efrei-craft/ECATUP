package fr.efreicraft.ecatup.players.menus;

import fr.efreicraft.ecatup.players.menus.interfaces.Menu;
import fr.efreicraft.ecatup.players.menus.interfaces.MenuItem;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Classe de gestion des menus d'un joueur.
 *
 * @author Antoine B. {@literal <antoine@jiveoff.fr>}
 * @project Ludos
 */
public class PlayerMenus {

    /**
     * La map des menus instanciés pour le joueur.<br />
     * Il peut y avoir plusieurs menus ouverts en même temps pour un joueur (par exemple, un inventory menu,
     * un chest menu, un chat menu etc..)
     */
    private final Map<String, Menu> menus = new HashMap<>();

    /**
     * Récupère un item d'un menu du joueur en fonction de son UUID.
     * @param uuid L'UUID de l'item.
     * @return L'item.
     */
    public MenuItem getMenuItemFromUUID(UUID uuid) {
        for (Menu menu : this.menus.values()) {
            if (menu.getItemsMap().containsKey(uuid)) {
                return menu.getItemsMap().get(uuid);
            }
        }
        return null;
    }

    /**
     * Met à jour un item d'un menu du joueur en fonction de son UUID. Si le menu n'existe pas, il est inséré.
     * @param name Le nom du menu pour le distinguer dans la map.
     * @param menu Le menu.
     * @return Le menu (pour pouvoir chaîner les méthodes).
     */
    public Menu setMenu(String name, Menu menu) {
        this.menus.put(name, menu);
        return menu;
    }

    /**
     * Récupère un menu du joueur en fonction de son nom.
     * @param name Le nom du menu.
     * @return Le menu.
     */
    public Menu getMenu(String name) {
        return this.menus.get(name);
    }

}
