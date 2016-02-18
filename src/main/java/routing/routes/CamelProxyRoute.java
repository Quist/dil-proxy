package routing.routes;

import config.DilProxyConfig;
import org.apache.camel.builder.RouteBuilder;
import processors.ProxyPostProcessor;
import processors.TimeoutExceptionHandler;
import routing.RouteProcessorContainer;

import java.net.ConnectException;

public class CamelProxyRoute extends RouteBuilder {

    private final DilProxyConfig config;
    private final RouteProcessorContainer routeProcessorContainer;
    private final String listenUri;

    public CamelProxyRoute(DilProxyConfig config, RouteProcessorContainer routeProcessorContainer, String listenUri) {
        this.config = config;
        this.routeProcessorContainer = routeProcessorContainer;
        this.listenUri = listenUri;
    }

    @Override
    public void configure() throws Exception {

        setupExceptionHandling();

        if (config.useCompression()) {
            from(listenUri)
                    .process(routeProcessorContainer.getRequestProcessor())
                    .unmarshal()
                    .gzip()
                    .removeHeaders("CamelHttp*")
                    .toD("${header.path}" + "?bridgeEndpoint=true")
                    .process(routeProcessorContainer.getResponseProcessor())
                    .marshal()
                    .gzip();
        } else {
            from(listenUri)
                    .process(routeProcessorContainer.getRequestProcessor())
                    .process(new ProxyPostProcessor())
                    .toD("${header.path}" + "?bridgeEndpoint=true")
                    .process(routeProcessorContainer.getResponseProcessor());
        }
    }

    private void setupExceptionHandling() {
        onException(ConnectException.class)
                .process(new TimeoutExceptionHandler())
                .handled(true);
    }
}
