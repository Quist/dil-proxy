package processors.protocols;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestLogger;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.http.common.HttpMessage;

import javax.servlet.http.HttpServletRequest;

public class HttpRequest implements Processor {
    final Logger logger = LoggerFactory.getLogger(HttpRequest.class);

    @Override
    public void process(Exchange exchange) throws Exception {
        logger.info("Received an incoming HTTP request message from the other proxy.");
    }
}
