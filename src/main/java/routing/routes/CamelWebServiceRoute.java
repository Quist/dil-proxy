package routing.routes;

import config.DilProxyConfig;
import org.apache.camel.ExchangeTimedOutException;
import org.apache.camel.builder.RouteBuilder;
import processors.ProxyPreprocessor;
import processors.TimeoutExceptionHandler;
import routing.RouteProcessorContainer;

import java.net.ConnectException;


public class CamelWebServiceRoute extends RouteBuilder {
    private final RouteProcessorContainer routeProcessorContainer;
    private final DilProxyConfig config;
    private final String toUri;

    public CamelWebServiceRoute(DilProxyConfig config, RouteProcessorContainer routeProcessorContainer, String toUri) {
        this.config = config;
        this.routeProcessorContainer = routeProcessorContainer;
        this.toUri = toUri;
    }

    @Override
    public void configure() throws Exception {
        String fromPath = getFromPath();

        setupExceptionHandling();

        if (config.useCompression()) {
            from(fromPath)
                    .process(routeProcessorContainer.getRequestProcessor())
                    .process(new ProxyPreprocessor())
                    .marshal()
                    .gzip()
                    .to(toUri)
                    .process(routeProcessorContainer.getResponseProcessor())
                    .unmarshal()
                    .gzip();
        } else {
            from(fromPath)
                    .process(routeProcessorContainer.getRequestProcessor())
                    .process(new ProxyPreprocessor())
                    .to(toUri)
                    .process(routeProcessorContainer.getResponseProcessor());
        }
    }

    private String getFromPath() {
        String hostname = String.format("%s:%s", config.getHostname(), config.getPort());
        return String.format("jetty:http://%s?matchOnUriPrefix=true", hostname);
    }

    private void setupExceptionHandling() {
        onException(ExchangeTimedOutException.class, ConnectException.class)
                .process(new TimeoutExceptionHandler())
                .handled(true);
    }
}
