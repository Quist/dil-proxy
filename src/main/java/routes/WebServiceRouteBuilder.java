package routes;


import config.AmqpConfig;
import config.DilProxyConfig;
import org.apache.camel.ExchangeTimedOutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import processors.HttpRequestProcessor;
import processors.ProxyResponseProcessor;
import org.apache.camel.builder.RouteBuilder;
import processors.TimeoutExceptionHandler;

import java.net.ConnectException;


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

        onException(ExchangeTimedOutException.class, ConnectException.class)
                .process(new TimeoutExceptionHandler())
                .handled(true);

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
                return constructAmqpToPath(config.getAmqpConfig());
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

    private String constructAmqpToPath(AmqpConfig amqpConfig) {
        return String.format("amqp:queue:" + amqpConfig.getProduceQueue());
    }
}
