package routing;

import config.DilProxyConfig;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import routing.protocols.CoapRoute;
import routing.protocols.MqttRoute;
import routing.protocols.AmqpRoute;
import routing.protocols.HttpRoute;

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
            case COAP:
                return createCoapRoute(config);
            default:
                logger.error("No path configuration for: " + config.getProtocol());
                throw new IllegalArgumentException("No configuration for: " + config.getProtocol());
        }
    }

    private RouteBuilder createCoapRoute(DilProxyConfig config) {
        return new CoapRoute(config).create();
    }

    private RouteBuilder createMqttRoute(DilProxyConfig config) {
        return new MqttRoute(config).create();
    }

    private RouteBuilder createAmqpRoute(DilProxyConfig config) {
        return new AmqpRoute(config).create();
    }

    private RouteBuilder createHttpRoute(DilProxyConfig config) {
        return new HttpRoute(config).create();
    }
}
