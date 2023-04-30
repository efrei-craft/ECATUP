package fr.efreicraft.ecatup.players.menus.interfaces;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Item abstrait d'un menu.
 *
 * @author Antoine B. {@literal <antoine@jiveoff.fr>}
 * @project Ludos
 */
public abstract class MenuItem {

    /**
     * UUID du MenuItem pour une detection plus efficace d'une interaction avec.
     */
    private UUID uuid;

    /**
     * Le nom de l'item.
     */
    private TextComponent name = null;

    /**
     * La description de l'item. Un élément de la liste = une ligne.
     */
    private List<Component> description = null;

    /**
     * Lambda à appeler lors d'un clic sur l'item.
     */
    private IMenuClickCallback callback;

    /**
     * Lambda appelée pour rafraichir l'item.
     */
    private IMenuItemRefresh refresh;

    /**
     * Constructeur de base.
     * N'est pas tellement utilisé étant donné qu'une lambda de refresh peut spécifier les informations de l'item.
     *
     * @param name Le nom de l'item.
     * @param description La description de l'item.
     * @param callback La lambda à appeler lors d'un clic sur l'item.
     * @param refresh La lambda appelée pour rafraichir l'item.
     */
    protected MenuItem(
            String name,
            String description,
            IMenuClickCallback callback,
            IMenuItemRefresh refresh
    ) {
        this.uuid = UUID.randomUUID();
        this.name = LegacyComponentSerializer.legacyAmpersand().deserialize(name).decoration(TextDecoration.ITALIC, false);
        this.description = new ArrayList<>();
        for (String line : description.split("\n")) {
            this.description.add(LegacyComponentSerializer.legacyAmpersand().deserialize(line).decoration(TextDecoration.ITALIC, false));
        }
        this.callback = callback;
        this.refresh = refresh;
    }

    /**
     * Constructeur utilisé pour les items qui n'ont pas besoin de refresh ou de callback.
     * (Souvent utilisé pour les refresh)
     *
     * @param name Le nom de l'item.
     * @param description La description de l'item.
     */
    protected MenuItem(
            String name,
            String description
    ) {
        this(name, description, null, null);
    }

    /**
     * Constructeur utilisé pour les items qui donnent déjà leur information dans le refresh.
     *
     * @param callback La lambda à appeler lors d'un clic sur l'item.
     * @param refresh La lambda appelée pour rafraichir l'item.
     */
    protected MenuItem(
            IMenuClickCallback callback,
            IMenuItemRefresh refresh
    ) {
        this.uuid = UUID.randomUUID();
        this.callback = callback;
        this.refresh = refresh;
    }

    /**
     * Récupère l'UUID de l'item.
     * @return L'UUID de l'item.
     */
    public UUID getUUID() {
        return uuid;
    }

    /**
     * Récupère le nom de l'item. <b>Si une lambda de refresh est passée à la construction,
     * le nom d'item donné par le refresh est renvoyé.</b>
     * @return Le nom de l'item.
     */
    public TextComponent getName() {
        if (this.refresh != null) {
            return this.refresh.run().getName();
        }
        return name;
    }

    /**
     * Récupère la description de l'item. <b>Si une lambda de refresh est passée à la construction,
     * la description d'item donnée par le refresh est renvoyée.</b>
     * @return La description de l'item.
     */
    public List<Component> getDescription() {
        if (this.refresh != null) {
            return this.refresh.run().getDescription();
        }
        return description;
    }

    /**
     * Récupère la lambda à appeler lors d'un clic sur l'item.
     * @return La lambda à appeler lors d'un clic sur l'item.
     */
    public IMenuClickCallback getCallback() {
        return callback;
    }

    /**
     * Récupère la lambda appelée pour rafraichir l'item.
     * @return La lambda appelée pour rafraichir l'item.
     */
    public IMenuItemRefresh getRefresh() {
        return refresh;
    }
}
