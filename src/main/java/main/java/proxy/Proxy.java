package main.java.proxy;

import org.apache.camel.impl.DefaultCamelContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Proxy {

    final Logger logger = LoggerFactory.getLogger(Proxy.class);
    private final DefaultCamelContext camelContext;
    private int listenPort;
    private String oppositeProxyHost;

    public static void main(String args[]) {

        if (args.length < 2) {
            printUsage();
            System.exit(1);
        }

        final int listenPort = Integer.parseInt(args[0]);
        final String oppositeProxyHost = args[1];

        Proxy proxy = new Proxy(listenPort, oppositeProxyHost);
        proxy.start();
    }

    private static void printUsage() {
        System.out.println("Usage: proxy listenPort oppositeProxyPort");
    }

    public Proxy(int listenPort, String oppositeProxyHost) {
        this.listenPort = listenPort;
        this.oppositeProxyHost = oppositeProxyHost;
        this.camelContext = new DefaultCamelContext();
    }

    public void start() {

        new ProxyRouteBuilder().buildRoutes(listenPort, oppositeProxyHost, camelContext);

        try {
            camelContext.start();
            logger.info("Proxy started and listening on port " + listenPort);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
