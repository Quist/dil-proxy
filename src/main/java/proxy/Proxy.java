package proxy;

import com.typesafe.config.ConfigFactory;
import config.DilProxyConfig;
import org.apache.camel.impl.DefaultCamelContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import processors.protocols.HttpRequestLogger;
import processors.ProxyRequestPostProcessor;
import processors.ProxyRequestPreprocessor;
import processors.protocols.AmqpRequest;
import processors.protocols.HttpPreprocessor;
import proxy.serializer.ProxyRequestSerializer;
import routing.protocols.*;
import routing.routes.CamelProxyRoute;
import routing.routes.CamelWebServiceRoute;

import java.io.File;
import java.net.MalformedURLException;

public class Proxy {

    private final Logger logger = LoggerFactory.getLogger(Proxy.class);
    private final DefaultCamelContext camelContext;
    private final DilProxyConfig config;

    public static void main(String args[]) throws MalformedURLException {

        if (args.length < 1) {
            printUsage();
            System.exit(1);
        }

        File configFile = new File(args[0]);
        if ( !configFile.exists()) {
            System.out.println("File does not exist: " + args[0]);
            System.exit(1);
        }

        DilProxyConfig config = new DilProxyConfig(ConfigFactory.parseFile(configFile));

        DefaultCamelContext defaultCamelContext = new DefaultCamelContext();
        new CamelComponentInitializer(defaultCamelContext).init(config);
        Proxy proxy = new Proxy(config, defaultCamelContext);
        proxy.start();
    }

    private static void printUsage() {
        System.out.println("Usage: proxy configFile");
    }

    public Proxy(DilProxyConfig config, DefaultCamelContext camelContext) {
        this.camelContext = camelContext;
        this.config = config;
    }

    public void start()  {
        try {
            addRoutes();
            camelContext.start();
            logger.info("Proxy started and listening on " + config.getHostname() + ", port " + config.getPort());
            logger.info("Using protocol " + config.getSelectedProtocol().toString() + " to communicate between proxies");
            logger.info("Opposite proxy located at " + config.getTargetProxyHostname());
            logger.info("Compression setting: " + config.useCompression());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addRoutes() throws Exception {
        DilRouteBuilder routeBuilder = createProtocolRoute();
        camelContext.addRoutes(new CamelWebServiceRoute(routeBuilder, config));
        camelContext.addRoutes(new CamelProxyRoute(routeBuilder, config));
    }

    private DilRouteBuilder createProtocolRoute() {
        switch (config.getSelectedProtocol()) {
            case AMQP:
                AmqpRoute amqpRoute = new AmqpRoute(config);
                amqpRoute.addPreprocessor(new HttpRequestLogger());
                amqpRoute.addPreprocessor(new ProxyRequestPreprocessor(new ProxyRequestSerializer()));
                amqpRoute.addPostProcessor(new ProxyRequestPostProcessor());
                amqpRoute.addPostProcessor(new AmqpRequest());
                return  amqpRoute;
            case HTTP:
                HttpRoute httpRoute = new HttpRoute(config);
                httpRoute.addPreprocessor(new HttpRequestLogger());
                httpRoute.addPreprocessor(new HttpPreprocessor());
                httpRoute.addPreprocessor(new ProxyRequestPreprocessor(new ProxyRequestSerializer()));
                httpRoute.addPostProcessor(new ProxyRequestPostProcessor());
                httpRoute.addPostProcessor(new HttpRequestLogger());
                return httpRoute;
            case MQTT:
                throw new IllegalArgumentException("MQTT is not implemented");
            case COAP:
                CoapRoute coapRoute = new CoapRoute(config);
                coapRoute.addPreprocessor(new HttpRequestLogger());
                coapRoute.addPreprocessor(new ProxyRequestPreprocessor(new ProxyRequestSerializer()));
                coapRoute.addPostProcessor(new ProxyRequestPostProcessor());
                return coapRoute;
            default:
                logger.error("No configuration for: " + config.getSelectedProtocol());
                throw new IllegalArgumentException("No configuration for: " + config.getSelectedProtocol());
        }
    }
}
