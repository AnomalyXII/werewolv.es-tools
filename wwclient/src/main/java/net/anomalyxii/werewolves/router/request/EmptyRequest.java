package net.anomalyxii.werewolves.router.request;

import java.net.URI;

/**
 * Created by Anomaly on 23/11/2016.
 */
public class EmptyRequest extends AbstractRequest<Void> {

    // ******************************
    // Constructors
    // ******************************

    public EmptyRequest(URI host, String path) {
        super(host, path, null);
    }

}
