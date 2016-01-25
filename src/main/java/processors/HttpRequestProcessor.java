package processors;

import com.typesafe.config.Config;
import compressor.Compressor;
import proxy.HttpServletRequestLogger;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.http.common.HttpMessage;

import javax.servlet.http.HttpServletRequest;

public class HttpRequestProcessor implements Processor {

    private static final HttpServletRequestLogger httpServletRequestLogger = new HttpServletRequestLogger();
    private final Config proxyConfig;
    private final Compressor compressor;

    public HttpRequestProcessor(Config proxyConfig, Compressor compressor) {
        this.proxyConfig = proxyConfig;
        this.compressor = compressor;
    }


    @Override
    public void process(Exchange exchange) throws Exception {
        HttpServletRequest request = exchange.getIn(HttpMessage.class).getRequest();
        String body = exchange.getIn().getBody(String.class);
        if (body == null) {
            body = "";
        }

        exchange.getIn().setHeader("path", request.getRequestURL());
        httpServletRequestLogger.log(request, body);

        if (proxyConfig.getBoolean("useCompression")) {
            body = compressor.compress(body);
        }

        exchange.getIn().setBody(body);
    }
}
