package routing.webservice;

import config.AmqpConfig;
import config.DilProxyConfig;
import org.apache.camel.builder.RouteBuilder;
import org.junit.Test;
import proxy.Protocol;
import routing.WebServiceRouteFactory;
import routing.protocols.AmqpRouteFactory;
import routing.protocols.ProtocolFactory;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.Matchers.isNotNull;
import static org.mockito.Matchers.notNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class WebServiceRouteFactoryTest {

    @Test
    public void testCreateWebServiceRouteBuilder() throws Exception {
        DilProxyConfig config = mock(DilProxyConfig.class);
        AmqpConfig amqpConfig = mock(AmqpConfig.class);
        WebServiceRouteFactory factory = new WebServiceRouteFactory(config);
        ProtocolFactory protocolFactory = new AmqpRouteFactory(config);

        when(config.getProtocol()).thenReturn(Protocol.AMQP);
        when(config.getAmqpConfig()).thenReturn(amqpConfig);
        when(amqpConfig.getConsumeQueue()).thenReturn("1001");

        RouteBuilder route = factory.create(protocolFactory);

        assertThat(route, is(notNullValue()));
    }

}