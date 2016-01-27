package proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.List;

public class HttpServletRequestLogger {
    final Logger logger = LoggerFactory.getLogger(HttpServletRequestLogger.class);

    public void log(HttpServletRequest request, String body) {
        String logMessage = "Received request:";
        logMessage += "\nHttp    method: " + request.getMethod();
        logMessage += "\nRequest URL: " + request.getRequestURL();
        logMessage += "\nPath header: " + request.getHeader("path");
        logMessage += "\nQuery String: " + request.getQueryString();

        if (body != null) {
            logMessage += "\nRequest body:\n" + body;
        }

        Enumeration<String> headerNames = request.getHeaderNames();
        logMessage += "\nHeaders: ";
        while(headerNames.hasMoreElements()){
            String headerName = headerNames.nextElement();
            logMessage += (String.format("\n%s: %s", headerName, request.getHeader(headerName)));
        }

        logger.info(logMessage);
    }
}
