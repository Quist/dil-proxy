package routing.protocols;

import config.DilProxyConfig;
import org.apache.camel.builder.RouteBuilder;
import processors.MqttRequestProcessor;
import processors.WebServiceResponseProcessor;
import routing.RouteHandler;
import routing.builders.ProxyRouteBuilder;

public class MqttRoute implements DilRouteBuilder {

    private final DilProxyConfig config;

    public MqttRoute(DilProxyConfig config) {
        this.config = config;
    }

    @Override
    public String getToUri() {
        return "mqtt:" + config.getMqttConfig().getProduceQueue() +
                "?publishTopicName=" + config.getMqttConfig().getProduceQueue() + "?exchangePattern=InOut";
    }

    @Override
    public String getListenUri() {
        return "mqtt:" + config.getMqttConfig().getConsumeQueue() +
                "?subscribeTopicName=" + config.getMqttConfig().getConsumeQueue();
    }

    @Override
    public RouteBuilder create() {
        RouteHandler routeHandler = new RouteHandler(new MqttRequestProcessor(), new WebServiceResponseProcessor());
        return new ProxyRouteBuilder(config, routeHandler, getListenUri());
    }
}
