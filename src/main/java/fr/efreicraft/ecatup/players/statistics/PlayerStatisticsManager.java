package fr.efreicraft.ecatup.players.statistics;

import fr.efreicraft.animus.endpoints.PlayerService;
import fr.efreicraft.animus.invoker.ApiException;
import fr.efreicraft.ecatup.ECATUP;
import fr.efreicraft.ecatup.players.ECPlayer;
import fr.efreicraft.ecatup.players.statistics.interfaces.IPassiveStatisticValue;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;

public class PlayerStatisticsManager {

    private static final Integer PASSIVE_REFRESH_INTERVAL = 10;

    private ECPlayer player;

    private Map<String, PlayerStatistic> records = new HashMap<>();

    private Map<String, IPassiveStatisticValue> passiveValues = new HashMap<>();

    private BukkitTask passiveRefreshTask;

    public PlayerStatisticsManager(ECPlayer player) {
        this.player = player;

        this.initializeStatistics();
        this.startPassiveRefreshTask();
    }

    private void initializeStatistics() {
        try {
            this.records = PlayerService.getPlayerStatistics(this.player.getAnimusPlayer().getUuid())
                .stream()
                .collect(
                    HashMap::new,
                    (map, record) -> map.put(record.getKey(), new PlayerStatistic(this.player, record)),
                    HashMap::putAll
                );
        } catch (ApiException e) {
            e.printStackTrace();
        }
    }

    private void startPassiveRefreshTask() {
        this.passiveRefreshTask = Bukkit.getScheduler().runTaskTimerAsynchronously(
            ECATUP.getInstance(),
            () -> {
                this.passiveValues.forEach((key, value) -> {
                    this.get(key).set(value.value(this.player), "Passive refresh", false);
                });
            },
            0,
            20L * PASSIVE_REFRESH_INTERVAL
        );
    }

    public void registerPassiveValue(Statistic statistic, IPassiveStatisticValue value, Boolean now) {
        this.passiveValues.put(statistic.getKey(), value);

        if (now) {
            this.get(statistic).set(value.value(this.player), "Passive registration", false);
        }
    }

    public void triggerPassiveRefresh() {
        this.passiveValues.forEach((key, value) -> {
            this.get(key).set(value.value(this.player), "Forced passive refresh", false);
        });
    }

    public void unload() {
        this.passiveRefreshTask.cancel();
    }

    public PlayerStatistic get(Statistic statistic) {
        if (!this.records.containsKey(statistic.getKey())) {
            PlayerStatistic stat = new PlayerStatistic(this.player, statistic);
            this.records.put(statistic.getKey(), stat);
            return stat;
        } else {
            return this.records.get(statistic.getKey());
        }
    }

    private PlayerStatistic get(String key) {
        return this.get(new Statistic(key));
    }
}
