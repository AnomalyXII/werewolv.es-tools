package net.anomalyxii.werewolves.parser;

import net.anomalyxii.werewolves.domain.*;

import java.util.*;
import java.util.function.Supplier;

/**
 * Created by Anomaly on 05/01/2017.
 */
public abstract class AbstractGameParser implements GameParser<Map<String,Object>> {

    // ******************************
    // Members
    // ******************************

    private final Supplier<GameContext> factory;

    // ******************************
    // Constructors
    // ******************************

    public AbstractGameParser(Supplier<GameContext> factory) {
        this.factory = factory;
    }

    // ******************************
    // GameParser Methods
    // ******************************

    @Override
    public Game parse(List<Map<String, Object>> events) {
        GameContext context = factory.get();
        events.forEach(context::process);
        return context.build();
    }

}
