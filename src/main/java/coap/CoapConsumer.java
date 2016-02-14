package coap;

import org.apache.camel.Consumer;
import org.apache.camel.Endpoint;
import org.apache.camel.Processor;
import org.apache.camel.impl.DefaultConsumer;

public class CoapConsumer extends DefaultConsumer {

    private final CoapEndpoint endpoint;

    public CoapConsumer(CoapEndpoint coapEndpoint, Processor processor) {
        super(coapEndpoint, processor);
        this.endpoint = coapEndpoint;
    }
}
