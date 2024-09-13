package fr.efreicraft.ecatup.players.statistics;

import fr.efreicraft.animus.models.PlayerStatisticRecord;

public class Statistic {
    private String key;

    private String displayName;

    /**
     * Minecraft Color Code
     */
    private String color;

    private PlayerStatisticRecord.TypeEnum type;

    public Statistic(String key) {
        this.key = key;
    }

    public Statistic(String key, String displayName, String color, PlayerStatisticRecord.TypeEnum type) {
        this(key);
        this.displayName = displayName;
        this.color = color;
        this.type = type;
    }

    public String getKey() {
        return key;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getColor() {
        return color;
    }

    public PlayerStatisticRecord.TypeEnum getType() {
        return type;
    }

    public static Statistic fromRecord(PlayerStatisticRecord record) {
        return new Statistic(record.getKey(), record.getDisplayName(), record.getColor(), record.getType());
    }
}
