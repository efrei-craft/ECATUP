package fr.efreicraft.ecatup.players.menus;

import fr.efreicraft.ecatup.players.menus.interfaces.IMenuClickCallback;
import fr.efreicraft.ecatup.players.menus.interfaces.IMenuItemRefresh;
import fr.efreicraft.ecatup.players.menus.interfaces.MenuItem;
import fr.efreicraft.ecatup.utils.NBTUtils;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Classe représentant un item du menu de type {@link ItemStack}.<br />
 * C'est tout simplement un item représenté par un autre item de jeu Minecraft.
 *
 * @author Antoine B. {@literal <antoine@jiveoff.fr>}
 * @project Ludos
 */
public class ItemStackMenuItem extends MenuItem {

    /**
     * L'item de jeu Minecraft représentant l'item du menu.
     */
    private final ItemStack itemStack;

    /**
     * Slot où l'item doit être placé dans le menu.
     */
    private int slot;

    /**
     * Constructeur de base.
     * @param slot Le slot où l'item doit être placé dans le menu.
     * @param refresh La lambda permettant de refresh l'item.
     * @param callback La lambda permettant de définir une action à effectuer lors d'un clic sur l'item.
     */
    public ItemStackMenuItem(int slot, IMenuItemRefresh refresh, IMenuClickCallback callback) {
        super(callback, refresh);
        this.slot = slot;
        this.itemStack = ((ItemStackMenuItem) refresh.run()).getItemStack();
    }

    /**
     * Constructeur utilisé dans la lambda de refresh. Le slot n'a pas besoin d'être spécifié étant donné que
     * celui-ci n'a pas vocation à être modifié.
     * @param itemStack L'item de jeu Minecraft représentant l'item du menu.
     * @param name Le nom de l'item.
     * @param description La description de l'item.
     */
    public ItemStackMenuItem(ItemStack itemStack, String name, String description) {
        super(name, description);
        this.itemStack = itemStack;
    }

    /**
     * Constructeur pour un item n'ayant pas besoin d'être refresh (item statique).
     * @param itemStack L'item de jeu Minecraft représentant l'item du menu.
     * @param slot Le slot où l'item doit être placé dans le menu.
     * @param name Le nom de l'item.
     * @param description La description de l'item.
     * @param callback La lambda permettant de définir une action à effectuer lors d'un clic sur l'item.
     */
    public ItemStackMenuItem(
            ItemStack itemStack,
            int slot,
            String name,
            String description,
            IMenuClickCallback callback
    ) {
        super(name, description, callback, null);
        this.itemStack = itemStack;
        this.slot = slot;
    }

    /**
     * Récupère l'item de jeu Minecraft représentant l'item du menu. <b>Si une lambda de refresh est passée à la construction,
     * l'{@link ItemStack} donnée par le refresh est renvoyé.</b> <br /><br />
     * <i>A noter: l'item de jeu Minecraft est modifié pour y ajouter un tag NBT utilisant le PersistentDataContainer
     * permettant de rendre moins faillible la détection d'interaction avec l'item.</i>
     * @see fr.efreicraft.ecatup.utils.NBTUtils
     * @return L'item de jeu Minecraft représentant l'item du menu.
     */
    public ItemStack getItemStack() {
        if(this.getRefresh() != null) {
            ItemStack itemStack1 = ((ItemStackMenuItem) this.getRefresh().run()).getItemStack();
            NBTUtils.addNBT(itemStack1, "menu_item_uuid", this.getUUID().toString());
            return itemStack1;
        }
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.displayName(getName());
        itemMeta.lore(getDescription());
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        itemStack.setItemMeta(itemMeta);
        NBTUtils.addNBT(itemStack, "menu_item_uuid", this.getUUID().toString());
        return itemStack;
    }

    /**
     * Récupère le slot où l'item doit être placé dans le menu.
     * @return Le slot où l'item doit être placé dans le menu.
     */
    public int getSlot() {
        return slot;
    }

}
