package routing.protocols;

import config.DilProxyConfig;
import org.apache.camel.builder.RouteBuilder;
import processors.protocols.HttpRequest;
import processors.WebServiceResponseProcessor;
import routing.RouteProcessorContainer;
import routing.routes.CamelProxyRoute;

public class HttpRoute implements DilRouteBuilder {

    private final DilProxyConfig config;

    public HttpRoute(DilProxyConfig config) {
        this.config = config;
    }

    @Override
    public String getToUri() {
        return String.format("jetty:http://%s/proxy?bridgeEndpoint=true", config.getTargetProxyHostname());
    }

    @Override
    public String getListenUri() {
        String hostname = String.format("%s:%s", config.getHostname(), config.getPort());
        return String.format("jetty:http://%s/proxy?matchOnUriPrefix=true", hostname);
    }

    @Override
    public RouteBuilder create() {
        RouteProcessorContainer routeProcessorContainer = new RouteProcessorContainer(new HttpRequest(), new WebServiceResponseProcessor());
        String listenUri = getListenUri();
        return new CamelProxyRoute(config, routeProcessorContainer, listenUri);
    }
}
