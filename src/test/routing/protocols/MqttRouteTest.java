package routing.protocols;

import config.DilProxyConfig;
import org.apache.camel.builder.RouteBuilder;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

public class MqttRouteTest {

    @Test
    public void testCreate() {
        DilProxyConfig dilProxyConfig = mock(DilProxyConfig.class);
        MqttRoute mqttRoute = new MqttRoute(dilProxyConfig);

        RouteBuilder routeBuilder = mqttRoute.create();
        assertThat(routeBuilder, is(notNullValue()));
    }
}