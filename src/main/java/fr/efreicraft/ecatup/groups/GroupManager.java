package fr.efreicraft.ecatup.groups;

import fr.efreicraft.animus.endpoints.PermGroupService;
import fr.efreicraft.animus.invoker.ApiException;
import fr.efreicraft.animus.models.PermGroup;
import fr.efreicraft.animus.models.PermGroupPlayer;
import fr.efreicraft.ecatup.ECATUP;
import fr.efreicraft.ecatup.players.ECPlayer;
import fr.efreicraft.ecatup.utils.ColorUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.scoreboard.Team;

import javax.annotation.Nullable;
import java.util.List;

public class GroupManager {

    public GroupManager() {
        registerTeams();
    }

    public void registerTeams() {
        List<PermGroup> groups = getGroups();

        for(PermGroup group : groups) {
            Character priority = (char) ('z' - group.getPriority().intValue());
            String teamName = priority + group.getName();
            Team team = ECATUP.getInstance().getServer()
                    .getScoreboardManager()
                    .getMainScoreboard()
                    .registerNewTeam(teamName);

            team.prefix(Component.text()
                    .append(
                            LegacyComponentSerializer.legacyAmpersand().deserialize(
                                    group.getPrefix()
                            )
                    )
                    .build());

            team.color(ColorUtils.getTeamColorSetFromCode(group.getColor()).textColor());
            team.setAllowFriendlyFire(false);
        }

        for(ECPlayer player : ECATUP.getInstance().getPlayerManager().getPlayers()) {
            addPlayerToTeam(player);
        }
    }

    public void unregisterTeams() {
        List<PermGroup> groups = getGroups();

        for(PermGroup group : groups) {
            Character priority = (char) ('z' - group.getPriority().intValue());
            String teamName = priority + group.getName();
            Team team = ECATUP.getInstance().getServer()
                    .getScoreboardManager()
                    .getMainScoreboard()
                    .getTeam(teamName);

            team.unregister();
        }
    }

    public void addPlayerToTeam(ECPlayer player) {
        PermGroupPlayer group = player.getAnimusPlayer().getPermGroups().get(0);
        Character priority = (char) ('z' - group.getPriority().intValue());
        String teamName = priority + group.getName();
        Team team = ECATUP.getInstance().getServer()
                .getScoreboardManager()
                .getMainScoreboard()
                .getTeam(teamName);

        if(team != null) {
            team.addEntry(player.entity().getName());
        }
    }

    private List<PermGroup> getGroups() {
        try {
            return PermGroupService.getPermGroups();
        } catch (ApiException e) {
            throw new RuntimeException(e);
        }
    }

    @Nullable
    public PermGroup getGroup(String name) {
        try {
            return PermGroupService.getPermGroups().stream().filter(permGroup -> permGroup.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
        } catch (ApiException e) {
            Bukkit.getLogger().severe("Error while getting group by name: " + name);
            throw new RuntimeException(e);
        }
    }

    @Nullable
    public PermGroup getGroup(int id) {
        try {
            return PermGroupService.getPermGroups().stream().filter(permGroup -> permGroup.getId() == id).findFirst().orElse(null);
        } catch (ApiException e) {
            Bukkit.getLogger().severe("Error while getting group by id: " + id);
            throw new RuntimeException(e);
        }
    }

}
