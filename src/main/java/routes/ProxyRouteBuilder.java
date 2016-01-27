package routes;

import config.DilProxyConfig;
import processors.HttpResponseProcessor;
import processors.ProxyRequestProcessor;
import org.apache.camel.builder.RouteBuilder;

public class ProxyRouteBuilder extends RouteBuilder {

    private final DilProxyConfig config;
    private final String proxyListenRoute;


    public ProxyRouteBuilder(DilProxyConfig config) {
        this.config = config;
        proxyListenRoute = String.format("jetty:%s/proxy?matchOnUriPrefix=true", config.getHostname());
    }

    @Override
    public void configure() throws Exception {
        if (config.useCompression()) {
            from(proxyListenRoute)
                    .process(new ProxyRequestProcessor())
                    .unmarshal()
                    .gzip()
                    .removeHeaders("CamelHttp*")
                    .toD("${header.path}" + "?bridgeEndpoint=true")
                    .process(new HttpResponseProcessor())
                    .marshal()
                    .gzip();
        } else {
            from(proxyListenRoute)
                    .process(new ProxyRequestProcessor())
                    .removeHeaders("CamelHttp*")
                    .toD("${header.path}" + "?bridgeEndpoint=true")
                    .process(new HttpResponseProcessor());
        }
    }
}
