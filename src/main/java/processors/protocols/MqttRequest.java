package processors.protocols;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import proxy.serializer.ProxyPayload;
import proxy.serializer.ProxyPayloadDeserializer;

class MqttRequest implements Processor {
    private final ProxyPayloadDeserializer deserializer;

    private final Logger logger = LoggerFactory.getLogger(MqttRequest.class);

    private MqttRequest() {
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
