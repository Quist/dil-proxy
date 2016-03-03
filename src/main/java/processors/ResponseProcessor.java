package processors;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.eclipse.jetty.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 * Generic response processor
 */
public class ResponseProcessor implements Processor {

    private final Logger logger = LoggerFactory.getLogger(ResponseProcessor.class);

    @Override
    public void process(Exchange exchange) throws Exception {
        int httpStatusCode = (int) exchange.getIn().getHeader(Exchange.HTTP_RESPONSE_CODE);
        logger.info("Received response from other proxy. HTTP status: " + httpStatusCode + " - " + HttpStatus.getMessage(httpStatusCode));
    }
}
