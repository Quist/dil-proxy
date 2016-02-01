package config;

import com.typesafe.config.Config;

public class AmqpConfig {
    private final String brokerConnectionUri;
    private final String produceQueue;
    private final String consumeQueue;

    public AmqpConfig(Config amqpConfig) {
        brokerConnectionUri = amqpConfig.getString("brokerConnectionUri");
        produceQueue = amqpConfig.getString("produceQueue");
        consumeQueue = amqpConfig.getString("consumeQueue");
    }

    public String getBrokerConnectionUri() {
        return brokerConnectionUri;
    }

    public String getProduceQueue() {
        return produceQueue;
    }

    public String getConsumeQueue() {
        return consumeQueue;
    }
}
