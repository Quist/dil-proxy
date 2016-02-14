package proxy;

import org.junit.Test;
import proxy.serializer.ProxyPayload;
import proxy.serializer.ProxyPayloadDeserializer;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class ProxyPayloadDeserializerTest {

    @Test
    public void testDeserializePathWithEmptyBody() throws Exception {
        ProxyPayloadDeserializer deserializer = new ProxyPayloadDeserializer();
        ProxyPayload proxyPayload = deserializer.deserialize("{\"path\":\"test\"}");

        assertThat(proxyPayload.getPath(), is("test"));
    }

    @Test
    public void testDeserializeBodyWithEmptyBody() throws Exception {
        ProxyPayloadDeserializer deserializer = new ProxyPayloadDeserializer();
        ProxyPayload proxyPayload = deserializer.deserialize("{\"path\":\"test\"}");

        assertThat(proxyPayload.getBody(), is(""));
    }

    @Test
    public void testDeserializePathWithBody() throws Exception {
        ProxyPayloadDeserializer deserializer = new ProxyPayloadDeserializer();
        ProxyPayload proxyPayload = deserializer.deserialize("{\"path\":\"test\"}examplebody");

        assertThat(proxyPayload.getPath(), is("test"));
    }

    @Test
    public void testDeserializeBodyWithBody() throws Exception {
        ProxyPayloadDeserializer deserializer = new ProxyPayloadDeserializer();
        ProxyPayload proxyPayload = deserializer.deserialize("{\"path\":\"test\"}examplebody");

        assertThat(proxyPayload.getBody(), is("examplebody"));
    }
}