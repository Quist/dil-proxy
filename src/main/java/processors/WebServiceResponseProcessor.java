package processors;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.eclipse.jetty.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebServiceResponseProcessor implements Processor {

    private final Logger logger = LoggerFactory.getLogger(WebServiceResponseProcessor.class);

    @Override
    public void process(Exchange exchange) throws Exception {
        int httpStatusCode = (int) exchange.getIn().getHeader(Exchange.HTTP_RESPONSE_CODE);
        logger.info("HTTP status: " + httpStatusCode + " - " + HttpStatus.getMessage(httpStatusCode) + ". Forwarding response to the other proxy.");

        if (exchange.getIn().getBody() == null) {
            exchange.getIn().setBody("");
        }
    }
}
