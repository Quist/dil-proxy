package processors;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import proxy.serializer.ProxyHeaderDeserializer;
import proxy.serializer.ProxyMessage;
import proxy.serializer.ProxyRequestDeserializer;

import java.util.Iterator;

public class ProxyPostProcessor implements Processor {
    private final Logger logger = LoggerFactory.getLogger(ProxyPostProcessor.class);

    private final ProxyRequestDeserializer deserializer;

    public ProxyPostProcessor() {
        this.deserializer = new ProxyRequestDeserializer(new ProxyHeaderDeserializer());
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        logger.debug("Starting post processing of exchange received from other proxy.");
        String payload = exchange.getIn().getBody(String.class);
        ProxyMessage proxyMessage = deserializer.deserialize(payload);

        setHttpHeaders(exchange, proxyMessage);
        setBody(exchange, proxyMessage);
        removeCamelRoutingHeaders(exchange);
    }

    private void setBody(Exchange exchange, ProxyMessage proxyMessage) {
        if (proxyMessage.getBody().isPresent()) {
            exchange.getIn().setBody(proxyMessage.getBody().get());
        }
    }

    private void setHttpHeaders(Exchange exchange, ProxyMessage proxyMessage) {
        exchange.getIn().setHeader("path", proxyMessage.getPath());
        exchange.getIn().setHeader(Exchange.HTTP_METHOD, proxyMessage.getMethod());
        if (proxyMessage.getQuery().isPresent()) {
            exchange.getIn().setHeader(Exchange.HTTP_QUERY, proxyMessage.getQuery().get());
        }

        Iterator<String> it = proxyMessage.getHttpHeaders().keySet().iterator();
        while (it.hasNext()) {
            String headerName = it.next();
            String headerValue = proxyMessage.getHttpHeaders().get(headerName);
            exchange.getIn().setHeader(headerName, headerValue);
        }
    }

    /*
    * We want to route the message manually and not through camel.
     */
    private void removeCamelRoutingHeaders(Exchange exchange) {
        exchange.getIn().removeHeader("CamelHttpPath");
    }
}
