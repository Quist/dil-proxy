package proxy.serializer;

import org.apache.camel.Exchange;
import org.apache.camel.http.common.HttpMessage;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

public class ProxyRequestSerializer  {

    public String serialize(Exchange exchange) {
        HttpServletRequest request = exchange.getIn(HttpMessage.class).getRequest();
        JSONObject proxyMessage = new JSONObject();
        proxyMessage.put("path", request.getRequestURL());
        proxyMessage.put("method", request.getMethod());
        proxyMessage.put("query", request.getQueryString());

        JSONObject httpHeaders = getHttpHeaders(request);
        proxyMessage.put("headers", httpHeaders);

        String body = exchange.getIn().getBody(String.class);
        if (body != null) {
            proxyMessage.put("body", body);
        }

        return proxyMessage.toString();
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
