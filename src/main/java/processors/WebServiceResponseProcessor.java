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
        logger.info("Received response from Web service. HTTP status: " + httpStatusCode + " - " + HttpStatus.getMessage(httpStatusCode));
        logger.info(" Forwarding response to the other proxy.");
    }
}
