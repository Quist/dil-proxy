package routing.protocols;

import config.DilProxyConfig;
import org.apache.camel.builder.RouteBuilder;
import processors.protocols.HttpRequest;
import processors.WebServiceResponseProcessor;
import routing.routes.CamelProxyRoute;

public class HttpRoute extends DilRouteBuilder {

    private final DilProxyConfig config;

    public HttpRoute(DilProxyConfig config) {
        this.config = config;
    }

    @Override
    public String getToUri() {
        return String.format("http://%s/proxy?bridgeEndpoint=true", config.getTargetProxyHostname());
    }

    @Override
    public String getListenUri() {
        String hostname = String.format("%s:%s", config.getHostname(), config.getPort());
        return String.format("jetty:http://%s/proxy?matchOnUriPrefix=true", hostname);
    }

}
