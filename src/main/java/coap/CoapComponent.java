package coap;

import org.apache.camel.*;
import org.apache.camel.impl.DefaultComponent;
import org.apache.camel.impl.DefaultEndpoint;

import java.util.Map;

public class CoapComponent extends DefaultComponent {

    @Override
    protected Endpoint createEndpoint(String uri, String remaining, Map<String, Object> parameters) throws Exception {
        return new CoapEndpoint(uri, this);
    }

}
