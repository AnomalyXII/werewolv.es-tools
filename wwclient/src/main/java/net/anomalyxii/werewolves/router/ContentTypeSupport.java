package net.anomalyxii.werewolves.router;

import org.apache.http.entity.ContentType;

import java.util.List;

/**
 * Created by Anomaly on 22/11/2016.
 */
public interface ContentTypeSupport {

    // ******************************
    // Interface Methods
    // ******************************

    List<ContentType> getSupportedContentTypes();

    ContentType getPreferredContentType();

}
