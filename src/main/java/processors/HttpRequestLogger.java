package processors;

import org.apache.camel.Exchange;
import org.apache.camel.http.common.HttpMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.camel.Processor;
import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;

public class HttpRequestLogger implements Processor {
    private final Logger logger = LoggerFactory.getLogger(HttpRequestLogger.class);

    @Override
    public void process(Exchange exchange) throws Exception {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("Received HTTP request:");
        stringBuilder.append("\n\n# Request info :");
        stringBuilder.append("\nHttp method: ").append(exchange.getIn().getHeader(Exchange.HTTP_METHOD));
        stringBuilder.append("\nRequest URL: ").append(exchange.getIn().getHeader(Exchange.HTTP_SERVLET_REQUEST));
        stringBuilder.append("\nPath header: ").append(exchange.getIn().getHeader("path"));
        stringBuilder.append("\nQuery String: ").append(exchange.getIn().getHeader(Exchange.HTTP_QUERY));

        Iterator<String> headerIterator = exchange.getIn().getHeaders().keySet().iterator();
        stringBuilder.append("\n# HTTP Headers: ");
        while(headerIterator.hasNext()){
            String headerName = headerIterator.next();
            stringBuilder.append((String.format("\n%s: %s", headerName, exchange.getIn().getHeader(headerName))));
        }

        stringBuilder.append("\n");
        logger.info(stringBuilder.toString());
    }

}
