package routing.routes;

import config.DilProxyConfig;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.ProcessorDefinition;
import org.apache.camel.model.RouteDefinition;
import processors.errorhandlers.ServerErrorHandler;
import processors.errorhandlers.TimeoutExceptionHandler;
import processors.ProxyResponsePreProcessor;
import proxy.serializer.ProxyResponseSerializer;
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

        ProcessorDefinition<RouteDefinition> routeDefinition = from(routeBuilder.getListenUri());

        if (config.useCompression()) {
            routeDefinition = routeDefinition.unmarshal().gzip();
        }

        for (Processor processor: routeBuilder.getPostProcessors()) {
            routeDefinition = routeDefinition.process(processor);
        }
        routeDefinition = routeDefinition.toD("${header.Dil-Path}" + "?bridgeEndpoint=true&throwExceptionOnFailure=false")
                .process(new ProxyResponsePreProcessor(new ProxyResponseSerializer()));

        if (config.useCompression()) {
            routeDefinition.marshal().gzip();
        }
    }

    private void setupExceptionHandling() {
        onException(ConnectException.class)
                .process(new TimeoutExceptionHandler())
                .handled(true);

        onException(Exception.class)
                .process(new ServerErrorHandler());
    }
}
