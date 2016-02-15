package config;

import com.typesafe.config.Config;

public class CoapConfig {
    private final int listenPort;

    public CoapConfig(Config coap) {
        listenPort = Integer.parseInt(coap.getString("listenPort"));
    }

    public int getListenPort() {
        return listenPort;
    }
}
