package config;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigValueFactory;
import org.junit.Test;
import proxy.Protocol;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class DilProxyConfigTest {

    @Test
    public void testGetProtocolWithAmqp() throws Exception {
        DilProxyConfig dilProxyConfig = new DilProxyConfig(ConfigFactory.empty()
                .withValue("proxy.useCompression", ConfigValueFactory.fromAnyRef(true))
                .withValue("proxy.protocol", ConfigValueFactory.fromAnyRef("amqp"))
                .withValue("network.hostname", ConfigValueFactory.fromAnyRef("random"))
                .withValue("network.proxyHostname", ConfigValueFactory.fromAnyRef("test"))
                .withValue("amqp.brokerConnectionUri", ConfigValueFactory.fromAnyRef("lol"))
                .withValue("amqp.consumeQueue", ConfigValueFactory.fromAnyRef("lol"))
                .withValue("amqp.produceQueue", ConfigValueFactory.fromAnyRef("lol"))
        );

        assertThat(dilProxyConfig.getProtocol(), is(Protocol.AMQP));
    }

    @Test
    public void testGetProtocolWithMqtt() throws Exception {
        DilProxyConfig dilProxyConfig = new DilProxyConfig(ConfigFactory.empty()
                .withValue("proxy.useCompression", ConfigValueFactory.fromAnyRef(true))
                .withValue("proxy.protocol", ConfigValueFactory.fromAnyRef("mqtt"))
                .withValue("network.hostname", ConfigValueFactory.fromAnyRef("random"))
                .withValue("network.proxyHostname", ConfigValueFactory.fromAnyRef("test"))
                .withValue("amqp.brokerConnectionUri", ConfigValueFactory.fromAnyRef("lol"))
                .withValue("amqp.consumeQueue", ConfigValueFactory.fromAnyRef("lol"))
                .withValue("amqp.produceQueue", ConfigValueFactory.fromAnyRef("lol"))
        );

        assertThat(dilProxyConfig.getProtocol(), is(Protocol.MQTT));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnsupportedProtocol() {
        DilProxyConfig dilProxyConfig = new DilProxyConfig(ConfigFactory.empty()
                .withValue("proxy.useCompression", ConfigValueFactory.fromAnyRef(true))
                .withValue("proxy.protocol", ConfigValueFactory.fromAnyRef("amqpsss"))
                .withValue("network.hostname", ConfigValueFactory.fromAnyRef("random"))
                .withValue("network.proxyHostname", ConfigValueFactory.fromAnyRef("test"))
                .withValue("amqp.brokerConnectionUri", ConfigValueFactory.fromAnyRef("lol"))
                .withValue("amqp.consumeQueue", ConfigValueFactory.fromAnyRef("lol"))
                .withValue("amqp.produceQueue", ConfigValueFactory.fromAnyRef("lol"))
        );
    }
}