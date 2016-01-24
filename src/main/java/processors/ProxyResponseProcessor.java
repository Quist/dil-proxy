package processors;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;

import javax.servlet.http.HttpServletRequest;

public class ProxyResponseProcessor implements Processor{

    @Override
    public void process(Exchange exchange) throws Exception {
        System.out.println("Received HTTP response");
        Message response = exchange.getIn();
    }
}
