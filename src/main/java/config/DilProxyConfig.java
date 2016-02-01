package config;

import com.typesafe.config.Config;
import proxy.Protocol;

public class DilProxyConfig {
    private final boolean useCompression;
    private final String hostname;
    private final String proxyHostname;
    private final Protocol protocol;
    private final AmqpConfig amqpConfig;

    public DilProxyConfig(Config config) {
        Config proxyConfig = config.getConfig("proxy");
        Config networkConfig = config.getConfig("network");

        useCompression = proxyConfig.getBoolean("useCompression");
        proxyHostname = networkConfig.getString("proxyHostname");
        hostname = networkConfig.getString("hostname");
        protocol = setProtocol(proxyConfig.getString("protocol"));

        this.amqpConfig = new AmqpConfig(config.getConfig("amqp"));
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

    private Protocol setProtocol(String protocol) {
        switch (protocol.toLowerCase()) {
            case "amqp": {
                return Protocol.AMQP;
            }
            case "http": {
                return Protocol.HTTP;
            }
            default: {
                throw  new IllegalArgumentException("Unsupported protocol: " + protocol);
            }
        }
    }
}
