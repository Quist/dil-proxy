package processors;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;

public class HttpResponseProcessor implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        Message response = exchange.getIn();
       // exchange.getOut().setBody("LOL");
        System.out.println("Received HTTP response)");
    }
}
