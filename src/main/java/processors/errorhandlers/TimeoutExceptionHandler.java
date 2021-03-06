package processors.errorhandlers;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TimeoutExceptionHandler implements Processor {
    private final Logger logger = LoggerFactory.getLogger(TimeoutExceptionHandler.class);

    @Override
    public void process(Exchange exchange) throws Exception {
        logger.error("Timeout exception occurred. Returning 504.");
        exchange.getOut().setHeader(Exchange.HTTP_RESPONSE_CODE, 504);
    }
}
