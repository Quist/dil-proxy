package routing;

import org.apache.camel.Processor;

public class RouteProcessorContainer {

    private final Processor responseProcessor;
    private final Processor requestProcessor;

    public RouteProcessorContainer(Processor requestProcessor, Processor responseProcessor) {
        this.requestProcessor = requestProcessor;
        this.responseProcessor = responseProcessor;
    }

    public Processor getRequestProcessor() {
        return requestProcessor;
    }

    public Processor getResponseProcessor() {
        return responseProcessor;
    }
}
