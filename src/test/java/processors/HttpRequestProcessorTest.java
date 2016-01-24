package processors;

import org.apache.camel.Exchange;
import org.apache.camel.http.common.HttpMessage;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

import javax.servlet.http.HttpServletRequest;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class HttpRequestProcessorTest {

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void testProcessSetsPathInHeader() throws Exception {
        HttpRequestProcessor processor = new HttpRequestProcessor();
        StringBuffer requestURL = new StringBuffer("http://myubertest.com");

        Exchange exchange = mock(Exchange.class);
        HttpMessage httpMessage = mock(HttpMessage.class);
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURL()).thenReturn(requestURL);

        when(exchange.getIn(HttpMessage.class)).thenReturn(httpMessage);
        when(exchange.getIn()).thenReturn(httpMessage);
        when(httpMessage.getRequest()).thenReturn(request);

        processor.process(exchange);

        verify(exchange.getIn()).setHeader(eq("path"), eq(requestURL));
    }
}