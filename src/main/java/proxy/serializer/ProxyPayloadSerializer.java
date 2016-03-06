package proxy.serializer;

import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;

public class ProxyPayloadSerializer {

    public String serialize(HttpServletRequest request) {
        JSONObject header = new JSONObject();
        header.put("path", request.getRequestURL());
        header.put("method", request.getMethod());
        header.put("query", request.getQueryString());
        return header.toString();
    }
}
