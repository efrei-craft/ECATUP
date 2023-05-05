package fr.efreicraft.ecatup.players.scoreboards;

import fr.efreicraft.ecatup.players.ECPlayer;
import fr.efreicraft.ecatup.players.scoreboards.interfaces.IDynamicScoreboardFieldValue;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

/**
 * Représente un field de Scoreboard.<br />
 * Celui-ci peut être sois dynamique (avec une lambda de type {@link IDynamicScoreboardFieldValue})
 * ou statique (avec une chaîne de caractères).
 *
 * @author Antoine B. {@literal <antoine@jiveoff.fr>}
 * @project EFREI-Minigames
 */
public class ScoreboardField {

    /**
     * Composant de texte du nom du field. Celui-ci est affiché au scoreboard.
     */
    private TextComponent name;

    /**
     * Composant de texte de la valeur du field. Celui-ci est affiché au scoreboard.
     */
    private TextComponent value;
    private IDynamicScoreboardFieldValue dynamicValue;

    private final boolean oneLine;

    private ECPlayer player; // Only used for dynamic values

    /**
     * Constructeur d'un field de scoreboard statique.
     * @param name Nom du field.
     * @param value Valeur du field.
     * @param oneLine Si le field doit être affiché sur une seule ligne.
     */
    public ScoreboardField(String name, String value, boolean oneLine) {
        this.name = LegacyComponentSerializer.legacyAmpersand().deserialize(name);
        if (value != null) {
            this.value = LegacyComponentSerializer.legacyAmpersand().deserialize(value);
        }
        this.oneLine = oneLine;
    }

    /**
     * Constructeur d'un field de scoreboard statique.
     * @param name Nom du field.
     * @param value Valeur du field.
     */
    public ScoreboardField(String name, String value) {
        this(name, value, false);
    }

    /**
     * Constructeur d'un field de scoreboard dynamique.
     * @param name         Nom du field.
     * @param dynamicValue Lambda de type {@link IDynamicScoreboardFieldValue} qui retourne la valeur du field.
     */
    public ScoreboardField(String name, boolean oneLine, IDynamicScoreboardFieldValue dynamicValue) {
        this(name, null, oneLine);
        this.dynamicValue = dynamicValue;
    }

    /**
     * Constructeur d'un field de scoreboard dynamique.
     * @param name Nom du field.
     * @param dynamicValue Lambda de type {@link IDynamicScoreboardFieldValue} qui retourne la valeur du field.
     */
    public ScoreboardField(String name, IDynamicScoreboardFieldValue dynamicValue) {
        this(name, false, dynamicValue);
    }

    /**
     * Retourne le nom du field.
     * @return Nom du field.
     */
    protected TextComponent name() {
        return name;
    }

    /**
     * Retourne la valeur du field.
     * @return Valeur du field.
     */
    protected TextComponent value() {
        if(this.value == null) {
            return LegacyComponentSerializer.legacyAmpersand().deserialize(this.dynamicValue.value(player));
        }
        return value;
    }

    /**
     * Change le nom du field.
     * @param name Nouveau nom du field.
     */
    protected void name(TextComponent name) {
        this.name = name;
    }

    /**
     * Change la valeur du field.
     * @param value Nouvelle valeur du field.
     */
    protected void value(TextComponent value) {
        this.value = value;
    }

    /**
     * Change le joueur du field.
     * @param player Nouveau joueur du field.
     */
    public void setPlayer(ECPlayer player) {
        this.player = player;
    }

    /**
     * Retourne si le field doit être affiché sur une seule ligne.
     * @return Si le field doit être affiché sur une seule ligne.
     */
    protected boolean isOneLine() {
        return oneLine;
    }
}
