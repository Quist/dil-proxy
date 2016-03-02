package proxy.serializer;

import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;

public class ProxyPayloadSerializer {

    public String serialize(HttpServletRequest request) {
        JSONObject header = new JSONObject();
        header.put("path", request.getRequestURL());
        header.put("method", request.getMethod());
        System.out.println(header.toString());
        return header.toString();
    }
}
