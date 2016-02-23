package processors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestLogger;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.http.common.HttpMessage;

import javax.servlet.http.HttpServletRequest;

class WebServiceRequestProcessor implements Processor {

    private static final HttpRequestLogger HTTP_REQUEST_LOGGER = new HttpRequestLogger();
    private final Logger logger = LoggerFactory.getLogger(WebServiceRequestProcessor.class);

    private WebServiceRequestProcessor() {
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        HttpServletRequest request = exchange.getIn(HttpMessage.class).getRequest();
        HTTP_REQUEST_LOGGER.log(request, null);
        logger.info("Received request from Web service. Forwarding request to proxy.");
    }
}
