package coap;

import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.server.resources.CoapExchange;


public class MyResource extends CoapResource{

    private final Endpoint endpoint;
    private final Processor processor;

    public MyResource(String name, Endpoint endpoint, Processor processor) {
        super(name);
        this.endpoint = endpoint;
        this.processor = processor;
    }

    @Override
    public void handlePOST(CoapExchange coapExchange) {
        System.out.println("Wazzup!");
        String body = new String(coapExchange.getRequestPayload());
        System.out.println("Body: " + body);

        Exchange camelExchange = endpoint.createExchange();
        camelExchange.getIn().setBody(body);
        try {
            processor.process(camelExchange);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //exchange.respond("HELLO WORLD");
    }
}
