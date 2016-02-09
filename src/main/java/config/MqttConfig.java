package config;

import com.typesafe.config.Config;

public class MqttConfig {

    private final String consumeQueue;
    private final String produceQueue;

    public MqttConfig(Config mqtt) {
        consumeQueue = mqtt.getString("consumeQueue");
        produceQueue = mqtt.getString("produceQueue");
    }

    public String getConsumeQueue() {
        return consumeQueue;
    }

    public String getProduceQueue() {
        return produceQueue;
    }
}
