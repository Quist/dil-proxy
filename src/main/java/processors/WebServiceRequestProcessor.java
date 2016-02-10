package processors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import proxy.HttpRequestLogger;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.http.common.HttpMessage;

import javax.servlet.http.HttpServletRequest;

public class WebServiceRequestProcessor implements Processor {

    private static final HttpRequestLogger HTTP_REQUEST_LOGGER = new HttpRequestLogger();
    final Logger logger = LoggerFactory.getLogger(WebServiceRequestProcessor.class);

    public WebServiceRequestProcessor() {
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        HttpServletRequest request = exchange.getIn(HttpMessage.class).getRequest();
        HTTP_REQUEST_LOGGER.log(request, null);
        logger.info("Forwarding request to proxy");
    }
}
