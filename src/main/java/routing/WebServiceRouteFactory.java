package routing;

import config.DilProxyConfig;
import org.apache.camel.builder.RouteBuilder;
import processors.ResponseProcessor;
import processors.WebServiceRequestProcessor;
import routing.routes.CamelWebServiceRoute;
import routing.protocols.DilRouteBuilder;

public class WebServiceRouteFactory {

    private final DilProxyConfig config;

    public WebServiceRouteFactory(DilProxyConfig config) {
        this.config = config;
    }

    public RouteBuilder create(DilRouteBuilder dilRouteBuilder) {
        RouteProcessorContainer routeProcessorContainer = getRouteProcessors();
        String toUri = dilRouteBuilder.getToUri();

        return new CamelWebServiceRoute(config, routeProcessorContainer, toUri);
    }

    private RouteProcessorContainer getRouteProcessors() {
        WebServiceRequestProcessor requestProcessor = new WebServiceRequestProcessor();
        ResponseProcessor responseProcessor = new ResponseProcessor();

        return new RouteProcessorContainer(requestProcessor, responseProcessor);
    }
}
