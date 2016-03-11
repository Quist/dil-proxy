package coap;

import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
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

        if (camelExchange.getException() != null) {
            respondException(coapExchange, camelExchange.getException());
        } else {
            respond(coapExchange, camelExchange);
        }
    }

    private void respond(CoapExchange coapExchange, Exchange camelExchange) {
        byte body[];

        if (camelExchange.hasOut()) {
            body = camelExchange.getOut().getBody(byte[].class);
        } else {
            body = camelExchange.getIn().getBody(byte[].class);
        }

        int httpStatusCode = (int) camelExchange.getIn().getHeader(Exchange.HTTP_RESPONSE_CODE);
        ResponseCode responseCode = getResponseCode(httpStatusCode);
        logger.info("Responding to CoAP request. CoAP response code: " + responseCode);
        coapExchange.respond(responseCode, body, MediaTypeRegistry.APPLICATION_OCTET_STREAM);
    }

    private void respondException(CoapExchange coapExchange, Exception exception) {
        coapExchange.respond(INTERNAL_SERVER_ERROR, exception.getMessage());
    }

    private ResponseCode getResponseCode(int httpStatusCode) {
        switch (httpStatusCode) {
            case 200 :
                return ResponseCode.CONTENT;
            case 201 :
                return ResponseCode.CREATED;
            case 204 :
                return ResponseCode.DELETED;
            case 404 :
                return ResponseCode.NOT_FOUND;
            case 500 :
                return ResponseCode.INTERNAL_SERVER_ERROR;
            case 504 :
                return ResponseCode.GATEWAY_TIMEOUT;
            default:
                logger.info("Unknown HTTP code. Setting default success code");
                return ResponseCode._UNKNOWN_SUCCESS_CODE;
        }
    }

    private Exchange convertExchange(CoapExchange coapExchange) {
        Exchange camelExchange = endpoint.createExchange();

        byte bytes[] = coapExchange.getRequestPayload();
        camelExchange.getIn().setBody(bytes);
        camelExchange.getIn().setHeader(Exchange.HTTP_METHOD, "GET");
        return camelExchange;
    }
}
