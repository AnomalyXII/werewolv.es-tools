package net.anomalyxii.werewolves.parser;

import net.anomalyxii.werewolves.domain.Game;

import java.util.List;

/**
 * Created by Anomaly on 05/01/2017.
 */
public interface GameParser<T> {

    // ******************************
    // Interface Methods
    // ******************************

    Game parse(List<T> events);

}
