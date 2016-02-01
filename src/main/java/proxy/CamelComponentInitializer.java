package proxy;

import config.AmqpConfig;
import config.DilProxyConfig;
import org.apache.camel.CamelContext;
import org.apache.camel.component.amqp.AMQPComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.net.MalformedURLException;

public class CamelComponentInitializer {
    private final Logger logger = LoggerFactory.getLogger(HttpRequestLogger.class);
    private final CamelContext camelContext;

    public CamelComponentInitializer(CamelContext camelContext) {
        this.camelContext = camelContext;
    }

    public void init(DilProxyConfig config) throws MalformedURLException {
        switch (config.getProtocol()) {
            case AMQP:
                addAmqpComponent(config.getAmqpConfig());
        }
    }

    private void addAmqpComponent(AmqpConfig config) throws MalformedURLException {
        logger.info("Adding AMQP component with broker connection URI: " + config.getBrokerConnectionUri());
        AMQPComponent amqp = AMQPComponent.amqp10Component(config.getBrokerConnectionUri());
        camelContext.addComponent("amqp", amqp);
    }
}