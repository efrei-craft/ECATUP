package fr.efreicraft.ecatup.players;

import fr.efreicraft.animus.endpoints.PlayerService;
import fr.efreicraft.animus.invoker.ApiException;
import fr.efreicraft.ecatup.players.menus.PlayerMenus;
import fr.efreicraft.ecatup.players.scoreboards.PlayerScoreboard;
import fr.efreicraft.ecatup.utils.MessageUtils;
import fr.efreicraft.ecatup.utils.SoundUtils;
import fr.efreicraft.ecatup.utils.TitleUtils;
import org.bukkit.GameMode;
import org.bukkit.Sound;

/**
 * Joueur des mini-jeux.
 *
 * @author Antoine B. {@literal <antoine@jiveoff.fr>}
 */
public class Player {

    /**
     * Instance du joueur Bukkit.
     */
    private final org.bukkit.entity.Player playerEntity;

    private final fr.efreicraft.animus.models.Player animusPlayer;

    /**
     * Instance du scoreboard du joueur.
     */
    private final PlayerScoreboard scoreboard;

    /**
     * Instance du menu du joueur.
     */
    private final PlayerMenus playerMenus;

    /**
     * Constructeur du joueur.
     * @param playerEntity Instance du joueur Bukkit.
     */
    public Player(org.bukkit.entity.Player playerEntity) throws ApiException {
        this.playerEntity = playerEntity;
        this.playerMenus = new PlayerMenus();
        this.scoreboard = new PlayerScoreboard(this);
        this.animusPlayer = PlayerService.getPlayer(String.valueOf(playerEntity.getUniqueId()));
    }

    /**
     * S'occupe de désinstancer les dépendances pour la destruction du joueur.
     */
    public void unload() {
        this.scoreboard.unload();
    }

    /**
     * Renvoie l'instance entité du joueur Bukkit.
     *
     * @return Instance entité du joueur Bukkit.
     */
    public org.bukkit.entity.Player entity() {
        return playerEntity;
    }

    /**
     * Retourne le {@link PlayerScoreboard} du joueur.
     * @return Le {@link PlayerScoreboard} du joueur.
     */
    public PlayerScoreboard getBoard() {
        return scoreboard;
    }

    /**
     * Retourne l'instance du Player Animus.
     * @return Instance du Player Animus.
     */
    public fr.efreicraft.animus.models.Player getAnimusPlayer() {
        return animusPlayer;
    }

    /**
     * Retourne le {@link PlayerMenus} du joueur.
     * @return Le {@link PlayerMenus} du joueur.
     */
    public PlayerMenus getPlayerMenus() {
        return playerMenus;
    }

    /**
     * Envoie un message au joueur.
     *
     * @param prefix  Préfixe du message.
     * @param message Message à envoyer.
     */
    public void sendMessage(MessageUtils.ChatPrefix prefix, String message) {
        MessageUtils.sendMessage(this.playerEntity, prefix, message);
    }

    /**
     * Envoie un title au joueur.
     * @param title     Titre du title.
     * @param subtitle  Sous-titre du title.
     * @param fadeIn    Temps d'apparition du title en secondes.
     * @param stay      Temps d'affichage du title en secondes.
     * @param fadeOut   Temps de disparition du title en secondes.
     */
    public void sendTitle(String title, String subtitle, float fadeIn, float stay, float fadeOut) {
        TitleUtils.sendTitle(this, title, subtitle, fadeIn, stay, fadeOut);
    }

    /**
     * Joue un son au joueur.
     * @param sound     Son à jouer.
     * @param volume    Volume du son.
     * @param pitch     Pitch du son.
     */
    public void playSound(Sound sound, float volume, float pitch) {
        SoundUtils.playSound(this, sound, volume, pitch);
    }

    public String toString() {
        return this.playerEntity.getName();
    }

    /**
     * Retourne le nom du joueur, avec la couleur de son équipe s'il en a une.
     * @return Nom du joueur
     */
    public String getName() {
        return this.playerEntity.getName();
    }

    /**
     * Remets le joueur à un état de jeu normal.
     */
    public void resetPlayer() {
        entity().setGameMode(GameMode.ADVENTURE);
        entity().setHealth(20);
        entity().setFoodLevel(20);
        entity().setSaturation(20);
        entity().setExhaustion(0);
        entity().setFireTicks(0);
        entity().setFallDistance(0);
        entity().setExp(0);
        entity().setLevel(0);
        entity().setAllowFlight(false);
        entity().setFlying(false);
        entity().setWalkSpeed(0.2f);
        entity().setFlySpeed(0.1f);
        entity().getInventory().clear();
        entity().getInventory().setArmorContents(null);
    }
}
