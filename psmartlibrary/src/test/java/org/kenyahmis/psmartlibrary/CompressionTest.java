package org.kenyahmis.psmartlibrary;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by GMwasi on 2/9/2018.
 */

public class CompressionTest {
    private Compression x = new Compression();
    private String message = "My very educated mother aaaaaaa ffffff hhhskds sdfifj efwfj we  wie foi fi ewif w wfi w ji fio fiwfi gggggg weofw";
    @Test
    public void MessageCanBeCompressed() throws Exception {

        byte[] compressed = x.Compress(message);
        int originalBytes = message.getBytes().length;
        int compressedBytes = compressed.length;
        Assert.assertTrue(originalBytes > compressedBytes);
    }

    @Test
    public void MessageCanBeDecompressed() throws Exception {
        byte[] compressed = x.Compress(message);
        String decompressedText = x.Decompress(compressed);
        int originalBytes = message.getBytes().length;
        int compressedBytes = compressed.length;
        //Assert.assertEquals(message, messageTest);

    }
}
