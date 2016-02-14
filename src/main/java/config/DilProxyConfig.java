package config;

import com.typesafe.config.Config;
import proxy.Protocol;

public class DilProxyConfig {
    private final boolean useCompression;
    private final String hostname;
    private final String proxyHostname;
    private final Protocol protocol;
    private final AmqpConfig amqpConfig;
    private final MqttConfig mqttConfig;

    public DilProxyConfig(Config config) {
        Config proxyConfig = config.getConfig("proxy");
        Config networkConfig = config.getConfig("network");

        useCompression = proxyConfig.getBoolean("useCompression");
        proxyHostname = networkConfig.getString("proxyHostname");
        hostname = networkConfig.getString("hostname");
        protocol = setProtocol(proxyConfig.getString("protocol"));

        this.amqpConfig = new AmqpConfig(config.getConfig("amqp"));
        this.mqttConfig = new MqttConfig(config.getConfig("mqtt"));
    }

    public boolean useCompression() {
        return useCompression;
    }

    public String getHostname() {
        return hostname;
    }

    public String getProxyHostname() {
        return proxyHostname;
    }

    public Protocol getProtocol() {
        return protocol;
    }

    public AmqpConfig getAmqpConfig() {
        return amqpConfig;
    }

    public MqttConfig getMqttConfig() {
        return mqttConfig;
    }

    private Protocol setProtocol(String protocol) {
        switch (protocol.toLowerCase()) {
            case "amqp": {
                return Protocol.AMQP;
            }
            case "http": {
                return Protocol.HTTP;
            }
            case "mqtt": {
                return Protocol.MQTT;
            }
            case "coap": {
                return Protocol.COAP;
            }
            default: {
                throw  new IllegalArgumentException("Unsupported protocol: " + protocol);
            }
        }
    }
}
