package routing.protocols;

import config.AmqpConfig;
import config.DilProxyConfig;
import org.apache.camel.builder.RouteBuilder;
import processors.protocols.AmqpRequest;
import processors.WebServiceResponseProcessor;
import routing.RouteProcessorContainer;
import routing.routes.CamelProxyRoute;

public class AmqpRoute implements DilRouteBuilder {

    private final DilProxyConfig config;
    private final AmqpConfig amqpConfig;

    public AmqpRoute(DilProxyConfig config) {
        this.config = config;
        amqpConfig = config.getAmqpConfig();
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
        RouteProcessorContainer routeProcessorContainer = new RouteProcessorContainer(new AmqpRequest(), new WebServiceResponseProcessor());
        return new CamelProxyRoute(config, routeProcessorContainer, getListenUri());
    }
}
