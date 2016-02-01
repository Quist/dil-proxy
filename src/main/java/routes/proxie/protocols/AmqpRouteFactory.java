package routes.proxie.protocols;

import config.DilProxyConfig;
import org.apache.camel.builder.RouteBuilder;
import processors.AmqpRequestProcessor;
import processors.WebServiceResponseProcessor;
import routes.RouteHandler;
import routes.proxie.ProxyRouteBuilder;

public class AmqpRouteFactory implements ProtocolFactory {

    private final DilProxyConfig config;

    public AmqpRouteFactory(DilProxyConfig config) {
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
        RouteHandler routeHandler = new RouteHandler(new AmqpRequestProcessor(), new WebServiceResponseProcessor());
        return new ProxyRouteBuilder(config, routeHandler, getListenUri());
    }
}
