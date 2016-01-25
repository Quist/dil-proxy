package routes;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigValueFactory;
import org.apache.camel.model.RouteDefinition;
import org.junit.Test;
import processors.HttpRequestProcessor;
import processors.ProxyResponseProcessor;

import static org.mockito.Mockito.*;

public class WebServiceRouteBuilderTest {

    @Test
    public void testConfigure() throws Exception {
        Config networkConfig = ConfigFactory.empty()
                .withValue("hostname", ConfigValueFactory.fromAnyRef("myhostname"))
                .withValue("proxyHostname", ConfigValueFactory.fromAnyRef("proxyhostname"));

        HttpRequestProcessor httpRequestProcessor = mock(HttpRequestProcessor.class);
        ProxyResponseProcessor proxyResponseProcessor = mock(ProxyResponseProcessor.class);
        RouteDefinition routeDefinition = mock(RouteDefinition.class);
        RouteDefinition routeDefinition1 = mock(RouteDefinition.class);
        RouteDefinition routeDefinition2 = mock(RouteDefinition.class);


        WebServiceRouteBuilder routeBuilder = spy(new WebServiceRouteBuilder(networkConfig, httpRequestProcessor, proxyResponseProcessor));

        String expectedFromPath = "jetty:myhostname?matchOnUriPrefix=true";
        String expectedToPath = "jetty:proxyhostname/proxy?bridgeEndpoint=true";

        when(routeBuilder.from(expectedFromPath)).thenReturn(routeDefinition);
        when(routeDefinition.process(httpRequestProcessor)).thenReturn(routeDefinition1);
        when(routeDefinition1.to(expectedToPath)).thenReturn(routeDefinition2);

        routeBuilder.configure();

        verify(routeBuilder, times(2)).from(expectedFromPath);
        verify(routeDefinition).process(httpRequestProcessor);
        verify(routeDefinition1).to("jetty:proxyhostname/proxy?bridgeEndpoint=true");
        verify(routeDefinition2).process(proxyResponseProcessor);
    }

}