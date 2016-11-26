package net.anomalyxii.werewolves.router;

import org.apache.http.entity.ContentType;

import java.util.ArrayList;
import java.util.List;

/**
 * An interface that supports both JSON and XML
 * serialisation
 *
 * <i>This interface is effectively pointless,
 * I'm just playing around with default methods</i>
 */
public interface XmlOrJsonSupport extends JsonSupport, XmlSupport {

    // ******************************
    // Default Methods
    // ******************************

    @Override
    default List<ContentType> getSupportedContentTypes() {
        List<ContentType> contentTypes = new ArrayList<>();
        contentTypes.addAll(JsonSupport.super.getSupportedContentTypes());
        contentTypes.addAll(XmlSupport.super.getSupportedContentTypes());
        return contentTypes;
    }

    @Override
    default ContentType getPreferredContentType()  {
        return JsonSupport.super.getPreferredContentType();
    }

}
