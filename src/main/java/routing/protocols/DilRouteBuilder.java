package routing.protocols;

import org.apache.camel.Processor;
import java.util.ArrayList;

public abstract class DilRouteBuilder {
    private final ArrayList<Processor> preProcssors = new ArrayList<>();
    private final ArrayList<Processor> postProcessors = new ArrayList<>();

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
