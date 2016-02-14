package coap;

import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.impl.DefaultEndpoint;

public class CoapEndpoint extends DefaultEndpoint {

    public CoapEndpoint(String uri, CoapComponent coapComponent) {
        super(uri, coapComponent);
    }

    @Override
    public Producer createProducer() throws Exception {
        return new CoapProducer(this);
    }

    @Override
    public Consumer createConsumer(Processor processor) throws Exception {
        return new CoapConsumer(this, processor);
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
