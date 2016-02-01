package routes.proxie.protocols;

import org.apache.camel.builder.RouteBuilder;

public interface ProtocolFactory {
    String getToUri();
    String getListenUri();
    RouteBuilder create();
}
