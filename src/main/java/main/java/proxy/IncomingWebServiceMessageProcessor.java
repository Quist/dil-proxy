package main.java.proxy;

import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IncomingWebServiceMessageProcessor implements org.apache.camel.Processor {
    final Logger logger = LoggerFactory.getLogger(Proxy.class);

    @Override
    public void process(Exchange exchange) throws Exception {
        logger.info("Received incoming message from Web Service: ", exchange);
        logger.warn(new M);
    }
}
