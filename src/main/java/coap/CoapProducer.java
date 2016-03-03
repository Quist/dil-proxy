package coap;

import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultProducer;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.coap.CoAP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.ConnectException;

import static org.eclipse.californium.core.coap.MediaTypeRegistry.TEXT_PLAIN;

class CoapProducer extends DefaultProducer {
    private static final int HTTP_GATEWAY_TIMEOUT = 504;
    private final Logger logger = LoggerFactory.getLogger(CoapProducer.class);

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
            logger.warn("No CoAP response received.");
            exchange.getIn().setHeader(Exchange.HTTP_RESPONSE_CODE, HTTP_GATEWAY_TIMEOUT);
            exchange.getIn().setBody("");
            throw new ConnectException("No CoAP response received");
        } else {
            logger.info("CoAP response received. Status code: " + response.getCode());

            exchange.getIn().setBody(response.getResponseText());
            exchange.getIn().setHeader(Exchange.HTTP_RESPONSE_CODE, getHttpStatusCode(response.getCode())); /* To do: Map this to correct status code.*/
        }
    }

    private int getHttpStatusCode(CoAP.ResponseCode coapResponseCode) {
        switch(coapResponseCode) {
            case CONTENT:
                return HttpStatus.SC_OK;
            case CREATED:
                return HttpStatus.SC_CREATED;
            case DELETED:
                return HttpStatus.SC_NO_CONTENT;
            case NOT_FOUND:
                return HttpStatus.SC_NOT_FOUND;
            case INTERNAL_SERVER_ERROR:
                return HttpStatus.SC_INTERNAL_SERVER_ERROR;
            case GATEWAY_TIMEOUT:
                return HttpStatus.SC_GATEWAY_TIMEOUT;
            default:
                logger.info("Unknown CoAP response code: " + coapResponseCode + ". Returning 200 OK");
                return HttpStatus.SC_OK;
        }
    }
}
