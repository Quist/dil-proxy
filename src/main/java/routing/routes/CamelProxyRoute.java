package routing.routes;

import config.DilProxyConfig;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.RouteDefinition;
import processors.TimeoutExceptionHandler;
import processors.WebServiceResponseProcessor;
import routing.protocols.DilRouteBuilder;

import java.net.ConnectException;

public class CamelProxyRoute extends RouteBuilder {

    private final DilProxyConfig config;
    private final DilRouteBuilder routeBuilder;

    public CamelProxyRoute(DilRouteBuilder routeBuilder, DilProxyConfig config) {
        this.routeBuilder = routeBuilder;
        this.config = config;
    }

    @Override
    public void configure() throws Exception {

        setupExceptionHandling();

        RouteDefinition routeDefinition = from(routeBuilder.getListenUri());
        for (Processor processor: routeBuilder.getPreProcessors()) {
            routeDefinition = routeDefinition.process(processor);
        }
        routeDefinition.toD("${header.path}" + "?bridgeEndpoint=true")
                .process(new WebServiceResponseProcessor());
    }

    private void setupExceptionHandling() {
        onException(ConnectException.class)
                .process(new TimeoutExceptionHandler())
                .handled(true);
    }
}
