package main.java.proxy;

import org.apache.camel.impl.DefaultCamelContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Proxy {

    private final Logger logger = LoggerFactory.getLogger(Proxy.class);
    private final DefaultCamelContext camelContext;
    private int port;
    private String oppositeProxyHost;

    public static void main(String args[]) {

        if (args.length < 2) {
            printUsage();
            System.exit(1);
        }

        final int port = Integer.parseInt(args[0]);
        final String oppositeProxyHost = args[1];

        Proxy proxy = new Proxy(port, oppositeProxyHost);
        proxy.start();
    }

    private static void printUsage() {
        System.out.println("Usage: proxy port oppositeProxyPort");
    }

    public Proxy(int port, String oppositeProxyHost) {
        this.port = port;
        this.oppositeProxyHost = oppositeProxyHost;
        this.camelContext = new DefaultCamelContext();
    }

    public void start() {

        new ProxyRouteBuilder().buildRoutes(port, oppositeProxyHost, camelContext);

        try {
            camelContext.start();
            logger.info("Proxy started and listening on port " + port);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
