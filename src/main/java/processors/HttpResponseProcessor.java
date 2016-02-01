package processors;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import proxy.HttpServletRequestLogger;

public class HttpResponseProcessor implements Processor {

    final Logger logger = LoggerFactory.getLogger(HttpResponseProcessor.class);

    @Override
    public void process(Exchange exchange) throws Exception {
        logger.info("Received response from Web service. Forwarding response to the proxy");
    }
}
