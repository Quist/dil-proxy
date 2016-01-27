package processors;

import proxy.HttpServletRequestLogger;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.http.common.HttpMessage;

import javax.servlet.http.HttpServletRequest;

public class ProxyRequestProcessor implements Processor {

    private HttpServletRequestLogger httpServletRequestLogger = new HttpServletRequestLogger();

    @Override
    public void process(Exchange exchange) throws Exception {
        HttpServletRequest req = exchange.getIn(HttpMessage.class).getRequest();

        httpServletRequestLogger.log(req, null);

    }
}
