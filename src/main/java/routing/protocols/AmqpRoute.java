package routing.protocols;

import config.AmqpConfig;

public class AmqpRoute extends DilRouteBuilder {

    private final AmqpConfig amqpConfig;

    public AmqpRoute(AmqpConfig config) {
        this.amqpConfig = config;
    }

    @Override
    public String getToUri() {
        return String.format("amqp:queue:" + amqpConfig.getProduceQueue());
    }

    @Override
    public String getListenUri() {
        return "amqp:queue:" + amqpConfig.getConsumeQueue();
    }

}
