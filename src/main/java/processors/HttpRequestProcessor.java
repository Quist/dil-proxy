package processors;

import proxy.HttpServletRequestLogger;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.http.common.HttpMessage;

import javax.servlet.http.HttpServletRequest;

public class HttpRequestProcessor implements Processor {

    private static final HttpServletRequestLogger httpServletRequestLogger = new HttpServletRequestLogger();

    public HttpRequestProcessor() {
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        HttpServletRequest request = exchange.getIn(HttpMessage.class).getRequest();

        exchange.getIn().setHeader("path", request.getRequestURL());
        httpServletRequestLogger.log(request, null);
    }
}
