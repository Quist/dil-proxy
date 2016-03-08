package config;

import com.typesafe.config.Config;
import proxy.Protocol;
import java.util.Optional;
import static proxy.Protocol.*;

public class DilProxyConfig {
    private final boolean useCompression;
    private final String hostname;
    private final String port;
    private final String targetProxyHostname;
    private final Protocol selectedProtocol;

    private final Optional<Long> timeout;

    private Optional<AmqpConfig> amqpConfig = Optional.empty();
    private final Optional<MqttConfig> mqttConfig = Optional.empty();

    public DilProxyConfig(Config config) {
        Config proxyConfig = config.getConfig("proxy");

        useCompression = proxyConfig.getBoolean("useCompression");
        port = proxyConfig.getString("port");
        targetProxyHostname = proxyConfig.getString("targetProxyHostname");
        hostname = proxyConfig.getString("hostname");

        if (proxyConfig.hasPath("timeout")) {
            timeout = Optional.of(proxyConfig.getLong("timeout"));
        } else {
            timeout = Optional.empty();
        }

        selectedProtocol = getProtocol(proxyConfig);
        setProtocolConfig(selectedProtocol, config);
    }

    public boolean useCompression() {
        return useCompression;
    }

    public String getHostname() {
        return hostname;
    }

    public String getPort() {
        return port;
    }

    public String getTargetProxyHostname() {
        return targetProxyHostname;
    }

    public Protocol getSelectedProtocol() {
        return selectedProtocol;
    }

    public AmqpConfig getAmqpConfig() {
        if ( ! amqpConfig.isPresent()) {
            throw  new IllegalArgumentException("No protocol configuration for AMQP");
        }
        return amqpConfig.get();
    }

    public MqttConfig getMqttConfig() {
        if ( ! mqttConfig.isPresent()) {
            throw  new IllegalArgumentException("No protocol configuration for MQTT");
        }
        return mqttConfig.get();
    }

    private Protocol getProtocol(Config proxyConfig) {
        switch (proxyConfig.getString("protocol").toLowerCase()) {
            case "amqp": {
                return AMQP;
            }
            case "http": {
                return HTTP;
            }
            case "mqtt": {
                return MQTT;
            }
            case "coap": {
                return COAP;
            }
            default: {
                throw  new IllegalArgumentException("Unsupported protocol: " + proxyConfig.getString("protocol"));
            }
        }
    }

    private void setProtocolConfig(Protocol protocol, Config config) {
        switch (protocol) {
            case AMQP: {
                this.amqpConfig = Optional.of(new AmqpConfig(config.getConfig("amqp")));
                break;
            }
        }
    }

    public Optional<Long> getTimeout() {
        return timeout;
    }
}
