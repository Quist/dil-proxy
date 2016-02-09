package routing;

import config.DilProxyConfig;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import routing.protocols.MqttRouteFactory;
import routing.protocols.AmqpRouteFactory;
import routing.protocols.HttpRouteFactory;

/**
 * Creates routes between the proxy pair.
 */
public class ProxyRouteFactory {
    final Logger logger = LoggerFactory.getLogger(ProxyRouteFactory.class);

    public RouteBuilder createProxyRouteBuilder(DilProxyConfig config) {
        switch (config.getProtocol()) {
            case AMQP:
                return createAmqpRoute(config);
            case HTTP:
                return createHttpRoute(config);
            case MQTT:
                return createMqttRoute(config);
            default:
                logger.error("No path configuration for: " + config.getProtocol());
                throw new IllegalArgumentException("No configuration for: " + config.getProtocol());
        }
    }

    private RouteBuilder createMqttRoute(DilProxyConfig config) {
        return new MqttRouteFactory(config).create();
    }

    private RouteBuilder createAmqpRoute(DilProxyConfig config) {
        return new AmqpRouteFactory(config).create();
    }

    private RouteBuilder createHttpRoute(DilProxyConfig config) {
        return new HttpRouteFactory(config).create();
    }
}
