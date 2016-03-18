package proxy;

import org.junit.Test;
import proxy.serializer.ProxyHeaderDeserializer;
import proxy.serializer.ProxyMessage;
import proxy.serializer.ProxyRequestDeserializer;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class ProxyMessageDeserializerTest {

    @Test
    public void testDeserializePathWithEmptyBody() throws Exception {
        ProxyRequestDeserializer deserializer = new ProxyRequestDeserializer(new ProxyHeaderDeserializer());
        ProxyMessage proxyMessage = deserializer.deserialize("{\"path\":\"test\"}");

        assertThat(proxyMessage.getPath(), is("test"));
    }

    @Test
    public void testDeserializeBodyWithEmptyBody() throws Exception {
        ProxyRequestDeserializer deserializer = new ProxyRequestDeserializer(new ProxyHeaderDeserializer());
        ProxyMessage proxyMessage = deserializer.deserialize("{\"path\":\"test\"}");

        assertThat(proxyMessage.getBody(), is(""));
    }

    @Test
    public void testDeserializePathWithBody() throws Exception {
        ProxyRequestDeserializer deserializer = new ProxyRequestDeserializer(new ProxyHeaderDeserializer());
        ProxyMessage proxyMessage = deserializer.deserialize("{\"path\":\"test\"}examplebody");

        assertThat(proxyMessage.getPath(), is("test"));
    }

    @Test
    public void testDeserializeBodyWithBody() throws Exception {
        ProxyRequestDeserializer deserializer = new ProxyRequestDeserializer(new ProxyHeaderDeserializer());
        ProxyMessage proxyMessage = deserializer.deserialize("{\"path\":\"test\"}examplebody");

        assertThat(proxyMessage.getBody(), is("examplebody"));
    }
}