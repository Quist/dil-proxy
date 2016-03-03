package processors;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.http.common.HttpMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import proxy.serializer.ProxyPayloadSerializer;

import javax.servlet.http.HttpServletRequest;

public class ProxyPreprocessor implements Processor {
    private final Logger logger = LoggerFactory.getLogger(ProxyPreprocessor.class);
    private final ProxyPayloadSerializer serializer;

    public ProxyPreprocessor(ProxyPayloadSerializer serializer) {
        this.serializer = serializer;
    }


    @Override
    public void process(Exchange exchange) throws Exception {
        logger.info("Starting pre processing exchange before sending to other proxy.");

        HttpServletRequest req = exchange.getIn(HttpMessage.class).getRequest();
        String proxyHeader = serializer.serialize(req);

        String body = exchange.getIn().getBody(String.class);
        body = proxyHeader + body;
        exchange.getIn().setBody(body);
    }
}
