package fr.efreicraft.ecatup.players.menus.interfaces;

import org.bukkit.event.Event;

/**
 * Interface fonctionnelle permettant de définir une action à effectuer lors d'un clic sur un item d'un menu.
 *
 * @author Antoine B. {@literal <antoine@jiveoff.fr>}
 * @project ECATUP
 */
public interface IMenuClickCallback {

    void run(Event event);

}
