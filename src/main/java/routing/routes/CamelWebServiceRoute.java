package routing.routes;

import config.DilProxyConfig;
import org.apache.camel.ExchangeTimedOutException;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.http.common.HttpOperationFailedException;
import org.apache.camel.model.RouteDefinition;
import processors.ResponseProcessor;
import processors.TimeoutExceptionHandler;
import routing.protocols.DilRouteBuilder;

import java.net.ConnectException;


public class CamelWebServiceRoute extends RouteBuilder {
    private final DilProxyConfig config;
    private final DilRouteBuilder routeBuilder;

    public CamelWebServiceRoute(DilRouteBuilder routeBuilder, DilProxyConfig config) {
        this.config = config;
        this.routeBuilder = routeBuilder;
    }

    @Override
    public void configure() throws Exception {
        String fromPath = getFromPath();

        setupExceptionHandling();

        RouteDefinition routeDefinition = from(fromPath);
        for(Processor processor: routeBuilder.getPreProcessors()) {
            routeDefinition = routeDefinition.process(processor);
        }

        routeDefinition = routeDefinition.to(routeBuilder.getToUri());

        routeDefinition.process(new ResponseProcessor());

    }

    private String getFromPath() {
        String hostname = String.format("%s:%s", config.getHostname(), config.getPort());
        return String.format("jetty:http://%s?matchOnUriPrefix=true", hostname);
    }

    private void setupExceptionHandling() {
        onException(ExchangeTimedOutException.class, ConnectException.class, HttpOperationFailedException.class)
                .process(new TimeoutExceptionHandler())
                .handled(true);
    }
}
