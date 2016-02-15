package processors.protocols;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AmqpRequest implements Processor {

    final Logger logger = LoggerFactory.getLogger(AmqpRequest.class);

    @Override
    public void process(Exchange exchange) throws Exception {
        logger.info("Received AMQP request from proxy");
        //Message message = exchange.getIn();
    }
}
