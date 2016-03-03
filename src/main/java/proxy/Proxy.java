package proxy;

import com.typesafe.config.ConfigFactory;
import config.DilProxyConfig;
import org.apache.camel.impl.DefaultCamelContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import processors.ProxyPostProcessor;
import processors.ProxyPreprocessor;
import processors.WebServiceRequestProcessor;
import processors.protocols.HttpPreprocessor;
import processors.protocols.HttpRequest;
import proxy.serializer.ProxyPayloadSerializer;
import routing.protocols.*;
import routing.routes.CamelProxyRoute;
import routing.routes.CamelWebServiceRoute;

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

        File configFile = new File(args[0]);
        if ( !configFile.exists()) {
            System.out.println("File does not exist: " + args[0]);
            System.exit(1);
        }

        DilProxyConfig config = new DilProxyConfig(ConfigFactory.parseFile(configFile));

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
        DilRouteBuilder routeBuilder = createProtocolRoute();
        camelContext.addRoutes(new CamelWebServiceRoute(routeBuilder, config));
        camelContext.addRoutes(new CamelProxyRoute(routeBuilder, config));
    }

    private DilRouteBuilder createProtocolRoute() {
        switch (config.getSelectedProtocol()) {
            case AMQP:
                AmqpRoute amqpRoute = new AmqpRoute(config.getAmqpConfig());
                amqpRoute.addPreprocessor(new WebServiceRequestProcessor());
                amqpRoute.addPreprocessor(new ProxyPreprocessor(new ProxyPayloadSerializer()));
                amqpRoute.addPostProcessor(new ProxyPostProcessor());
                return  amqpRoute;
            case HTTP:
                HttpRoute httpRoute = new HttpRoute(config);
                httpRoute.addPreprocessor(new WebServiceRequestProcessor());
                httpRoute.addPreprocessor(new HttpPreprocessor());
                httpRoute.addPostProcessor(new HttpRequest());
                return httpRoute;
            case MQTT:
                return new MqttRoute(config);
            case COAP:
                CoapRoute coapRoute = new CoapRoute(config);
                coapRoute.addPreprocessor(new WebServiceRequestProcessor());
                coapRoute.addPreprocessor(new ProxyPreprocessor(new ProxyPayloadSerializer()));
                coapRoute.addPostProcessor(new ProxyPostProcessor());
                return coapRoute;
            default:
                logger.error("No configuration for: " + config.getSelectedProtocol());
                throw new IllegalArgumentException("No configuration for: " + config.getSelectedProtocol());
        }
    }
}
