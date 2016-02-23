package routing.protocols;

import config.DilProxyConfig;

public class CoapRoute extends DilRouteBuilder {
    private final DilProxyConfig config;

    public CoapRoute(DilProxyConfig config) {
        this.config = config;
    }

    @Override
    public String getToUri() {
        return "coap:" + config.getTargetProxyHostname() + "/test";
    }

    @Override
    public String getListenUri() {
        return "coap:" + config.getHostname();
    }

}
