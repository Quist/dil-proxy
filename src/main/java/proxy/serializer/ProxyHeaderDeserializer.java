package proxy.serializer;

import org.json.JSONObject;

public class ProxyHeaderDeserializer {

    public JSONObject extractHeaders(String payload) {
        if (payload.length() == 0) {
            return new JSONObject("{}");
        }

        int headerEndPosition = getHeaderEndPosition(payload);
        String header = payload.substring(0, headerEndPosition + 1);
        return new JSONObject(header);
    }

    public static int getHeaderEndPosition(String payload) {
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
