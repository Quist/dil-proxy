package routing.protocols;

import config.DilProxyConfig;
import org.apache.camel.builder.RouteBuilder;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

public class MqttRouteFactoryTest {

    @Test
    public void testCreate() {
        DilProxyConfig dilProxyConfig = mock(DilProxyConfig.class);
        MqttRouteFactory mqttRouteFactory = new MqttRouteFactory(dilProxyConfig);

        RouteBuilder routeBuilder = mqttRouteFactory.create();
        assertThat(routeBuilder, is(notNullValue()));
    }
}