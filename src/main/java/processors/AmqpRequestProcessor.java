package processors;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AmqpRequestProcessor implements Processor {

    final Logger logger = LoggerFactory.getLogger(AmqpRequestProcessor.class);

    @Override
    public void process(Exchange exchange) throws Exception {
        logger.info("Received AMQP request from proxy");
        //Message message = exchange.getIn();
    }
}
