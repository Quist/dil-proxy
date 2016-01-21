package main.java.proxy;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

public class ProxyRouteBuilder {

    private final String webserviceUri = "/";
    private final String proxyUri = "/proxy";
    private final String matchOnUriPrefix = "?matchOnUriPrefix=true";
    private final String bridgeEndpoint = "?bridgeEndpoint=true";


    void buildRoutes(int listenPort, String oppositeProxyHost, DefaultCamelContext context) {
        String host = String.format("http://0.0.0.0:%d", listenPort);

        final String webServiceListenRoute = String.format("jetty:%s%s%s", host, webserviceUri, matchOnUriPrefix);
        final String OppositeProxyRoute = String.format("jetty:%s%s%s", oppositeProxyHost, proxyUri, bridgeEndpoint);
        final String proxyListenRoute = String.format("jetty:%s%s%s", host, proxyUri, matchOnUriPrefix);

        try {
            context.addRoutes(new RouteBuilder() {
                public void configure() {
                    from(webServiceListenRoute).process(new IncomingWebServiceMessageProcessor())
                            .to(OppositeProxyRoute)
                    .process(new ProxyResponseProcessor());
                }
            });
            context.addRoutes(new RouteBuilder() {
                public void configure() {
                    from(proxyListenRoute)
                            .process(new IncomingProxyMessageProcessor())
                            .removeHeaders("CamelHttp*")
                            .toD("${header.path}" + bridgeEndpoint)
                            .process(new HttpResponseProcessor());
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
