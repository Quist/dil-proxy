package routing.protocols;

import config.AmqpConfig;
import config.DilProxyConfig;

public class AmqpRoute extends DilRouteBuilder {

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

}
