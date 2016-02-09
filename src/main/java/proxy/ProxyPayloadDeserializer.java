package proxy;

import org.json.JSONObject;

public class ProxyPayloadDeserializer {

    public ProxyPayload deserialize(String payload) {
        JSONObject headers = extractHeaders(payload);
        String body = extractBody(payload);
        return new ProxyPayload(headers.getString("path"), body);
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
