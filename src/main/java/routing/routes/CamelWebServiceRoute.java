package routing.routes;

import config.DilProxyConfig;
import org.apache.camel.LoggingLevel;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.http.common.HttpOperationFailedException;
import org.apache.camel.model.ProcessorDefinition;
import org.apache.camel.model.RouteDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import processors.ProxyResponsePostProcessor;
import processors.errorhandlers.TimeoutExceptionHandler;
import routing.protocols.DilRouteBuilder;


public class CamelWebServiceRoute extends RouteBuilder {
    private final Logger logger = LoggerFactory.getLogger(CamelWebServiceRoute.class);

    private final DilProxyConfig config;
    private final DilRouteBuilder routeBuilder;

    public CamelWebServiceRoute(DilRouteBuilder routeBuilder, DilProxyConfig config) {
        this.config = config;
        this.routeBuilder = routeBuilder;
    }

    @Override
    public void configure() throws Exception {
        setupExceptionHandling();

        ProcessorDefinition<RouteDefinition> routeDefinition = from(getListenUri());
        for(Processor processor: routeBuilder.getPreProcessors()) {
            routeDefinition = routeDefinition.process(processor);
        }

        if (config.useCompression()) {
            logger.info("Using GZIP compression");
            routeDefinition = routeDefinition.marshal().gzip();
        }

        routeDefinition = routeDefinition.to(routeBuilder.getToUri());

        if (config.useCompression()) {
            routeDefinition.unmarshal().gzip();
        }

        routeDefinition.process(new ProxyResponsePostProcessor());
    }

    private String getListenUri() {
        String hostname = String.format("%s:%s", config.getHostname(), config.getPort());
        return String.format("jetty:http://%s?matchOnUriPrefix=true", hostname);
    }

    private void setupExceptionHandling() {
        errorHandler(deadLetterChannel("mock:death")
                        .maximumRedeliveries(-1)
                        .redeliveryDelay(config.getRedeliverDelay())
                        .logHandled(true)
                        .logNewException(true)
                        .logExhaustedMessageHistory(true)
                        .logExhausted(true)
                        .logStackTrace(true)
                        .loggingLevel(LoggingLevel.WARN)
                        .logRetryStackTrace(true)
        );

        onException(HttpOperationFailedException.class)
                .process(new TimeoutExceptionHandler())
                .handled(true);
    }
}
