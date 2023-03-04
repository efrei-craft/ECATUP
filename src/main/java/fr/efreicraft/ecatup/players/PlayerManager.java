package fr.efreicraft.ecatup.players;

import fr.efreicraft.ecatup.ECATUP;
import fr.efreicraft.ecatup.players.events.ECPlayerJoined;

import java.util.HashSet;
import java.util.Set;

/**
 * Gestionnaire des joueurs.<br /><br />
 *
 * @author Antoine B. {@literal <antoine@jiveoff.fr>}
 */
public class PlayerManager {

    private final Set<Player> players;

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
    public void addPlayer(Player p) {
        this.players.add(p);

        ECPlayerJoined event = new ECPlayerJoined(p);
        ECATUP.getInstance().getServer().getPluginManager().callEvent(event);
    }

    /**
     * Supprime un joueur de la liste des joueurs. Déclenche également la vérification pour voir si le jeu doit être arrêté.
     * @param player Joueur à supprimer
     */
    public void removePlayer(Player player) {
        player.unload();
        this.players.remove(player);
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
    public Player getPlayer(org.bukkit.entity.Player player) {
        for (Player p : this.players) {
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
    public Set<Player> getPlayers() {
        return players;
    }

}
