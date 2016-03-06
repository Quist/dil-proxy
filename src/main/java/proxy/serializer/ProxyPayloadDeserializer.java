package proxy.serializer;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProxyPayloadDeserializer {
    private final static Logger logger = LoggerFactory.getLogger(ProxyPayloadDeserializer.class);

    public ProxyPayload deserialize(String payload) {
        logger.info("DeSerializing proxy payload. Body length: " + payload.length());
        JSONObject headers = extractHeaders(payload);
        String body = extractBody(payload);
        String query = getQuery(headers);

        ProxyPayload proxyPayload = new ProxyPayload.Builder(headers.getString("path"), headers.getString("method"))
                .body(body)
                .query(query)
                .build();

        logger.info("Proxy header. Path: " + proxyPayload.getPath() + ", Method: " + proxyPayload.getMethod());
        return proxyPayload;
    }

    private String getQuery(JSONObject headers) {
        if (headers.has("query")) {
            return headers.getString("query");
        } else {
            return null;
        }
    }

    private JSONObject extractHeaders(String payload) {
        if (payload.length() == 0) {
            return new JSONObject("{}");
        }

        int headerEndPosition = getHeaderEndPosition(payload);
        String header = payload.substring(0, headerEndPosition + 1);
        return new JSONObject(header);
    }

    private String extractBody(String payload) {
        return payload.substring(getHeaderEndPosition(payload) + 1 , payload.length());
    }

    private int getHeaderEndPosition(String payload) {
        for(int i = 0; i < payload.length(); i++) {
            if (payload.charAt(i) == '}') {
                return i;
            }
        }
        return payload.length();
    }
}
