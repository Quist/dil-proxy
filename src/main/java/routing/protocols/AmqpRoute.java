package routing.protocols;

import config.DilProxyConfig;
import org.apache.camel.builder.RouteBuilder;
import processors.AmqpRequestProcessor;
import processors.WebServiceResponseProcessor;
import routing.RouteProcessorContainer;
import routing.routes.CamelProxyRoute;

public class AmqpRoute implements DilRouteBuilder {

    private final DilProxyConfig config;

    public AmqpRoute(DilProxyConfig config) {
        this.config = config;
    }

    @Override
    public String getToUri() {
        return String.format("amqp:queue:" + config.getAmqpConfig().getProduceQueue());
    }

    @Override
    public String getListenUri() {
        return "amqp:queue:" + config.getAmqpConfig().getConsumeQueue();
    }

    @Override
    public RouteBuilder create() {
        RouteProcessorContainer routeProcessorContainer = new RouteProcessorContainer(new AmqpRequestProcessor(), new WebServiceResponseProcessor());
        return new CamelProxyRoute(config, routeProcessorContainer, getListenUri());
    }
}
