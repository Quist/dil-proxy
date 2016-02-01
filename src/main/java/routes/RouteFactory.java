package routes;

import config.DilProxyConfig;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import processors.AmqpPreProcessor;
import proxy.HttpServletRequestLogger;

public class RouteFactory {
    final Logger logger = LoggerFactory.getLogger(RouteFactory.class);

    public RouteBuilder createProxyRouteBuilder(DilProxyConfig config) {
        switch (config.getProtocol()) {
            case AMQP:
                return new AmqpRouteBuilder(config);
            case HTTP:
                return new ProxyRouteBuilder(config);
            default:
                logger.error("No path configuration for: " + config.getProtocol());
                throw new IllegalArgumentException("No path configuration for: " + config.getProtocol());
        }
    }

    public RouteBuilder createWebServiceRouteBuilder(DilProxyConfig config) {
        return null;
    }
}
