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
        return new ProxyPayload(headers.getString("path"), headers.getString("method"), body);
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
