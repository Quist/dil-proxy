package processors;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.commons.httpclient.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerErrorHandler implements Processor {
    private final Logger logger = LoggerFactory.getLogger(ServerErrorHandler.class);

    @Override
    public void process(Exchange exchange) throws Exception {
        logger.error("Exception occurred. Returning 500.");
        exchange.getOut().setHeader(Exchange.HTTP_RESPONSE_CODE, HttpStatus.SC_INTERNAL_SERVER_ERROR);
    }
}
