package coap;

import org.apache.camel.Processor;
import org.apache.camel.impl.EventDrivenPollingConsumer;
import org.eclipse.californium.core.CoapServer;
import org.eclipse.californium.core.network.CoapEndpoint;

import java.net.InetSocketAddress;

public class CoapConsumer extends EventDrivenPollingConsumer {

    private final CamelCoapEndpoint endpoint;
    private final Processor processor;
    private final int port;

    public CoapConsumer(CamelCoapEndpoint camelCoapEndpoint, int port, Processor processor) {
        super(camelCoapEndpoint);
        this.endpoint = camelCoapEndpoint;
        this.port = port;
        this.processor = processor;
    }

    @Override
    protected void doStart() throws Exception {
        System.out.println("Starter opp!");
        CoapServer server = new CoapServer();
        addEndpoint(server);
        server.add(new CamelCoapResource("test", endpoint, processor));
        server.start();
    }

    @Override
    protected void doStop() throws Exception {

    }

    private void addEndpoint(CoapServer server) {
        InetSocketAddress bindToAddress = new InetSocketAddress("0.0.0", port);
        server.addEndpoint(new CoapEndpoint(bindToAddress));
    }
}
