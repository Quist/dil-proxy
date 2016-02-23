package processors.protocols;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class HttpRequest implements Processor {
    private final Logger logger = LoggerFactory.getLogger(HttpRequest.class);

    @Override
    public void process(Exchange exchange) throws Exception {
        logger.info("Received an incoming HTTP request message from the other proxy.");
    }
}
