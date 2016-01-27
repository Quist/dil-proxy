package routes;

import config.DilProxyConfig;
import org.apache.camel.builder.RouteBuilder;
import processors.AmqpRequestProcessor;
import processors.HttpResponseProcessor;

public class AmqpRouteBuilder extends RouteBuilder {
    private final DilProxyConfig config;

    public AmqpRouteBuilder(DilProxyConfig config) {
        this.config = config;
    }


    @Override
    public void configure() throws Exception {
        String fromPath = "amqp:queue:incoming";

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
                    .toD("jetty:http://localhost:4001/proxy")
                    .process(new HttpResponseProcessor());
        }
    }
}
