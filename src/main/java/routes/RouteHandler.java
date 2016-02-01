package routes;

import org.apache.camel.Processor;

public class RouteHandler {

    private final Processor responseProcessor;
    private final Processor requestProcessor;

    public RouteHandler(Processor requestProcessor, Processor responseProcessor) {
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
