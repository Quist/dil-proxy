package processors;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.http.common.HttpMessage;

import javax.servlet.http.HttpServletRequest;

public class ProxyPreprocessor implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        HttpServletRequest req = exchange.getIn(HttpMessage.class).getRequest();
        String headers = "{\"path\":\"" + req.getRequestURL() + "\"}";
        String body = exchange.getIn().getBody(String.class);
        body = headers + body;

        exchange.getIn().setBody(body);
    }
}
