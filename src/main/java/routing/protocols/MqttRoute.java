package routing.protocols;

import config.DilProxyConfig;

public class MqttRoute extends DilRouteBuilder {

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

}
