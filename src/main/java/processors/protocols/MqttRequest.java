package processors.protocols;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import proxy.serializer.ProxyMessage;
import proxy.serializer.ProxyRequestDeserializer;

class MqttRequest implements Processor {
    private final ProxyRequestDeserializer deserializer;

    private final Logger logger = LoggerFactory.getLogger(MqttRequest.class);

    private MqttRequest() {
        this.deserializer = new ProxyRequestDeserializer();
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        logger.info("Received MQTT request from proxy");
        String body = exchange.getIn().getBody(String.class);
        ProxyMessage payload = deserializer.deserialize(body);
        exchange.getIn().setBody(payload.getBody());
        exchange.getIn().setHeader("Dil-Path", payload.getPath());
    }

}
