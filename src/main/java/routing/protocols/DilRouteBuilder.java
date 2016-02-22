package routing.protocols;

import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;

import java.util.ArrayList;

public abstract class DilRouteBuilder {
    private ArrayList<Processor> preProcssors = new ArrayList<>();
    private ArrayList<Processor> postProcessors = new ArrayList<>();

    public abstract String getToUri();
    public abstract String getListenUri();

    public void addPreprocessor(Processor processor) {
        preProcssors.add(processor);
    }

    public ArrayList<Processor> getPreProcessors() {
        return preProcssors;
    }

    public void addPostProcessor(Processor processor) {
        postProcessors.add(processor);
    }

    public ArrayList<Processor> getPostProcessors() {
        return postProcessors;
    }
}
