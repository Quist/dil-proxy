package routes.proxie;

import config.DilProxyConfig;
import org.apache.camel.builder.RouteBuilder;
import processors.TimeoutExceptionHandler;
import routes.RouteHandler;

import java.net.ConnectException;

public class ProxyRouteBuilder extends RouteBuilder {

    private final DilProxyConfig config;
    private final RouteHandler routeHandler;
    private final String listenUri;

    public ProxyRouteBuilder(DilProxyConfig config, RouteHandler routeHandler, String listenUri) {
        this.config = config;
        this.routeHandler = routeHandler;
        this.listenUri = listenUri;
    }

    @Override
    public void configure() throws Exception {

        setupExceptionHandling();

        if (config.useCompression()) {
            from(listenUri)
                    .process(routeHandler.getRequestProcessor())
                    .unmarshal()
                    .gzip()
                    .removeHeaders("CamelHttp*")
                    .toD("${header.path}" + "?bridgeEndpoint=true")
                    .process(routeHandler.getResponseProcessor())
                    .marshal()
                    .gzip();
        } else {
            from(listenUri)
                    .process(routeHandler.getRequestProcessor())
                    .removeHeaders("CamelHttp*")
                    .toD("${header.path}" + "?bridgeEndpoint=true")
                    .process(routeHandler.getResponseProcessor());
        }
    }

    private void setupExceptionHandling() {
        onException(ConnectException.class)
                .process(new TimeoutExceptionHandler())
                .handled(true);
    }
}
