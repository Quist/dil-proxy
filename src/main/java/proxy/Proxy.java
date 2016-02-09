package proxy;

import com.typesafe.config.ConfigFactory;
import config.DilProxyConfig;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import routing.ProxyRouteFactory;
import routing.protocols.AmqpRouteFactory;
import routing.protocols.HttpRouteFactory;
import routing.protocols.MqttRouteFactory;
import routing.protocols.ProtocolFactory;
import routing.WebServiceRouteFactory;


public class Proxy {

    private final Logger logger = LoggerFactory.getLogger(Proxy.class);
    private final DefaultCamelContext camelContext;
    private final DilProxyConfig config;
    private final CamelComponentInitializer camelComponentInitializer;

    public static void main(String args[]) {

        if (args.length < 1) {
            printUsage();
            System.exit(1);
        }

        DilProxyConfig config = new DilProxyConfig(ConfigFactory.load(args[0]));
        DefaultCamelContext defaultCamelContext = new DefaultCamelContext();
        CamelComponentInitializer componentInitializer = new CamelComponentInitializer(defaultCamelContext);

        Proxy proxy = new Proxy(config, defaultCamelContext, componentInitializer);
        proxy.start();
    }

    private static void printUsage() {
        System.out.println("Usage: proxy configFile");
    }

    public Proxy(DilProxyConfig config, DefaultCamelContext camelContext, CamelComponentInitializer camelComponentInitializer) {
        this.camelContext = camelContext;
        this.config = config;
        this.camelComponentInitializer = camelComponentInitializer;
    }

    public void start()  {
        try {
            camelComponentInitializer.init(config);
            addRoutes();
            camelContext.start();
            logger.info("Proxy started and listening on " + config.getHostname());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addRoutes() throws Exception {
        addIncomingWebServiceRequestRoute();
        addBetweenProxiesRoute();
    }

    private void addIncomingWebServiceRequestRoute() throws Exception {
        WebServiceRouteFactory webServiceRouteFactory = new WebServiceRouteFactory(config);
        RouteBuilder webServiceRequestRoute = webServiceRouteFactory.create(createProtocolFactory());
        camelContext.addRoutes(webServiceRequestRoute);
    }

    private void addBetweenProxiesRoute() throws Exception {
        ProxyRouteFactory proxyRouteFactory = new ProxyRouteFactory();
        camelContext.addRoutes(proxyRouteFactory.createProxyRouteBuilder(config));
    }

    private ProtocolFactory createProtocolFactory() {
        switch (config.getProtocol()) {
            case AMQP:
                return new AmqpRouteFactory(config);
            case HTTP:
                return new HttpRouteFactory(config);
            case MQTT:
                return new MqttRouteFactory(config);
            default:
                logger.error("No configuration for: " + config.getProtocol());
                throw new IllegalArgumentException("No configuration for: " + config.getProtocol());
        }
    }
}
