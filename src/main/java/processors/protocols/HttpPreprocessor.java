package processors.protocols;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.http.common.HttpMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;

public class HttpPreprocessor implements Processor {
    private final Logger logger = LoggerFactory.getLogger(HttpPreprocessor.class);

    @Override
    public void process(Exchange exchange) throws Exception {
        logger.info("Pre processing http message before sending to other proxy.");
        HttpServletRequest req = exchange.getIn(HttpMessage.class).getRequest();
        exchange.getIn().setHeader("path", req.getRequestURL());
        exchange.getIn().setHeader(Exchange.HTTP_METHOD, "POST");
    }
}
