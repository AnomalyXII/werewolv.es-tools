package net.anomalyxii.werewolves.wwesbot.handlers;

import net.anomalyxii.bot.api.server.events.MessageEvent;

/**
 * Thrown by {@code AbstractCommandHandler#checkAuthorization}
 * if authorization fails for a given {@link MessageEvent}.
 * <p>
 * Created by Anomaly on 15/04/17.
 */
public class AuthorizationException extends Exception {

    // *********************************
    // Attributes
    // *********************************

    private final MessageEvent message;

    // *********************************
    // Constructors
    // *********************************

    public AuthorizationException(String s, MessageEvent message) {
        super(s);
        this.message = message;
    }

    public AuthorizationException(String s, Throwable throwable, MessageEvent message) {
        super(s, throwable);
        this.message = message;
    }

    public AuthorizationException(Throwable throwable, MessageEvent message) {
        super(throwable);
        this.message = message;
    }

    // *********************************
    // Getters & Setters
    // *********************************

    public MessageEvent getMessageEvent() {
        return message;
    }

}
