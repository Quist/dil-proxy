package routes.webservice;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import config.AmqpConfig;
import config.DilProxyConfig;
import org.apache.camel.builder.RouteBuilder;
import org.junit.Test;
import proxy.Protocol;
import routes.proxie.protocols.AmqpRouteFactory;
import routes.proxie.protocols.ProtocolFactory;

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
        WebServiceRouteFactory factory = new WebServiceRouteFactory();
        ProtocolFactory protocolFactory = new AmqpRouteFactory(config);

        when(config.getProtocol()).thenReturn(Protocol.AMQP);
        when(config.getAmqpConfig()).thenReturn(amqpConfig);
        when(amqpConfig.getConsumeQueue()).thenReturn("1001");

        RouteBuilder route = factory.createWebServiceRouteBuilder(config, protocolFactory);

        assertThat(route, is(notNullValue()));
    }

}