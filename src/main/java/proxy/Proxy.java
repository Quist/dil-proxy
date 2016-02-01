package proxy;

import com.typesafe.config.ConfigFactory;
import config.DilProxyConfig;
import org.apache.camel.component.amqp.AMQPComponent;
import org.apache.camel.impl.DefaultCamelContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import processors.HttpRequestProcessor;
import processors.ProxyResponseProcessor;
import routes.RouteFactory;
import routes.WebServiceRouteBuilder;

import java.net.MalformedURLException;


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
        try {
            addComponents();
            addRoutes(config);
            camelContext.start();
            logger.info("Proxy started and listening on " + config.getHostname());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addComponents() throws MalformedURLException {
        logger.info("Adding AMQP component with broker connection URI: " + config.getBrokerConnectionUri());
        AMQPComponent amqp = AMQPComponent.amqp10Component(config.getBrokerConnectionUri());
        camelContext.addComponent("amqp", amqp);
    }

    private void addRoutes(DilProxyConfig config) throws Exception {
        HttpRequestProcessor httpRequestProcessor = new HttpRequestProcessor();
        ProxyResponseProcessor proxyResponseProcessor = new ProxyResponseProcessor();

        camelContext.addRoutes(new WebServiceRouteBuilder(config, httpRequestProcessor, proxyResponseProcessor));
        camelContext.addRoutes(new RouteFactory().createProxyRouteBuilder(config));
    }
}
