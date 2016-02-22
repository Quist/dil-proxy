package proxy;

import com.typesafe.config.ConfigFactory;
import config.DilProxyConfig;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import routing.ProxyRouteFactory;
import routing.protocols.*;
import routing.WebServiceRouteFactory;

import java.io.File;

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

        File file = new File(args[0]);
        if ( !file.exists()) {
            System.out.println("File does not exist: " + args[0]);
            System.exit(1);
        }

        DilProxyConfig config = new DilProxyConfig(ConfigFactory.parseFile(file));

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
            logger.info("Proxy started and listening on " + config.getHostname() + " port " + config.getPort());
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

    private DilRouteBuilder createProtocolFactory() {
        switch (config.getSelectedProtocol()) {
            case AMQP:
                return new AmqpRoute(config);
            case HTTP:
                return new HttpRoute(config);
            case MQTT:
                return new MqttRoute(config);
            case COAP:
                return new CoapRoute(config);
            default:
                logger.error("No configuration for: " + config.getSelectedProtocol());
                throw new IllegalArgumentException("No configuration for: " + config.getSelectedProtocol());
        }
    }
}
