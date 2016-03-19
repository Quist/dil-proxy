package proxy.serializer;

import org.apache.camel.Exchange;
import org.json.JSONObject;

import java.util.Collection;

public class ProxyResponseSerializer {

    public String serialize(Exchange exchange) {

        JSONObject proxyMessage = new JSONObject();

        Collection<String> headerNames = exchange.getIn().getHeaders().keySet() ;
        JSONObject headers = new JSONObject();

        headerNames.stream().filter(headerName -> !headerName.startsWith("Camel")).forEach(headerName -> {
            String headerValue = exchange.getIn().getHeader(headerName).toString();
            headers.put(headerName, headerValue);
        });

        proxyMessage.put("headers" , headers);
        proxyMessage.put("responsecode", exchange.getIn().getHeader(Exchange.HTTP_RESPONSE_CODE));

        String body = exchange.getIn().getBody(String.class);

        if (body != null) {
            proxyMessage.put("body", body);
        }

        return proxyMessage.toString();
    }

}
