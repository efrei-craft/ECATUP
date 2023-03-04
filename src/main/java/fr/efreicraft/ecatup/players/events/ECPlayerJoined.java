package fr.efreicraft.ecatup.players.events;

import fr.efreicraft.ecatup.players.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ECPlayerJoined extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    private final Player player;

    public ECPlayerJoined(Player player) {
        this.player = player;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public Player getPlayer() {
        return this.player;
    }

}
