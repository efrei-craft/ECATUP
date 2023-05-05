package fr.efreicraft.ecatup.players.menus.interfaces;

import fr.efreicraft.ecatup.players.ECPlayer;
import net.kyori.adventure.text.Component;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Interface pour les menus.
 *
 * @author Antoine B. {@literal <antoine@jiveoff.fr>}
 * @project ECATUP
 */
public abstract class Menu {

    /**
     * Le joueur concerné par le menu.
     */
    protected ECPlayer player;

    /**
     * Le titre du menu.
     */
    protected Component menuName;

    /**
     * La liste d'items à afficher dans le menu.
     */
    protected List<MenuItem> items;

    protected Menu(ECPlayer player, Component menuName, List<MenuItem> items) {
        this.player = player;
        this.menuName = menuName;
        this.items = items;
    }

    /**
     * Affiche le menu.
     */
    public abstract void show();

    /**
     * Ferme le menu.
     */
    public abstract void close();

    /**
     * Prépare les items du menu.
     */
    protected abstract void prepareMenuItems();

    /**
     * Change le titre du menu.
     * @param menuName Le nouveau titre.
     */
    public void setMenuName(Component menuName) {
        this.menuName = menuName;
    }

    /**
     * Change les items du menu.
     * @param items Les nouveaux items.
     */
    public void setItems(List<MenuItem> items) {
        this.items = items;
        this.prepareMenuItems();
    }

    /**
     * Retourne les items du menu.
     * @return Les items du menu.
     */
    public List<MenuItem> getItems() {
        return items;
    }

    /**
     * Retourne une map des items du menu avec comme clé l'UUID de l'item.
     * @return Une map des items du menu.
     */
    public Map<UUID, MenuItem> getItemsMap() {
        return items.stream().collect(Collectors.toMap(MenuItem::getUUID, item -> item));
    }

}
