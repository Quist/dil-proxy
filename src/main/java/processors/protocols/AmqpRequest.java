package processors.protocols;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;

public class AmqpRequest implements Processor {

    private final Logger logger = LoggerFactory.getLogger(AmqpRequest.class);

    @Override
    public void process(Exchange exchange) throws Exception {
        logger.info("Received AMQP request from proxy. Headers: ");
        printHeaders(exchange);
    }

    private void printHeaders(Exchange exchange) {
        Iterator<String> iterator = exchange.getIn().getHeaders().keySet().iterator();
        Message in = exchange.getIn();
        while(iterator.hasNext()) {
            String headerName = iterator.next();
            Object headerValue = in.getHeader(headerName);
            if (headerValue != null) {
                logger.info(headerName + ": " + headerValue);
            } else {
                logger.info(headerName + ": ");
            }
        }
    }
}
