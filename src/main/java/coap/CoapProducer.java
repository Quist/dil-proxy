package coap;

import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultProducer;
import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;

import static org.eclipse.californium.core.coap.MediaTypeRegistry.TEXT_PLAIN;

class CoapProducer extends DefaultProducer {
    private static final int HTTP_GATEWAY_TIMEOUT = 504;

    private final CamelCoapEndpoint endpoint;

    public CoapProducer(CamelCoapEndpoint endpoint) {
        super(endpoint);
        this.endpoint = endpoint;
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        CoapClient client = new CoapClient(endpoint.getEndpointUri());
        CoapResponse response = client.post(exchange.getIn().getBody().toString(), TEXT_PLAIN);

        if (response == null) {
            System.out.println("No COAP response :(");
            exchange.getIn().setHeader(Exchange.HTTP_RESPONSE_CODE, HTTP_GATEWAY_TIMEOUT);
            exchange.getIn().setBody("");
        } else {
            exchange.getIn().setBody(response.getResponseText());
        }
    }
}
