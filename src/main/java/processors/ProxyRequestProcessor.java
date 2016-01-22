package processors;

import main.java.proxy.HttpServletRequestLogger;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.http.common.HttpMessage;

import javax.servlet.http.HttpServletRequest;

public class ProxyRequestProcessor implements Processor {

    private HttpServletRequestLogger httpServletRequestLogger = new HttpServletRequestLogger();

    @Override
    public void process(Exchange exchange) throws Exception {
        HttpServletRequest req = exchange.getIn(HttpMessage.class).getRequest();

        String body = exchange.getIn().getBody(String.class);


        exchange.getOut().setHeaders(exchange.getIn().getHeaders());
        if (body == null) {
            body = "";
        }

        httpServletRequestLogger.log(req, body);

    }
}
