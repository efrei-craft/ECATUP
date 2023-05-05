package fr.efreicraft.ecatup.utils;

import fr.efreicraft.ecatup.ECATUP;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

/**
 * Classe utilitaire pour la gestion des NBT à l'aide du {@link PersistentDataContainer} de Bukkit.
 *
 * @author mfnalex
 * @see <a href="https://minecraft.gamepedia.com/NBT_format">NBT format</a>
 * @see <a href="https://www.spigotmc.org/threads/what-are-nbt-tags-and-how-do-you-use-it.500603/">What are NBT tags and how do you use it?</a>
 */
public class NBTUtils {

    /**
     * Constructeur privé car la classe est un utilitaire statique.
     */
    private NBTUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Récupère la valeur attachée à une clé dans le {@link PersistentDataContainer} d'un {@link ItemStack}.
     * @param item Item auquel récupérer la valeur.
     * @param key Clé de la donnée.
     * @return Valeur de la donnée.
     */
    public static @Nullable String getNBT(@NotNull ItemStack item, String key) {
        if(!item.hasItemMeta()) return null;
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        NamespacedKey namespacedKey = new NamespacedKey(ECATUP.getInstance(), key);
        if(pdc.has(namespacedKey, PersistentDataType.STRING)) {
            return pdc.get(namespacedKey, PersistentDataType.STRING);
        }
        return null;
    }

    /**
     * Récupère la valeur attachée à une clé dans le {@link PersistentDataContainer} d'une {@link Entity}.
     * @param entity Entité à laquelle récupérer la valeur.
     * @param key Clé de la donnée.
     * @return Valeur de la donnée.
     */
    public static @Nullable String getNBT(@NotNull Entity entity, String key) {
        PersistentDataContainer pdc = entity.getPersistentDataContainer();
        NamespacedKey namespacedKey = new NamespacedKey(ECATUP.getInstance(), key);
        if(pdc.has(namespacedKey, PersistentDataType.STRING)) {
            return pdc.get(namespacedKey, PersistentDataType.STRING);
        }
        return null;
    }

    /**
     * Ajoute une donnée dans le {@link PersistentDataContainer} d'un {@link ItemStack}.
     * @param item Item auquel ajouter la donnée.
     * @param key Clé de la donnée.
     * @param value Valeur de la donnée.
     */
    public static void addNBT(@NotNull ItemStack item, String key, String value) {
        ItemMeta meta = item.hasItemMeta() ? item.getItemMeta() : Bukkit.getItemFactory().getItemMeta(item.getType());
        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        NamespacedKey namespacedKey = new NamespacedKey(ECATUP.getInstance(), key);
        pdc.set(namespacedKey, PersistentDataType.STRING, value);
        item.setItemMeta(meta);
    }

    /**
     * Ajoute une donnée dans le {@link PersistentDataContainer} d'une {@link Entity}.
     * @param entity Entité à laquelle ajouter la donnée.
     * @param key Clé de la donnée.
     * @param value Valeur de la donnée.
     */
    public static void addNBT(@NotNull Entity entity, String key, String value) {
        PersistentDataContainer pdc = entity.getPersistentDataContainer();
        NamespacedKey namespacedKey = new NamespacedKey(ECATUP.getInstance(), key);
        pdc.set(namespacedKey, PersistentDataType.STRING, value);
    }

    /**
     * Vérifie si une clé est présente dans le {@link PersistentDataContainer} d'un {@link ItemStack}.
     * @param item Item à vérifier.
     * @param key Clé à vérifier.
     * @return Vrai si la clé est présente, faux sinon.
     */
    public static boolean hasNBT(@NotNull ItemStack item, String key) {
        if(!item.hasItemMeta()) return false;
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        return pdc.has(new NamespacedKey(ECATUP.getInstance(), key), PersistentDataType.STRING);
    }

    /**
     * Vérifie si une clé est présente dans le {@link PersistentDataContainer} d'une {@link Entity}.
     * @param entity Entité à vérifier.
     * @param key Clé à vérifier.
     * @return Vrai si la clé est présente, faux sinon.
     */
    public static boolean hasNBT(@NotNull Entity entity, String key) {
        PersistentDataContainer pdc = entity.getPersistentDataContainer();
        return pdc.has(new NamespacedKey(ECATUP.getInstance(), key), PersistentDataType.STRING);
    }

    /**
     * Supprime une donnée du {@link PersistentDataContainer} d'un {@link ItemStack}.
     * @param item Item auquel supprimer la donnée.
     * @param key Clé de la donnée.
     */
    public static void removeNBT(@NotNull ItemStack item, String key) {
        if(!item.hasItemMeta()) return;
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        pdc.remove(new NamespacedKey(ECATUP.getInstance(), key));
        item.setItemMeta(meta);
    }

    /**
     * Supprime une donnée du {@link PersistentDataContainer} d'une {@link Entity}.
     * @param entity Entité à laquelle supprimer la donnée.
     * @param key Clé de la donnée.
     */
    public static void removeNBT(@NotNull Entity entity, String key) {
        PersistentDataContainer pdc = entity.getPersistentDataContainer();
        pdc.remove(new NamespacedKey(ECATUP.getInstance(), key));
    }

    /**
     * Récupère toutes les données du {@link PersistentDataContainer} d'un {@link ItemStack}.
     * @param item Item auquel récupérer les données.
     * @return Map représentant les données.
     */
    public static Map<String, String> getAllValues(@NotNull ItemStack item) {
        HashMap<String,String> map = new HashMap<>();
        if(!item.hasItemMeta()) return map;
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        for(NamespacedKey key : pdc.getKeys()) {
            map.put(key.toString(),pdc.get(key, PersistentDataType.STRING));
        }
        return map;
    }

}
