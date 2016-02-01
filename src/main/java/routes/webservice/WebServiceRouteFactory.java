package routes.webservice;

import config.DilProxyConfig;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import processors.HttpResponseProcessor;
import processors.WebServiceRequestProcessor;
import routes.RouteHandler;
import routes.proxie.protocols.AmqpRouteFactory;
import routes.proxie.protocols.HttpProtocolFactory;

public class WebServiceRouteFactory {
    private final Logger logger = LoggerFactory.getLogger(WebServiceRouteFactory.class);

    public RouteBuilder createWebServiceRouteBuilder(DilProxyConfig config) {
        RouteHandler routeHandler = getRouteHandler();
        String toUri = getToUri(config);

        return new WebServiceRouteBuilder(config, routeHandler, toUri);
    }

    private RouteHandler getRouteHandler() {
        WebServiceRequestProcessor requestProcessor = new WebServiceRequestProcessor();
        HttpResponseProcessor responseProcessor = new HttpResponseProcessor();

        return new RouteHandler(requestProcessor, responseProcessor);
    }

    private String getToUri(DilProxyConfig config) {
        switch (config.getProtocol()) {
            case AMQP:
                return new AmqpRouteFactory(config).getToUri();
            case HTTP:
                return new HttpProtocolFactory(config).getToUri();
            default:
                logger.error("No path configuration for: " + config.getProtocol());
                throw new IllegalArgumentException("No configuration for: " + config.getProtocol());
        }
    }
}
