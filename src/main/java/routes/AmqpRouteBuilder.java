package routes;

import config.AmqpConfig;
import config.DilProxyConfig;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangeTimedOutException;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import processors.AmqpRequestProcessor;
import processors.HttpResponseProcessor;
import processors.TimeoutExceptionHandler;

public class AmqpRouteBuilder extends RouteBuilder {

    private final DilProxyConfig config;

    public AmqpRouteBuilder(DilProxyConfig config) {
        this.config = config;
    }

    @Override
    public void configure() throws Exception {
        String fromPath = createFromPath(config.getAmqpConfig());

        onException(ExchangeTimedOutException.class)
                .process(new TimeoutExceptionHandler());

        if (config.useCompression()) {
            from(fromPath)
                    .process(new AmqpRequestProcessor())
                    .unmarshal()
                    .gzip()
                    .removeHeaders("CamelHttp*")
                    .toD("${header.path}" + "?bridgeEndpoint=true")
                    .process(new HttpResponseProcessor())
                    .marshal()
                    .gzip();
        } else {
            from(fromPath)
                    .process(new AmqpRequestProcessor())
                    .removeHeaders("CamelHttp*")
                    .toD("${header.path}" + "?bridgeEndpoint=true")
                    .process(new HttpResponseProcessor());
        }
    }

    private String createFromPath(AmqpConfig amqpConfig) {
        return "amqp:queue:" + amqpConfig.getConsumeQueue();
    }
}
