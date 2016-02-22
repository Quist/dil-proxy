package routing.protocols;

import config.DilProxyConfig;
import org.apache.camel.builder.RouteBuilder;
import processors.WebServiceResponseProcessor;
import processors.protocols.CoapRequest;
import routing.routes.CamelProxyRoute;

public class CoapRoute extends DilRouteBuilder {
    private DilProxyConfig config;

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
