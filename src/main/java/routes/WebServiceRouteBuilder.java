package routes;


import config.DilProxyConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import processors.HttpRequestProcessor;
import processors.ProxyResponseProcessor;
import org.apache.camel.builder.RouteBuilder;



public class WebServiceRouteBuilder extends RouteBuilder {
    final Logger logger = LoggerFactory.getLogger(WebServiceRouteBuilder.class);

    private final HttpRequestProcessor httpRequestProcessor;
    private final ProxyResponseProcessor proxyResponseProcessor;
    private final DilProxyConfig config;

    public WebServiceRouteBuilder(DilProxyConfig config, HttpRequestProcessor httpRequestProcessor,
                                  ProxyResponseProcessor proxyResponseProcessor) {
        this.config = config;
        this.httpRequestProcessor = httpRequestProcessor;
        this.proxyResponseProcessor = proxyResponseProcessor;
    }

    @Override
    public void configure() throws Exception {
        String fromPath = String.format("jetty:%s?matchOnUriPrefix=true", config.getHostname());
        String toPath = createToPath();

        if (config.useCompression()) {
            from(fromPath)
                    .process(httpRequestProcessor)
                    .marshal()
                    .gzip()
                    .to(toPath)
                    .process(proxyResponseProcessor)
                    .unmarshal()
                    .gzip();
        } else {
            from(fromPath)
                    .process(httpRequestProcessor)
                    .to(toPath)
                    .process(proxyResponseProcessor);
        }
    }

    private String createToPath() {
        switch (config.getProtocol()) {
            case AMQP:
                return constructAmqpToPath();
            case HTTP:
                return constructHttpToPath();
            default:
                logger.error("No path configuration for: " + config.getProtocol());
                throw new IllegalArgumentException("No path configuration for: " + config.getProtocol());
        }
    }

    private String constructHttpToPath() {
        return String.format("jetty:%s/proxy?bridgeEndpoint=true", config.getProxyHostname());
    }

    private String constructAmqpToPath() {
        String toPath = String.format("amqp:topic:notify");
        return toPath;
    }
}
