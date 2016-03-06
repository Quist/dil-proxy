package processors;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import proxy.serializer.ProxyPayload;
import proxy.serializer.ProxyPayloadDeserializer;

public class ProxyPostProcessor implements Processor {
    private final Logger logger = LoggerFactory.getLogger(ProxyPostProcessor.class);

    private final ProxyPayloadDeserializer deserializer;

    public ProxyPostProcessor() {
        this.deserializer = new ProxyPayloadDeserializer();
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        logger.info("Starting post processing of exchange received from other proxy.");
        String body = exchange.getIn().getBody(String.class);
        ProxyPayload payload = deserializer.deserialize(body);

        exchange.getIn().setHeader("path", payload.getPath());
        exchange.getIn().setHeader(Exchange.HTTP_METHOD, payload.getMethod());

        if (payload.getBody().isPresent()) {
            exchange.getIn().setBody(payload.getBody().get());
        }

        if (payload.getQuery().isPresent()) {
            exchange.getIn().setHeader(Exchange.HTTP_QUERY, payload.getQuery().get());
        }

        removeCamelRoutingHeaders(exchange);

    }

    private void removeCamelRoutingHeaders(Exchange exchange) {
        exchange.getIn().removeHeader("CamelHttpPath");
    }
}
