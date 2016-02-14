package routing.protocols;

import config.DilProxyConfig;
import org.apache.camel.builder.RouteBuilder;
import processors.AmqpRequestProcessor;
import processors.WebServiceResponseProcessor;
import routing.RouteHandler;
import routing.builders.ProxyRouteBuilder;
import routing.protocols.ProtocolFactory;

public class CoapRoute implements ProtocolFactory {
    private DilProxyConfig config;

    public CoapRoute(DilProxyConfig config) {
        this.config = config;
    }

    @Override
    public String getToUri() {
        return "coap:" + config.getProxyHostname();
    }

    @Override
    public String getListenUri() {
        return "coap:" + config.getHostname();
    }

    @Override
    public RouteBuilder create() {
        RouteHandler routeHandler = new RouteHandler(new AmqpRequestProcessor(), new WebServiceResponseProcessor());
        return new ProxyRouteBuilder(config, routeHandler, getListenUri());
    }
}
