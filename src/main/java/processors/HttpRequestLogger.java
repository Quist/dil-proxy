package processors;

import org.apache.camel.Exchange;
import org.apache.camel.http.common.HttpMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.camel.Processor;
import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

public class HttpRequestLogger implements Processor {
    private final Logger logger = LoggerFactory.getLogger(HttpRequestLogger.class);

    @Override
    public void process(Exchange exchange) throws Exception {
        HttpServletRequest request = exchange.getIn(HttpMessage.class).getRequest();
        log(request);
    }

    private void log(HttpServletRequest request) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("Received HTTP request:");
        stringBuilder.append("\n\n# Request info :");
        stringBuilder.append("\nHttp method: ").append(request.getMethod());
        stringBuilder.append("\nRequest URL: ").append(request.getRequestURL());
        stringBuilder.append("\nPath header: ").append(request.getHeader("path"));
        stringBuilder.append("\nQuery String: ").append(request.getQueryString());

        Enumeration<String> headerNames = request.getHeaderNames();
        stringBuilder.append("\n# HTTP Headers: ");
        while(headerNames.hasMoreElements()){
            String headerName = headerNames.nextElement();
            stringBuilder.append((String.format("\n%s: %s", headerName, request.getHeader(headerName))));
        }

        stringBuilder.append("\n");
        logger.info(stringBuilder.toString());
    }


}
