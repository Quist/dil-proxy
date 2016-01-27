package routes;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import compressor.Compressor;
import config.DilProxyConfig;
import processors.HttpRequestProcessor;
import processors.ProxyResponseProcessor;
import org.apache.camel.builder.RouteBuilder;

public class WebServiceRouteBuilder extends RouteBuilder {
    private final HttpRequestProcessor httpRequestProcessor;
    private final ProxyResponseProcessor proxyResponseProcessor;
    private final DilProxyConfig config;

    public WebServiceRouteBuilder(DilProxyConfig config, HttpRequestProcessor httpRequestProcessor,
                                  ProxyResponseProcessor proxyResponseProcessor) {
        this.config = config;
        this.httpRequestProcessor = httpRequestProcessor;
        this.proxyResponseProcessor = proxyResponseProcessor;
    }

    @Override
    public void configure() throws Exception {
        String fromPath = String.format("jetty:%s?matchOnUriPrefix=true", config.getHostname());
        String toPath = String.format("jetty:%s/proxy?bridgeEndpoint=true", config.getProxyHostname());

        if (config.useCompression()) {
            from(fromPath)
                    .process(httpRequestProcessor)
                    .marshal()
                    .gzip()
                    .to(toPath)
                    .process(proxyResponseProcessor)
                    .unmarshal()
                    .gzip();
        } else {
            from(fromPath)
                    .process(httpRequestProcessor)
                    .to(toPath)
                    .process(proxyResponseProcessor);
        }
    }
}
