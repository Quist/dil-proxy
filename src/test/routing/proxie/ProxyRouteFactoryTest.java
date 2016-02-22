package routing.proxie;

import config.DilProxyConfig;
import org.apache.camel.builder.RouteBuilder;
import org.junit.Test;
import proxy.Protocol;
import routing.ProxyRouteFactory;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ProxyRouteFactoryTest {

    @Test
    public void testFactorySupportsMqtt() {
        ProxyRouteFactory proxyRouteFactory = new ProxyRouteFactory();
        DilProxyConfig config = mock(DilProxyConfig.class);
        when(config.getSelectedProtocol()).thenReturn(Protocol.MQTT);

        RouteBuilder routeBuilder = proxyRouteFactory.createProxyRouteBuilder(config);
        assertThat(routeBuilder, is(notNullValue()));
    }
}