package proxy;

import config.AmqpConfig;
import config.DilProxyConfig;
import org.apache.camel.impl.DefaultCamelContext;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ProxyTest {

    @Test
    public void testStartWithAmqp() throws Exception {
        DilProxyConfig config = mock(DilProxyConfig.class);
        AmqpConfig amqpConfig = mock(AmqpConfig.class);
        DefaultCamelContext camelContext = mock(DefaultCamelContext.class);
        CamelComponentInitializer componentInitializer = mock(CamelComponentInitializer.class);

        when(config.getProtocol()).thenReturn(Protocol.AMQP);
        when(config.getAmqpConfig()).thenReturn(amqpConfig);
        when(amqpConfig.getConsumeQueue()).thenReturn("1001");

        Proxy proxy = new Proxy(config, camelContext, componentInitializer);
        proxy.start();
    }

}