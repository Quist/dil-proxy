package main.java.proxy;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.http.common.HttpMessage;

import javax.servlet.http.HttpServletRequest;

public class IncomingWebServiceMessageProcessor implements Processor {

    private HttpServletRequestLogger httpServletRequestLogger = new HttpServletRequestLogger();

    @Override
    public void process(Exchange exchange) throws Exception {
        HttpServletRequest request = exchange.getIn(HttpMessage.class).getRequest();
        String body = exchange.getIn().getBody(String.class);
        if (body == null) {
            body = "";
        }

        exchange.getIn().setHeader("path", request.getRequestURL());
        httpServletRequestLogger.log(request, body);

        exchange.getIn().setBody(body);
    }
}
