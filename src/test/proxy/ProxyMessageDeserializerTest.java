package proxy;

import org.junit.Test;
import proxy.serializer.ProxyMessage;
import proxy.serializer.ProxyMessageDeserializer;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class ProxyMessageDeserializerTest {

    @Test
    public void testDeserializePathWithEmptyBody() throws Exception {
        ProxyMessageDeserializer deserializer = new ProxyMessageDeserializer();
        ProxyMessage proxyMessage = deserializer.deserialize("{\"path\":\"test\"}");

        assertThat(proxyMessage.getPath(), is("test"));
    }

    @Test
    public void testDeserializeBodyWithEmptyBody() throws Exception {
        ProxyMessageDeserializer deserializer = new ProxyMessageDeserializer();
        ProxyMessage proxyMessage = deserializer.deserialize("{\"path\":\"test\"}");

        assertThat(proxyMessage.getBody(), is(""));
    }

    @Test
    public void testDeserializePathWithBody() throws Exception {
        ProxyMessageDeserializer deserializer = new ProxyMessageDeserializer();
        ProxyMessage proxyMessage = deserializer.deserialize("{\"path\":\"test\"}examplebody");

        assertThat(proxyMessage.getPath(), is("test"));
    }

    @Test
    public void testDeserializeBodyWithBody() throws Exception {
        ProxyMessageDeserializer deserializer = new ProxyMessageDeserializer();
        ProxyMessage proxyMessage = deserializer.deserialize("{\"path\":\"test\"}examplebody");

        assertThat(proxyMessage.getBody(), is("examplebody"));
    }
}