package net.anomalyxii.werewolves.router;

import org.apache.http.entity.ContentType;

import java.util.Collections;
import java.util.List;

/**
 * Created by Anomaly on 22/11/2016.
 */
public interface JsonSupport extends ContentTypeSupport {

    // ******************************
    // Default Methods
    // ******************************

    @Override
    default List<ContentType> getSupportedContentTypes() {
        return Collections.singletonList(ContentType.APPLICATION_JSON);
    }

    @Override
    default ContentType getPreferredContentType()  {
        return ContentType.APPLICATION_JSON;
    }

}
