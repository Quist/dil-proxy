package routes.webservice;

import config.DilProxyConfig;
import org.apache.camel.ExchangeTimedOutException;
import org.apache.camel.builder.RouteBuilder;
import processors.TimeoutExceptionHandler;
import routes.RouteHandler;

import java.net.ConnectException;


public class WebServiceRouteBuilder extends RouteBuilder {
    private final RouteHandler routeHandler;
    private final DilProxyConfig config;
    private final String toUri;

    public WebServiceRouteBuilder(DilProxyConfig config, RouteHandler routeHandler, String toUri) {
        this.config = config;
        this.routeHandler = routeHandler;
        this.toUri = toUri;
    }

    @Override
    public void configure() throws Exception {
        String fromPath = getFromPath();

        setupExceptionHandling();

        if (config.useCompression()) {
            from(fromPath)
                    .process(routeHandler.getRequestProcessor())
                    .marshal()
                    .gzip()
                    .to(toUri)
                    .process(routeHandler.getResponseProcessor())
                    .unmarshal()
                    .gzip();
        } else {
            from(fromPath)
                    .process(routeHandler.getRequestProcessor())
                    .to(toUri)
                    .process(routeHandler.getResponseProcessor());
        }
    }

    private String getFromPath() {
        return String.format("jetty:%s?matchOnUriPrefix=true", config.getHostname());
    }

    private void setupExceptionHandling() {
        onException(ExchangeTimedOutException.class, ConnectException.class)
                .process(new TimeoutExceptionHandler())
                .handled(true);
    }
}
