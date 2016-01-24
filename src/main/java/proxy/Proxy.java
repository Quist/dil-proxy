package proxy;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.apache.camel.impl.DefaultCamelContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

        try {
            addRoutes(networkConfig);
            camelContext.start();
            logger.info("Proxy started and listening on port " + networkConfig.getInt("port"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addRoutes(Config networkConfig) throws Exception {
        camelContext.addRoutes(new WebServiceRouteBuilder(networkConfig));
        camelContext.addRoutes(new ProxyRouteBuilder(networkConfig));
    }
}
