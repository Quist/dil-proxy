package proxy.serializer;

import org.apache.camel.http.common.HttpMessage;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

public class ProxyRequestSerializer  {

    public String serialize(HttpMessage message) {
        HttpServletRequest request = message.getRequest();
        JSONObject header = new JSONObject();
        header.put("path", request.getRequestURL());
        header.put("method", request.getMethod());
        header.put("query", request.getQueryString());

        JSONObject httpHeaders = getHttpHeaders(request);
        header.put("headers", httpHeaders);

        return header.toString();
    }

    private JSONObject getHttpHeaders(HttpServletRequest request) {
        Enumeration<String> headerNames = request.getHeaderNames();
        JSONObject headers = new JSONObject();

        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            String headerValue = request.getHeader(headerName);
            headers.put(headerName, headerValue);
        }

        return headers;
    }
}
