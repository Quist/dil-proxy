package coap;

import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultProducer;

public class CoapProducer extends DefaultProducer {

    private final CoapEndpoint endpoint;

    public CoapProducer(CoapEndpoint endpoint) {
        super(endpoint);
        this.endpoint = endpoint;
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        System.out.println("lol");
    }
}
