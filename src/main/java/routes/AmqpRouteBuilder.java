package routes;

import config.DilProxyConfig;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangeTimedOutException;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import processors.AmqpRequestProcessor;
import processors.HttpResponseProcessor;

public class AmqpRouteBuilder extends RouteBuilder {

    final Logger logger = LoggerFactory.getLogger(AmqpRouteBuilder.class);
    private final DilProxyConfig config;

    public AmqpRouteBuilder(DilProxyConfig config) {
        this.config = config;
    }


    @Override
    public void configure() throws Exception {
        String fromPath = "amqp:queue:incoming";

        onException(ExchangeTimedOutException.class)
        .process(new Processor() {
            @Override
            public void process(Exchange exchange) throws Exception {
                logger.error("Exception happened");
            }
        });

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
                    .errorHandler(noErrorHandler())
                    .process(new AmqpRequestProcessor())
                    .removeHeaders("CamelHttp*")
                    .onException(org.apache.camel.ExchangeTimedOutException.class)
                    .process(new Processor() {
                        @Override
                        public void process(Exchange exchange) throws Exception {
                            logger.error("Exception happened!!!!!!!!!!!!!!!!!!!!");
                        }
                    })
                    .process(new HttpResponseProcessor());
        }
    }
}
