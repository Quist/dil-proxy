package processors.protocols;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class HttpPreprocessor implements Processor {
    private final Logger logger = LoggerFactory.getLogger(HttpPreprocessor.class);

    @Override
    public void process(Exchange exchange) throws Exception {
        logger.info("Pre processing http message before sending to other proxy.");
        exchange.getIn().setHeader(Exchange.HTTP_METHOD, "POST");
    }
}
