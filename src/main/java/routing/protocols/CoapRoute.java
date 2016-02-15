package routing.protocols;

import config.DilProxyConfig;
import org.apache.camel.builder.RouteBuilder;
import processors.WebServiceResponseProcessor;
import processors.protocols.CoapRequest;
import routing.RouteProcessorContainer;
import routing.routes.CamelProxyRoute;

public class CoapRoute implements DilRouteBuilder {
    private DilProxyConfig config;

    public CoapRoute(DilProxyConfig config) {
        this.config = config;
    }

    @Override
    public String getToUri() {
        return "coap:" + config.getProxyHostname() + "/test";
    }

    @Override
    public String getListenUri() {
        return "coap:" + config.getHostname();
    }

    @Override
    public RouteBuilder create() {
        RouteProcessorContainer routeProcessorContainer = new RouteProcessorContainer(new CoapRequest(), new WebServiceResponseProcessor());
        return new CamelProxyRoute(config, routeProcessorContainer, getListenUri());
    }
}
