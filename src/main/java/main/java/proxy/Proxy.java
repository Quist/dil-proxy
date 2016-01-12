package main.java.proxy;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;


public class Proxy {

    private final DefaultCamelContext camelContext;
    private int listenPort;
    private int proxyPairPort;

    public static void main(String args[]) {

        if (args.length < 2) {
            printUsage();
            System.exit(1);
        }

        final int listenPort = Integer.parseInt(args[0]);
        final int proxyPairPort = Integer.parseInt(args[1]);

        new Proxy(listenPort, proxyPairPort).start();
    }


    public Proxy(int listenPort, int proxyPairPort) {
        this.listenPort = listenPort;
        this.proxyPairPort = proxyPairPort;
        this.camelContext = new DefaultCamelContext();
        buildRoutes();
    }

    public void start() {
        try {
            camelContext.start();
            System.out.println("Proxy listening on port " + listenPort);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void buildRoutes() {
        final String incomingServiceMessagePath = "jetty:http://localhost:" + listenPort + "/ws?matchOnUriPrefix=true";
        final String proxyPath = "http://localhost:" + proxyPairPort + "/proxy/incoming?bridgeEndpoint=true&throwExceptionOnFailure=false";
        final String incomingProxyMessagePath = "jetty:http://localhost:" + listenPort + "/proxy/incoming/?matchOnUriPrefix=true";
        final String outgoingServicePath = "jetty:http://localhost:" + "1337" + "/ws/hello?bridgeEndpoint=true&hrowExceptionOnFailure=false";

        try {
            camelContext.addRoutes(new RouteBuilder() {
                public void configure() {
                    from(incomingServiceMessagePath).process(new IncomingServiceMessageProcessor())
                            .to(proxyPath);
                }
            });
            camelContext.addRoutes(new RouteBuilder() {
                public void configure() {
                    from(incomingProxyMessagePath).process(new IncomingProxyMessageProcessor())
                            .to(outgoingServicePath);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void printUsage() {
       System.out.println("Usage: proxy listenPort proxyPairPort");
    }
}