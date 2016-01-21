package main.java.proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;

public class HttpServletRequestLogger {
    final Logger logger = LoggerFactory.getLogger(HttpServletRequestLogger.class);

    public void log(HttpServletRequest request, String body) {
        String logMessage = "Received request:";
        logMessage += "\nmethod: " + request.getMethod();
        logMessage += "\nRequest URL: " + request.getRequestURL();
        logMessage += "\nPath header: " + request.getHeader("path");
        logMessage += "\nQuery String: " + request.getQueryString();
        logMessage += "\nHeader names: " + request.getHeaderNames();
        logMessage += "\nRemoteHost: " + request.getRemoteHost();


        if (body.length() > 0) {
            logMessage += "\nRequest body:\n" + body;
        }

        logger.info(logMessage);
    }
}
