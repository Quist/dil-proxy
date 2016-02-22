package processors.protocols;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.http.common.HttpMessage;

import javax.servlet.http.HttpServletRequest;

public class HttpPreprocessor implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        HttpServletRequest req = exchange.getIn(HttpMessage.class).getRequest();
        exchange.getIn().setHeader("path", req.getRequestURL());
    }
}
