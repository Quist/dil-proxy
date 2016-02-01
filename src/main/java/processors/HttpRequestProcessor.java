package processors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import proxy.HttpServletRequestLogger;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.http.common.HttpMessage;

import javax.servlet.http.HttpServletRequest;

public class HttpRequestProcessor implements Processor {

    private static final HttpServletRequestLogger httpServletRequestLogger = new HttpServletRequestLogger();
    final Logger logger = LoggerFactory.getLogger(HttpServletRequestLogger.class);

    public HttpRequestProcessor() {
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        HttpServletRequest request = exchange.getIn(HttpMessage.class).getRequest();

        exchange.getIn().setHeader("path", request.getRequestURL());
        httpServletRequestLogger.log(request, null);
        logger.info("Forwarding request to other proxy");
    }
}
