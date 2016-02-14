package routing.protocols;

import config.DilProxyConfig;
import org.apache.camel.builder.RouteBuilder;
import processors.HttpRequestProcessor;
import processors.WebServiceResponseProcessor;
import routing.RouteHandler;
import routing.builders.ProxyRouteBuilder;

public class HttpRoute implements DilRouteBuilder {

    private final DilProxyConfig config;

    public HttpRoute(DilProxyConfig config) {
        this.config = config;
    }

    @Override
    public String getToUri() {
        return String.format("jetty:http://%s/proxy?bridgeEndpoint=true", config.getProxyHostname());
    }

    @Override
    public String getListenUri() {
        return String.format("jetty:http://%s/proxy?matchOnUriPrefix=true", config.getHostname());
    }

    @Override
    public RouteBuilder create() {
        RouteHandler routeHandler = new RouteHandler(new HttpRequestProcessor(), new WebServiceResponseProcessor());
        String listenUri = getListenUri();
        return new ProxyRouteBuilder(config, routeHandler, listenUri);
    }
}
