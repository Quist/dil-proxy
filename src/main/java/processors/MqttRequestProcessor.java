package processors;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import proxy.ProxyPayload;
import proxy.ProxyPayloadDeserializer;

public class MqttRequestProcessor implements Processor {
    private final ProxyPayloadDeserializer deserializer;

    private final Logger logger = LoggerFactory.getLogger(MqttRequestProcessor.class);

    public MqttRequestProcessor() {
        this.deserializer = new ProxyPayloadDeserializer();
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        logger.info("Received MQTT request from proxy");
        String body = exchange.getIn().getBody(String.class);
        ProxyPayload payload = deserializer.deserialize(body);
        exchange.getIn().setBody(payload.getBody());
        exchange.getIn().setHeader("path", payload.getPath());
    }


}
