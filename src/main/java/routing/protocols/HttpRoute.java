package routing.protocols;

import config.DilProxyConfig;

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
