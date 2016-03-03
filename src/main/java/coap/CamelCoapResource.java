package coap;

import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.coap.CoAP;
import org.eclipse.californium.core.server.resources.CoapExchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.eclipse.californium.core.coap.CoAP.ResponseCode;
import static org.eclipse.californium.core.coap.CoAP.ResponseCode.INTERNAL_SERVER_ERROR;

class CamelCoapResource extends CoapResource {
    private final Logger logger = LoggerFactory.getLogger(CamelCoapResource.class);

    private final Endpoint endpoint;
    private final Processor processor;

    public CamelCoapResource(String name, Endpoint endpoint, Processor processor) {
        super(name);
        this.endpoint = endpoint;
        this.processor = processor;
    }

    @Override
    public void handleRequest(org.eclipse.californium.core.network.Exchange exchange) {
        logger.info("Received CoAP request. Converting to camel exchange.");
        CoapExchange coapExchange = new CoapExchange(exchange, this);
        Exchange camelExchange = convertExchange(coapExchange);

        try {
            processor.process(camelExchange);
        } catch (Exception e) {
            e.printStackTrace();
        }

        respond(coapExchange, camelExchange);
    }

    private void respond(CoapExchange coapExchange, Exchange camelExchange) {
        Exception e = camelExchange.getException();
        if (e != null) {
            coapExchange.respond(INTERNAL_SERVER_ERROR, e.getMessage());
        } else {
            String body = camelExchange.getIn().getBody(String.class);

            int httpStatusCode = (int) camelExchange.getIn().getHeader(Exchange.HTTP_RESPONSE_CODE);
            ResponseCode responseCode = getResponseCode(httpStatusCode);
            logger.info("Responding to CoAP request. CoAP response code: " + responseCode);
            coapExchange.respond(responseCode, body);
        }
    }

    private ResponseCode getResponseCode(int httpStatusCode) {
        switch (httpStatusCode) {
            case 200 :
                return ResponseCode.CONTENT;
            case 201 :
                return ResponseCode.CREATED;
            case 404 :
                return ResponseCode.NOT_FOUND;
            default:
                logger.info("Unknown HTTP code. Setting default success code");
                return ResponseCode._UNKNOWN_SUCCESS_CODE;
        }
    }

    private Exchange convertExchange(CoapExchange coapExchange) {
        Exchange camelExchange = endpoint.createExchange();

        String body = new String(coapExchange.getRequestPayload());
        camelExchange.getIn().setBody(body);
        camelExchange.getIn().setHeader(Exchange.HTTP_METHOD, "GET");
        return camelExchange;
    }
}
