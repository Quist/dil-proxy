package routes;

import config.DilProxyConfig;
import processors.HttpResponseProcessor;
import processors.ProxyRequestProcessor;
import org.apache.camel.builder.RouteBuilder;
import processors.TimeoutExceptionHandler;
import java.net.ConnectException;

public class ProxyRouteBuilder extends RouteBuilder {

    private final DilProxyConfig config;

    public ProxyRouteBuilder(DilProxyConfig config) {
        this.config = config;
    }

    @Override
    public void configure() throws Exception {
        String proxyListenRoute = String.format("jetty:%s/proxy?matchOnUriPrefix=true", config.getHostname());

        onException(ConnectException.class)
                .process(new TimeoutExceptionHandler())
                .handled(true);

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
