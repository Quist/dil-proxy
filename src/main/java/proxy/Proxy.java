package proxy;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import compressor.Compressor;
import config.DilProxyConfig;
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
    private final DilProxyConfig config;

    public static void main(String args[]) {

        if (args.length < 1) {
            printUsage();
            System.exit(1);
        }

        DilProxyConfig config = new DilProxyConfig(ConfigFactory.load(args[0]));

        Proxy proxy = new Proxy(config);
        proxy.start();
    }

    private static void printUsage() {
        System.out.println("Usage: proxy configFile");
    }

    public Proxy(DilProxyConfig config) {
        this.camelContext = new DefaultCamelContext();
        this.config = config;
    }

    public void start() {

        try {
            addRoutes(config);
            camelContext.start();
            logger.info("Proxy started and listening on " + config.getHostname());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addRoutes(DilProxyConfig config) throws Exception {
        HttpRequestProcessor httpRequestProcessor = new HttpRequestProcessor();
        ProxyResponseProcessor proxyResponseProcessor = new ProxyResponseProcessor();

        camelContext.addRoutes(new WebServiceRouteBuilder(config, httpRequestProcessor, proxyResponseProcessor));
        camelContext.addRoutes(new ProxyRouteBuilder(config));
    }
}
