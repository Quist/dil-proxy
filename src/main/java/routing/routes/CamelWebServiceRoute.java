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
import processors.WebServiceResponseProcessor;
import processors.errorhandlers.TimeoutExceptionHandler;
import proxy.serializer.ProxyResponseSerializer;
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
        String fromPath = getFromPath();

        setupExceptionHandling();
        setupErrorHandler();

        ProcessorDefinition<RouteDefinition> routeDefinition = from(fromPath);
        for(Processor processor: routeBuilder.getPreProcessors()) {
            routeDefinition = routeDefinition.process(processor);
        }

        if (config.useCompression()) {
            logger.info("Using GZIP compression");
            routeDefinition = routeDefinition.marshal().gzip();
        }

        routeDefinition = routeDefinition.to(routeBuilder.getToUri());
        routeDefinition.process(new WebServiceResponseProcessor(new ProxyResponseSerializer()));

        if (config.useCompression()) {
            routeDefinition.unmarshal().gzip();
        }
    }

    private void setupErrorHandler() {

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
    }

    private String getFromPath() {
        String hostname = String.format("%s:%s", config.getHostname(), config.getPort());
        return String.format("jetty:http://%s?matchOnUriPrefix=true", hostname);
    }

    private void setupExceptionHandling() {
        onException(HttpOperationFailedException.class)
                .process(new TimeoutExceptionHandler())
                .handled(true);
    }
}
