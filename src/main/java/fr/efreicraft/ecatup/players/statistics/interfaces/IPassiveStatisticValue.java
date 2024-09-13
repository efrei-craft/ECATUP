package fr.efreicraft.ecatup.players.statistics.interfaces;

import fr.efreicraft.ecatup.players.ECPlayer;

/**
 * Interface fonctionnelle pour les lambda de récupération de valeur de statistiques passives.
 *
 * @author Antoine B. {@literal <antoine@jiveoff.fr>}
 * @project ECATUP
 */
public interface IPassiveStatisticValue {
    int value(ECPlayer player);
}