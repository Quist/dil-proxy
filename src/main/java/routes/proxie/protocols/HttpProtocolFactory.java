package routes.proxie.protocols;

import config.DilProxyConfig;
import org.apache.camel.builder.RouteBuilder;
import processors.HttpRequestProcessor;
import processors.WebServiceResponseProcessor;
import routes.RouteHandler;
import routes.proxie.ProxyRouteBuilder;

public class HttpProtocolFactory implements ProtocolFactory {

    private final DilProxyConfig config;

    public HttpProtocolFactory(DilProxyConfig config) {
        this.config = config;
    }

    @Override
    public String getToUri() {
        return String.format("jetty:%s/proxy?bridgeEndpoint=true", config.getProxyHostname());
    }

    @Override
    public String getListenUri() {
        return String.format("jetty:%s/proxy?matchOnUriPrefix=true", config.getHostname());
    }

    @Override
    public RouteBuilder create() {
        RouteHandler routeHandler = new RouteHandler(new HttpRequestProcessor(), new WebServiceResponseProcessor());
        String listenUri = new HttpProtocolFactory(config).getListenUri();
        return new ProxyRouteBuilder(config, routeHandler, listenUri);
    }
}
