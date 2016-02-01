package proxy;

import config.DilProxyConfig;
import org.apache.camel.builder.RouteBuilder;
import routes.proxie.protocols.ProtocolFactory;

public class MqttRouteFactory implements ProtocolFactory {

    public MqttRouteFactory(DilProxyConfig config) {

    }

    @Override
    public String getToUri() {
        return null;
    }

    @Override
    public String getListenUri() {
        return null;
    }

    @Override
    public RouteBuilder create() {
        return null;
    }
}
