package proxy;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import compressor.Compressor;
import org.apache.camel.impl.DefaultCamelContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import processors.HttpRequestProcessor;
import processors.ProxyResponseProcessor;
import routes.ProxyRouteBuilder;
import routes.WebServiceRouteBuilder;


public class Proxy {

    private final Logger logger = LoggerFactory.getLogger(Proxy.class);
    private final DefaultCamelContext camelContext;
    private final Config config;

    public static void main(String args[]) {

        if (args.length < 2) {
            printUsage();
            System.exit(1);
        }

        Config proxyConfig = ConfigFactory.load();

        Proxy proxy = new Proxy(proxyConfig);
        proxy.start();
    }

    private static void printUsage() {
        System.out.println("Usage: proxy port oppositeProxyPort");
    }

    public Proxy(Config proxyConfig) {
        this.camelContext = new DefaultCamelContext();
        this.config = proxyConfig;
    }
    public void start() {
        Config networkConfig = config.getConfig("network");
        Config proxyConfig = config.getConfig("proxy");
        try {
            addRoutes(networkConfig, proxyConfig);
            camelContext.start();
            logger.info("Proxy started and listening on " + networkConfig.getString("hostname"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addRoutes(Config networkConfig, Config proxyConfig) throws Exception {
        HttpRequestProcessor httpRequestProcessor = new HttpRequestProcessor(proxyConfig, new Compressor());
        ProxyResponseProcessor proxyResponseProcessor = new ProxyResponseProcessor();

        camelContext.addRoutes(new WebServiceRouteBuilder(networkConfig, httpRequestProcessor, proxyResponseProcessor));
        camelContext.addRoutes(new ProxyRouteBuilder(networkConfig));
    }
}
