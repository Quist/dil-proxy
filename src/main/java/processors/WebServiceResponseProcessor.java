package processors;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.eclipse.jetty.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import proxy.serializer.ProxyResponseSerializer;

public class WebServiceResponseProcessor implements Processor {

    private final Logger logger = LoggerFactory.getLogger(WebServiceResponseProcessor.class);
    private final ProxyResponseSerializer serializer;

    public WebServiceResponseProcessor(ProxyResponseSerializer serializer) {
        this.serializer = serializer;
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        int httpStatusCode = (int) exchange.getIn().getHeader(Exchange.HTTP_RESPONSE_CODE);
        logger.info("HTTP status: " + httpStatusCode + " - " + HttpStatus.getMessage(httpStatusCode) + ". Forwarding response to the other proxy.");

        exchange.getIn().setBody(serializer.serialize(exchange));
        exchange.getIn().setHeader("Content-Type", "application/json");
        exchange.getIn().setHeader(Exchange.HTTP_RESPONSE_CODE, 200);
    }
}
