package routing.protocols;

import config.DilProxyConfig;
import org.apache.camel.builder.RouteBuilder;
import processors.protocols.MqttRequest;
import processors.WebServiceResponseProcessor;
import routing.RouteProcessorContainer;
import routing.routes.CamelProxyRoute;

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
        RouteProcessorContainer routeProcessorContainer = new RouteProcessorContainer(new MqttRequest(), new WebServiceResponseProcessor());
        return new CamelProxyRoute(config, routeProcessorContainer, getListenUri());
    }
}
