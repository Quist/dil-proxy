package routing;

import config.DilProxyConfig;
import org.apache.camel.builder.RouteBuilder;
import processors.ResponseProcessor;
import processors.WebServiceRequestProcessor;
import routing.RouteHandler;
import routing.builders.WebServiceRouteBuilder;
import routing.protocols.ProtocolFactory;

public class WebServiceRouteFactory {

    private final DilProxyConfig config;

    public WebServiceRouteFactory(DilProxyConfig config) {
        this.config = config;
    }

    public RouteBuilder create(ProtocolFactory protocolFactory) {
        RouteHandler routeHandler = getRouteHandler();
        String toUri = protocolFactory.getToUri();

        return new WebServiceRouteBuilder(config, routeHandler, toUri);
    }

    private RouteHandler getRouteHandler() {
        WebServiceRequestProcessor requestProcessor = new WebServiceRequestProcessor();
        ResponseProcessor responseProcessor = new ResponseProcessor();

        return new RouteHandler(requestProcessor, responseProcessor);
    }
}
