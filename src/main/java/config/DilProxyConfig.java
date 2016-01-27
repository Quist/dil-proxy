package config;

import com.typesafe.config.Config;
import proxy.Protocol;

public class DilProxyConfig {
    private final boolean useCompression;
    private final String hostname;
    private final String proxyHostname;
    private final Protocol protocol;

    public DilProxyConfig(Config config) {
        Config proxyConfig = config.getConfig("proxy");
        Config networkConfig = config.getConfig("network");

        useCompression = proxyConfig.getBoolean("useCompression");

        proxyHostname = networkConfig.getString("proxyHostname");
        hostname = networkConfig.getString("hostname");

        protocol = Protocol.AMQP;
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

}
