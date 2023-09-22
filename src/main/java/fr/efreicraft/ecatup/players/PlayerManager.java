package fr.efreicraft.ecatup.players;

import fr.efreicraft.animus.models.PermGroup;
import fr.efreicraft.animus.models.PermGroupPlayer;
import fr.efreicraft.ecatup.ECATUP;
import fr.efreicraft.ecatup.players.events.ECPlayerJoined;
import fr.efreicraft.ecatup.players.events.ECPlayerLeft;

import java.util.HashSet;
import java.util.Set;

/**
 * Gestionnaire des joueurs.<br /><br />
 *
 * @author Antoine B. {@literal <antoine@jiveoff.fr>}
 */
public class PlayerManager {

    private final Set<ECPlayer> players;

    /**
     * Constructeur du gestionnaire de joueurs. Il initialise la liste des joueurs aux joueurs connectés actuellement.
     */
    public PlayerManager() {
        if(ECATUP.getInstance().getPlayerManager() != null) {
            throw new IllegalStateException("PlayerManager already initialized !");
        }
        this.players = new HashSet<>();
    }

    /**
     * Ajoute un joueur à la liste des joueurs.
     * @param p Joueur à ajouter
     */
    public void addPlayer(ECPlayer p) {
        this.players.add(p);

        ECPlayerJoined event = new ECPlayerJoined(p);
        ECATUP.getInstance().getServer().getPluginManager().callEvent(event);
    }

    /**
     * Supprime un joueur de la liste des joueurs. Déclenche également la vérification pour voir si le jeu doit être arrêté.
     * @param player Joueur à supprimer
     */
    public void removePlayer(ECPlayer player) {
        player.unload();
        this.players.remove(player);

        ECPlayerLeft event = new ECPlayerLeft(player);
        ECATUP.getInstance().getServer().getPluginManager().callEvent(event);
    }

    /**
     * Supprime un joueur de la liste des joueurs.
     * @param player Joueur à supprimer
     */
    public void removePlayer(org.bukkit.entity.Player player) {
        this.removePlayer(this.getPlayer(player));
    }

    /**

     * Retourne un joueur à partir de son entité {@link org.bukkit.entity.Player}.
     * @param player Entité du joueur
     * @return Liste des joueurs
     */
    public ECPlayer getPlayer(org.bukkit.entity.Player player) {
        for (ECPlayer p : this.players) {
            if(p.entity().equals(player)) {
                return p;
            }
        }
        return null;
    }

    /**
     * Retourne tous les joueurs.
     * @return Liste des joueurs
     */
    public Set<ECPlayer> getPlayers() {
        return players;
    }

    public Set<ECPlayer> getPlayersInGroup(PermGroup group) {
        Set<ECPlayer> players = new HashSet<>();
        for (ECPlayer player : this.players) {
            for (PermGroupPlayer permGroup : player.getAnimusPlayer().getPermGroups()) {
                if(permGroup.getName().equals(group.getName())) {
                    players.add(player);
                }
            }
        }
        return players;
    }
}
