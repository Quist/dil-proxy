package main.java.proxy;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;

public class ProxyResponseProcessor implements Processor{

    @Override
    public void process(Exchange exchange) throws Exception {
        System.out.println("Received HTTP response");
        Message response = exchange.getIn();
    }
}
