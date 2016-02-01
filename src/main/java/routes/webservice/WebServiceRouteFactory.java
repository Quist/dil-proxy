package routes.webservice;

import config.DilProxyConfig;
import org.apache.camel.builder.RouteBuilder;
import processors.HttpResponseProcessor;
import processors.WebServiceRequestProcessor;
import routes.RouteHandler;
import routes.proxie.protocols.ProtocolFactory;

public class WebServiceRouteFactory {

    public RouteBuilder createWebServiceRouteBuilder(DilProxyConfig config, ProtocolFactory protocolFactory) {
        RouteHandler routeHandler = getRouteHandler();
        String toUri = protocolFactory.getToUri();

        return new WebServiceRouteBuilder(config, routeHandler, toUri);
    }

    private RouteHandler getRouteHandler() {
        WebServiceRequestProcessor requestProcessor = new WebServiceRequestProcessor();
        HttpResponseProcessor responseProcessor = new HttpResponseProcessor();

        return new RouteHandler(requestProcessor, responseProcessor);
    }
}
