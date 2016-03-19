package proxy.serializer;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProxyRequestDeserializer {
    private final static Logger logger = LoggerFactory.getLogger(ProxyRequestDeserializer.class);

    public ProxyMessage deserialize(String payload) {
        logger.debug("Deserializing proxy payload. Body length: " + payload.length());
        JSONObject proxyJsonHeader = new JSONObject(payload);

        String httpPath = proxyJsonHeader.getString("path");
        String httpMethod = proxyJsonHeader.getString("method");
        JSONObject httpHeaders = proxyJsonHeader.getJSONObject("headers");
        ProxyMessage.Builder proxyMessageBuilder = new ProxyMessage.Builder(httpPath, httpMethod, httpHeaders);

        if (proxyJsonHeader.has("body")) {
            proxyMessageBuilder.body(proxyJsonHeader.getString("body"));
        }

        if (proxyJsonHeader.has("query")){
            proxyMessageBuilder.query(proxyJsonHeader.getString("query"));
        }

        ProxyMessage proxyMessage = proxyMessageBuilder.build();

        logger.debug("Proxy header. Path: " + proxyMessage.getPath() + ", Method: " + proxyMessage.getMethod());
        return proxyMessage;
    }

}
