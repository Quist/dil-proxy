package processors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import proxy.HttpServletRequestLogger;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.http.common.HttpMessage;

import javax.servlet.http.HttpServletRequest;

public class ProxyRequestProcessor implements Processor {
    final Logger logger = LoggerFactory.getLogger(ProxyRequestProcessor.class);
    private HttpServletRequestLogger httpServletRequestLogger = new HttpServletRequestLogger();

    @Override
    public void process(Exchange exchange) throws Exception {
        logger.info("Received an incoming proxy message from the other proxy.");
        HttpServletRequest req = exchange.getIn(HttpMessage.class).getRequest();
        httpServletRequestLogger.log(req, null);
        logger.info("Forwarding request to Web service.");
    }
}
