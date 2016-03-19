package processors;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.eclipse.jetty.http.HttpStatus;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Iterator;

public class ProxyResponsePostProcessor implements Processor {

    private final Logger logger = LoggerFactory.getLogger(ProxyResponsePostProcessor.class);


    @Override
    public void process(Exchange exchange) throws Exception {
        String payload = exchange.getIn().getBody(String.class);

        JSONObject proxyMessage = new JSONObject(payload);
        setHttpHeaders(exchange, proxyMessage.getJSONObject("headers"));
        exchange.getIn().setHeader(Exchange.HTTP_RESPONSE_CODE, proxyMessage.get("responsecode"));

        if (proxyMessage.has("body")) {
            exchange.getIn().setBody(proxyMessage.get("body"));
        }

        int httpStatusCode = (int) exchange.getIn().getHeader(Exchange.HTTP_RESPONSE_CODE);
        logger.info("Received response from other proxy. HTTP status: " + httpStatusCode + " - " + HttpStatus.getMessage(httpStatusCode));
    }

    private void setHttpHeaders(Exchange exchange, JSONObject headers) {
        Iterator<String> it = headers.keys();
        while (it.hasNext()) {
            String headerName = it.next();
            String headerValue = headers.getString(headerName);
            exchange.getIn().setHeader(headerName, headerValue);
        }
    }

}
