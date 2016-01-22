package routes;

import processors.HttpResponseProcessor;
import processors.ProxyRequestProcessor;
import org.apache.camel.builder.RouteBuilder;

public class ProxyRouteBuilder extends RouteBuilder {

    private final String host;

    public ProxyRouteBuilder(String host) {
        this.host = host;
    }

    @Override
    public void configure() throws Exception {
        final String proxyListenRoute = String.format("jetty:%s/proxy?matchOnUriPrefix=true", host);

        from(proxyListenRoute)
                .process(new ProxyRequestProcessor())
                .removeHeaders("CamelHttp*")
                .toD("${header.path}" + "?bridgeEndpoint=true")
                .process(new HttpResponseProcessor());
    }
}
