package fr.efreicraft.ecatup.players.events;

import fr.efreicraft.ecatup.players.ECPlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ECPlayerJoined extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    private final ECPlayer player;

    public ECPlayerJoined(ECPlayer player) {
        this.player = player;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public ECPlayer getPlayer() {
        return this.player;
    }

}
