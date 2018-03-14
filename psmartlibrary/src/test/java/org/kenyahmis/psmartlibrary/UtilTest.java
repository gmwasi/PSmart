package org.kenyahmis.psmartlibrary;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by GMwasi on 2/14/2018.
 */
public class UtilTest {
        private String originalFile = "TestData\\psmart.txt";
        private String encryptedCompressedFile = "TestData\\encryptedCompressedWrite.txt";
        private String binaryFile = "TestData\\writeBinary.txt";
        private String text = "Hello World!";
        private String hexText = "48 65 6C 6C 6F 20 57 6F 72 6C 64 21 ";
        private byte[] buffer = text.getBytes();

    @Test
    public void readFile() throws Exception {
        Utils u = new Utils();
        String output = u.ReadFile(originalFile);
        Assert.assertNotEquals(output, "Unable to open file '" + originalFile + "'");
        Assert.assertNotNull(output);
    }

    @Test
    public void writeFile() throws Exception {
        Utils u = new Utils();
        String writeFile = "TestData\\write.txt";
        u.WriteFile(writeFile, text);
        String output = u.ReadFile(writeFile);
        Assert.assertNotNull(output);
    }

    @Test
    public void writeBinaryFile() throws Exception {
        Utils u = new Utils();
        u.WriteBinaryFile(binaryFile, buffer);
        byte[] output = u.ReadBinaryFile(binaryFile);
        Assert.assertTrue(output.length>0);
    }

    @Test
    public void readBinaryFile() throws Exception {
        Utils u = new Utils();
        byte[] output = u.ReadBinaryFile(binaryFile);
        Assert.assertNotEquals(output, "Unable to open file '" + binaryFile + "'");
        Assert.assertTrue(output.length > 0);
    }

    @Test
    public void ReadCompressWrite() throws Exception{
        Utils u = new Utils();
        String compressedFile = "TestData\\compressedWrite.txt";
        u.ReadCompressWrite(originalFile, compressedFile);
        byte[] output = u.ReadBinaryFile(compressedFile);
        Assert.assertTrue(output.length>0);
    }

    @Test
    public void ReadEncryptWrite() throws Exception{
        Utils u = new Utils();
        String encryptedFile = "TestData\\encryptedWrite.txt";
        u.ReadEncryptWrite(originalFile, encryptedFile);
        String output = u.ReadFile(encryptedFile);
        Assert.assertNotNull(output);
    }

    @Test
    public void ReadEncryptCompressWrite() throws Exception{
        Utils u = new Utils();
        u.ReadEncryptCompressWrite(originalFile, encryptedCompressedFile);
        byte[] output = u.ReadBinaryFile(encryptedCompressedFile);
        Assert.assertTrue(output.length > 0);
    }

    @Test
    public void ReadDecompressDecryptWrite() throws Exception{
        Utils u = new Utils();
        String decryptedDecompressedFile = "TestData\\decryptedDecompressedWrite.txt";
        u.ReadEncryptCompressWrite(originalFile, encryptedCompressedFile);
        u.ReadDecompressDecryptWrite(encryptedCompressedFile, decryptedDecompressedFile);
        String output = u.ReadFile(decryptedDecompressedFile);
        Assert.assertNotEquals(output, "Unable to open file '" + decryptedDecompressedFile + "'");
        Assert.assertNotNull(output);
    }

    @Test
    public void stringToHexString(){
        String hex = Utils.stringToHexString(text);
        Assert.assertEquals(hex,hexText);
    }

    @Test
    public void hexStringToString(){
        String str = Utils.hexToString(hexText).trim();
        //Assert.assertSame(str,text);

    }

}