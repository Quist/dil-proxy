package routes;

import com.typesafe.config.Config;
import processors.HttpRequestProcessor;
import processors.ProxyResponseProcessor;
import org.apache.camel.builder.RouteBuilder;

public class WebServiceRouteBuilder extends RouteBuilder {
    private final Config networkConfig;

    public WebServiceRouteBuilder(Config networkConfig) {
        this.networkConfig = networkConfig;
    }

    @Override
    public void configure() throws Exception {
        String fromPath = String.format("jetty:%s?matchOnUriPrefix=true", networkConfig.getString("hostname"));
        String toPath = String.format("jetty:%s/proxy?bridgeEndpoint=true", networkConfig.getString("proxyHostname"));
        from(fromPath).process(new HttpRequestProcessor())
                .to(toPath)
                .process(new ProxyResponseProcessor());
    }
}
