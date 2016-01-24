package routes;

import com.typesafe.config.Config;
import org.apache.camel.Endpoint;
import processors.HttpResponseProcessor;
import processors.ProxyRequestProcessor;
import org.apache.camel.builder.RouteBuilder;

public class ProxyRouteBuilder extends RouteBuilder {

    private final Config networkConfig;

    public ProxyRouteBuilder(Config networkConfig) {
        this.networkConfig = networkConfig;
    }

    @Override
    public void configure() throws Exception {
        final String proxyListenRoute = String.format("jetty:%s/proxy?matchOnUriPrefix=true", networkConfig.getString("hostname"));

        from(proxyListenRoute)
                .process(new ProxyRequestProcessor())
                .removeHeaders("CamelHttp*")
                .toD("${header.path}" + "?bridgeEndpoint=true")
                .process(new HttpResponseProcessor());
    }
}
