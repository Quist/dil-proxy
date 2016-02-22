package processors;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.http.common.HttpMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;

public class ProxyPreprocessor implements Processor {
    private final Logger logger = LoggerFactory.getLogger(ProxyPreprocessor.class);

    @Override
    public void process(Exchange exchange) throws Exception {
        logger.info("Starting pre processing of exchange before sending to other proxy.");

        HttpServletRequest req = exchange.getIn(HttpMessage.class).getRequest();
        exchange.getIn().setHeader("path", req.getRequestURL());
        String headers = "{\"path\":\"" + req.getRequestURL() + "\"}";
        String body = exchange.getIn().getBody(String.class);
        body = headers + body;

        exchange.getIn().setBody(body);
        System.out.println("Setting body to: " + body);
    }
}
