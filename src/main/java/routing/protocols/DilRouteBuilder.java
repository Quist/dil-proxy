package routing.protocols;

import org.apache.camel.builder.RouteBuilder;

public interface DilRouteBuilder {
    String getToUri();
    String getListenUri();
    RouteBuilder create();
}
