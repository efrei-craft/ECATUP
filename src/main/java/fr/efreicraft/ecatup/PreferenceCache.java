package fr.efreicraft.ecatup;

import com.google.common.collect.Lists;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;


/**
 * ======= Important ! ======= <br>
 * Quand vous ajoutez une préférence, pensez à:
 * <li>ajouter un {@link PreferenceCache#DEFAULTS}</li>
 * <li>modifier la fonction {@link PreferenceCache#cache(Player, int)} comme dû</li>
 * <li>modifier la fonction {@link PreferenceCache#getPrefs(UUID)} comme dû</li>
 * <li>modifier le Javadoc de {@link PreferenceCache#getCache()} comme dû</li>
 * <li>modifier {@link PreferenceCache#NUMBER_OF_PREFS} comme dû</li>
 */
@SuppressWarnings("ALL") // ça ça s'appelle un fuck off.
public class PreferenceCache {

    private static Map<UUID, List<?>> cache;
    public static final List DEFAULTS = Lists.newArrayList(ChatChannel.SERVER);
    private static final int NUMBER_OF_PREFS = DEFAULTS.size();


    public static void cache(Player player, int channelID) {
        List entry = new ArrayList<>();

        // Channel
        ChatChannel channel;
        switch (channelID) {
            case 0 -> channel = ChatChannel.GLOBAL;
            case 1 -> channel = ChatChannel.SERVER;
            case 2 -> channel = ChatChannel.TEAM;
            default -> channel = (ChatChannel) DEFAULTS.get(0);
        }

        entry.add(channel);

        // Fin
        cache.put(player.getUniqueId(), entry);
    }

    public static void unCache(Player player) {
        cache.remove(player.getUniqueId());
    }

    /** Voici l'ordre des préférences utilisateur dans la List<?> du cache <br>
     * UUID -> <br>
     * <ol>
     * <li>ChatChannel</li>
     * </ol>
     */
    public static Map<UUID, List<?>> getCache() {
        return cache;
    }

    public static List<?> getPrefs(Player player) {
        return getPrefs(player.getUniqueId());
    }

    public static List<?> getPrefs(UUID player) {
        return cache.getOrDefault(player, DEFAULTS);
    }

    public static void setPref(Player player, List<?> prefs) {
        cache.put(player.getUniqueId(), prefs);
    }

    public static <T> void setPref(Player player, int index, T element) {
        List newEntry = cache.getOrDefault(player.getUniqueId(), DEFAULTS);
        newEntry.set(index, element);

        cache.put(player.getUniqueId(), newEntry);
    }

    public enum ChatChannel {
        GLOBAL(0),
        SERVER(1),
        TEAM(2);

        final int ID;
        ChatChannel(int i) {
            ID = i;
        }
    }
}
