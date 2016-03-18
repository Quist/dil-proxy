package processors.protocols;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import proxy.serializer.ProxyMessage;
import proxy.serializer.ProxyMessageDeserializer;

class MqttRequest implements Processor {
    private final ProxyMessageDeserializer deserializer;

    private final Logger logger = LoggerFactory.getLogger(MqttRequest.class);

    private MqttRequest() {
        this.deserializer = new ProxyMessageDeserializer();
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        logger.info("Received MQTT request from proxy");
        String body = exchange.getIn().getBody(String.class);
        ProxyMessage payload = deserializer.deserialize(body);
        exchange.getIn().setBody(payload.getBody());
        exchange.getIn().setHeader("path", payload.getPath());
    }

}
