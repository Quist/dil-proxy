package compressor;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.*;


public class CompressorTest {

    @Test
    public void testCompressReturnsString() {
        Compressor compressor = new Compressor();

        assertThat(compressor.compress("test"), is(notNullValue()));
    }

    @Test
    public void testCompressCompressesCorrectly() {
        Compressor compressor = new Compressor();
        String body = "1234";

        assertThat(compressor.compress(body), is("compressed"));

    }


}