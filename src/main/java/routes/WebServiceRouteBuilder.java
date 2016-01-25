package routes;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import compressor.Compressor;
import processors.HttpRequestProcessor;
import processors.ProxyResponseProcessor;
import org.apache.camel.builder.RouteBuilder;

public class WebServiceRouteBuilder extends RouteBuilder {
    private final Config networkConfig;
    private final HttpRequestProcessor httpRequestProcessor;
    private final ProxyResponseProcessor proxyResponseProcessor;

    public WebServiceRouteBuilder(Config networkConfig, HttpRequestProcessor httpRequestProcessor,
                                  ProxyResponseProcessor proxyResponseProcessor) {
        this.networkConfig = networkConfig;
        this.httpRequestProcessor = httpRequestProcessor;
        this.proxyResponseProcessor = proxyResponseProcessor;
    }

    @Override
    public void configure() throws Exception {
        String fromPath = String.format("jetty:%s?matchOnUriPrefix=true", networkConfig.getString("hostname"));
        String toPath = String.format("jetty:%s/proxy?bridgeEndpoint=true", networkConfig.getString("proxyHostname"));
        from(fromPath)
                .process(httpRequestProcessor)
                .to(toPath)
                .process(proxyResponseProcessor);
    }

}
