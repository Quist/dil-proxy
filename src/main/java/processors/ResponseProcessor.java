package processors;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 * Generic response processor
 */
public class ResponseProcessor implements Processor {

    private final Logger logger = LoggerFactory.getLogger(ResponseProcessor.class);

    @Override
    public void process(Exchange exchange) throws Exception {
        logger.info("Received response.");
    }
}
