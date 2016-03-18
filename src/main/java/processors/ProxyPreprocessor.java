package processors;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.http.common.HttpMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import proxy.serializer.ProxyRequestSerializer;

import javax.servlet.http.HttpServletRequest;

public class ProxyPreprocessor implements Processor {
    private final Logger logger = LoggerFactory.getLogger(ProxyPreprocessor.class);
    private final ProxyRequestSerializer serializer;

    public ProxyPreprocessor(ProxyRequestSerializer serializer) {
        this.serializer = serializer;
    }


    @Override
    public void process(Exchange exchange) throws Exception {
        logger.info("Starting pre processing exchange before sending to other proxy.");

        HttpMessage httpMessage = exchange.getIn(HttpMessage.class);
        String proxyHeader = serializer.serialize(httpMessage);

        String body = exchange.getIn().getBody(String.class);
        body = proxyHeader + body;
        exchange.getIn().setBody(body);
    }
}
