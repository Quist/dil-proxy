package processors;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProxyResponseProcessor implements Processor{
    final Logger logger = LoggerFactory.getLogger(ProxyResponseProcessor.class);

    @Override
    public void process(Exchange exchange) throws Exception {
        logger.info("Received HTTP response from other proxy");
    }
}
