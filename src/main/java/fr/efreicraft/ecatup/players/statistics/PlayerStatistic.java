package fr.efreicraft.ecatup.players.statistics;

import fr.efreicraft.animus.endpoints.PlayerService;
import fr.efreicraft.animus.invoker.ApiException;
import fr.efreicraft.animus.models.PlayerStatisticRecord;
import fr.efreicraft.ecatup.players.ECPlayer;
import fr.efreicraft.ecatup.utils.MessageUtils;

import java.math.BigDecimal;

public class PlayerStatistic {

    private Statistic statistic;

    private ECPlayer player;

    private BigDecimal value;

    public PlayerStatistic(ECPlayer player, PlayerStatisticRecord record) {
        this.player = player;
        this.statistic = Statistic.fromRecord(record);
        this.value = record.getValue();
    }

    public PlayerStatistic(ECPlayer player, Statistic statistic) {
        this.player = player;
        this.statistic = statistic;
        this.value = BigDecimal.ZERO;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void set(int value, String reason, Boolean sendMessage) {
        try {
            PlayerService.manipulatePlayerStatistic(
                    this.player.getAnimusPlayer().getUuid(),
                    this.statistic.getKey(),
                    BigDecimal.valueOf(value),
                    reason,
                    true,
                    this.statistic.getType(),
                    this.statistic.getDisplayName(),
                    this.statistic.getColor()
            );

            if(sendMessage) {
                this.player.sendMessage(MessageUtils.ChatPrefix.STATS, "&7Ton montant de " + this.statistic.getColor() + this.statistic.getDisplayName() + " &7a été mis à jour à " + this.statistic.getColor() + value + "&7.");
            }

            refreshStatistic();
        } catch (ApiException e) {
            e.printStackTrace();
        }
    }

    public void add(int value, String reason, Boolean sendMessage) {
        try {
            PlayerService.manipulatePlayerStatistic(
                    this.player.getAnimusPlayer().getUuid(),
                    this.statistic.getKey(),
                    BigDecimal.valueOf(value),
                    reason,
                    false,
                    this.statistic.getType(),
                    this.statistic.getDisplayName(),
                    this.statistic.getColor()
            );

            if(sendMessage) {
                StringBuilder sb = new StringBuilder(" ");

                if (value > 0)
                    sb.append("&a+");
                else
                    sb.append("&c-");

                sb.append(this.statistic.getColor()).append(Math.abs(value)).append(" ").append(this.statistic.getDisplayName())
                        .append(" &7(").append(reason).append(")");

                this.player.sendMessage(MessageUtils.ChatPrefix.EMPTY, sb.toString());
            }

            refreshStatistic();
        } catch (ApiException e) {
            e.printStackTrace();
        }
    }

    public void remove(int value, String reason, Boolean sendMessage) {
        this.add(-value, reason, sendMessage);
    }

    private void refreshStatistic() {
        try {
            PlayerStatisticRecord record = PlayerService.getPlayerStatistic(this.player.getAnimusPlayer().getUuid(), this.statistic.getKey());
            if(record != null) {
                this.statistic = Statistic.fromRecord(record);
                this.value = record.getValue();
            }
        } catch (ApiException e) {
            e.printStackTrace();
        }
    }
}
