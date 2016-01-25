package processors;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigValue;
import com.typesafe.config.ConfigValueFactory;
import compressor.Compressor;
import org.apache.camel.Exchange;
import org.apache.camel.http.common.HttpMessage;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class HttpRequestProcessorTest {

    @Test
    public void testProcessSetsPathInHeader() throws Exception {
        Config proxyConfig = ConfigFactory.empty();
        proxyConfig = proxyConfig.withValue("useCompression", ConfigValueFactory.fromAnyRef(true));

        StringBuffer requestURL = new StringBuffer("http://myubertest.com");
        Compressor compressor = new Compressor();

        HttpRequestProcessor processor = new HttpRequestProcessor(proxyConfig, compressor);
        Exchange exchange = createExchange(requestURL);

        processor.process(exchange);

        verify(exchange.getIn()).setHeader(eq("path"), eq(requestURL));
    }


    @Test public void testProcessSupportsCompression() throws Exception {
        Config proxyConfig = ConfigFactory.empty();
        proxyConfig = proxyConfig.withValue("useCompression", ConfigValueFactory.fromAnyRef(true));


        Compressor compressor = new Compressor();
        StringBuffer requestURL = new StringBuffer("http://myubertest.com");

        HttpRequestProcessor processor = new HttpRequestProcessor(proxyConfig, compressor);
        Exchange exchange = createExchange(requestURL);

        processor.process(exchange);

        verify(exchange.getIn()).setBody("compressed");
    }


    @Test
    public void testProcessRetainsBody() throws Exception {
        Config proxyConfig = ConfigFactory.empty();
        proxyConfig = proxyConfig.withValue("useCompression", ConfigValueFactory.fromAnyRef(false));

        Compressor compressor = new Compressor();
        StringBuffer requestURL = new StringBuffer("http://myubertest.com");

        HttpRequestProcessor processor = new HttpRequestProcessor(proxyConfig, compressor);
        Exchange exchange = createExchange(requestURL);

        processor.process(exchange);

        verify(exchange.getIn()).setBody("");
    }


    private Exchange createExchange(StringBuffer requestURL) {
        Exchange exchange = mock(Exchange.class);
        HttpMessage httpMessage = mock(HttpMessage.class);
        HttpServletRequest request = mock(HttpServletRequest.class);

        when(request.getRequestURL()).thenReturn(requestURL);
        when(exchange.getIn(HttpMessage.class)).thenReturn(httpMessage);
        when(exchange.getIn()).thenReturn(httpMessage);
        when(httpMessage.getRequest()).thenReturn(request);
        return exchange;
    }

}