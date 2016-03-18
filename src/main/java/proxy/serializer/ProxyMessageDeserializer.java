package proxy.serializer;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProxyMessageDeserializer {
    private final static Logger logger = LoggerFactory.getLogger(ProxyMessageDeserializer.class);

    public ProxyMessage deserialize(String payload) {
        logger.debug("DeSerializing proxy payload. Body length: " + payload.length());
        JSONObject headers = extractHeaders(payload);
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
        int openBraces = 0;
        for(int i = 0; i < payload.length(); i++) {
            if (payload.charAt(i) == '}' && openBraces == 1) {
                return i;
            } else if(payload.charAt(i) == '{') {
                openBraces++;
            } else if (payload.charAt(i) == '}') {
                openBraces--;
            }
        }
        return payload.length();
    }
}
