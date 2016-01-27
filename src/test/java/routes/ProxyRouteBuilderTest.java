package routes;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigValueFactory;
import config.DilProxyConfig;
import org.apache.camel.Processor;
import org.hamcrest.CoreMatchers;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.apache.camel.model.RouteDefinition;
import org.junit.Test;
import processors.ProxyRequestProcessor;

import java.util.List;

import static org.hamcrest.Matchers.isA;
import static org.junit.Assert.*;

public class ProxyRouteBuilderTest {

    @Test
    public void testConfigureAddsOneRoute() throws Exception {
        Config networkConfig = ConfigFactory.empty()
                .withValue("network.hostname", ConfigValueFactory.fromAnyRef("http://localhost:3001"))
                .withValue("network.proxyHostname", ConfigValueFactory.fromAnyRef("http://localhost:4001"))
                .withValue("proxy.useCompression", ConfigValueFactory.fromAnyRef(true));

        DilProxyConfig config = new DilProxyConfig(networkConfig);

        ProxyRouteBuilder proxyRouteBuilder = new ProxyRouteBuilder(config);
        proxyRouteBuilder.configure();

        assertEquals(1, proxyRouteBuilder.getRouteCollection().getRoutes().size());
    }

    @Test
    public void testConfigureAddsCorrectFrom() throws Exception {
        Config networkConfig = ConfigFactory.empty()
                .withValue("network.hostname", ConfigValueFactory.fromAnyRef("http://localhost:3001"))
                .withValue("network.proxyHostname", ConfigValueFactory.fromAnyRef("http://localhost:4001"))
                .withValue("proxy.useCompression", ConfigValueFactory.fromAnyRef(true));

        DilProxyConfig config = new DilProxyConfig(networkConfig);
        ProxyRouteBuilder proxyRouteBuilder = new ProxyRouteBuilder(config);
        proxyRouteBuilder.configure();

        List<RouteDefinition> routes = proxyRouteBuilder.getRouteCollection().getRoutes();

        assertEquals("Number of from inputs", 1, routes.get(0).getInputs().size());

        String expectedFrom = "jetty:http://localhost:3001/proxy?matchOnUriPrefix=true";
        assertEquals(expectedFrom, routes.get(0).getInputs().get(0).getEndpointUri());
    }

    @Test
    public void testConfigureAddProcessor() throws Exception {
        Config networkConfig = ConfigFactory.empty()
                .withValue("network.hostname", ConfigValueFactory.fromAnyRef("http://localhost:3001"))
                .withValue("network.proxyHostname", ConfigValueFactory.fromAnyRef("http://localhost:4001"))
                .withValue("proxy.useCompression", ConfigValueFactory.fromAnyRef(true));

        DilProxyConfig config = new DilProxyConfig(networkConfig);

        ProxyRouteBuilder spy = Mockito.spy(new ProxyRouteBuilder(config));
        spy.configure();

    }


}