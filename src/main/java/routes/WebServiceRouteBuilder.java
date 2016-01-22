package routes;

import processors.HttpRequestProcessor;
import processors.ProxyResponseProcessor;
import org.apache.camel.builder.RouteBuilder;

public class WebServiceRouteBuilder extends RouteBuilder {
    private final String host;
    private final String proxyAddress;

    public WebServiceRouteBuilder(String host, String proxyAddress) {
        this.host = host;
        this.proxyAddress = proxyAddress;
    }

    @Override
    public void configure() throws Exception {
        String fromPath = String.format("jetty:%s?matchOnUriPrefix=true", host);
        String toPath = String.format("jetty:%s/proxy?bridgeEndpoint=true", proxyAddress);
        from(fromPath).process(new HttpRequestProcessor())
                .to(toPath)
                .process(new ProxyResponseProcessor());
    }
}
