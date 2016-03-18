package proxy.serializer;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProxyRequestDeserializer {
    private final static Logger logger = LoggerFactory.getLogger(ProxyRequestDeserializer.class);
    private final ProxyHeaderDeserializer headerDeserializer;

    public ProxyRequestDeserializer(ProxyHeaderDeserializer headerDeserializer) {
        this.headerDeserializer = headerDeserializer;
    }

    public ProxyMessage deserialize(String payload) {
        logger.debug("Deserializing proxy payload. Body length: " + payload.length());
        JSONObject headers = headerDeserializer.extractHeaders(payload);
        String body = extractBody(payload);
        String query = getQuery(headers);

        ProxyMessage proxyMessage = new ProxyMessage.Builder(headers.getString("path"), headers.getString("method"), headers.getJSONObject("headers"))
                .body(body)
                .query(query)
                .build();

        logger.debug("Proxy header. Path: " + proxyMessage.getPath() + ", Method: " + proxyMessage.getMethod());
        return proxyMessage;
    }

    private String getQuery(JSONObject headers) {
        if (headers.has("query")) {
            return headers.getString("query");
        } else {
            return null;
        }
    }

    private String extractBody(String payload) {
        return payload.substring(ProxyHeaderDeserializer.getHeaderEndPosition(payload) + 1 , payload.length());
    }

}
