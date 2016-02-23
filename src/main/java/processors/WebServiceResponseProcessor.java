package processors;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebServiceResponseProcessor implements Processor {

    private final Logger logger = LoggerFactory.getLogger(WebServiceResponseProcessor.class);

    @Override
    public void process(Exchange exchange) throws Exception {
        logger.info("Received response from Web service. Forwarding response to the proxy.");
    }
}
