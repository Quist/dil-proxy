package proxy;

import com.typesafe.config.ConfigFactory;
import config.DilProxyConfig;
import org.apache.camel.impl.DefaultCamelContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import routes.proxie.ProxyRouteFactory;
import routes.webservice.WebServiceRouteFactory;


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

    public void start()  {
        ComponentInitializer componentInitializer = new ComponentInitializer(camelContext);

        try {
            componentInitializer.init(config);
            addRoutes(config);
            camelContext.start();
            logger.info("Proxy started and listening on " + config.getHostname());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addRoutes(DilProxyConfig config) throws Exception {
        WebServiceRouteFactory webServiceRouteFactory = new WebServiceRouteFactory();
        ProxyRouteFactory proxyRouteFactory = new ProxyRouteFactory();

        camelContext.addRoutes(webServiceRouteFactory.createWebServiceRouteBuilder(config));
        camelContext.addRoutes(proxyRouteFactory.createProxyRouteBuilder(config));
    }
}
