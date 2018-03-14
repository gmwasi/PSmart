package org.kenyahmis.psmartlibrary;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.Editable;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale;

/**
 * Created by GMwasi on 2/14/2018.
 */

public class Utils {
    // This will reference one line at a time
    String line = null;
    String output =null;

    public String ReadFile(String fileName){
        StringBuilder builder = new StringBuilder();
        try {
            // FileReader reads text files in the default encoding.
            FileReader fileReader = new FileReader(fileName);

            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            while((line = bufferedReader.readLine()) != null) {
                //System.out.println(line);
                builder.append(line+"\n");
                //line = line + "\n"+ bufferedReader.readLine();
            }

            System.out.println("Read " + builder.toString().getBytes().length + " bytes");

            // Always close files.
            bufferedReader.close();
        }
        catch(FileNotFoundException ex) {
            return new String("Unable to open file '" + fileName + "'");
        }
        catch(IOException ex) {
            return ex.getMessage();
        }
        return builder.toString();
    }

    public byte[] ReadBinaryFile(String fileName){
        //Define byte size
        //Currently set to fi the SHR message
        byte[] buffer = new byte[100000];
        try {

            FileInputStream inputStream = new FileInputStream(fileName);

            int total = 0;
            int nRead = 0;
            while((nRead = inputStream.read(buffer)) != -1) {
                // Convert to String so we can display it.
                // Of course you wouldn't want to do this with
                // a 'real' binary file.
                System.out.println(new String(buffer));
                total += nRead;
            }

            // Always close files.
            inputStream.close();

            System.out.println("Read " + total + " bytes");
        }
        catch(FileNotFoundException ex) {
            System.out.println("Unable to open file '" + fileName + "'");
        }
        catch(IOException ex) {
            System.out.println(ex.getMessage());
        }
        return buffer;
    }

    public void WriteFile(String writeFile, String data){
        try {
            FileWriter fileWriter = new FileWriter(writeFile);

            // Always wrap FileWriter in BufferedWriter.
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            bufferedWriter.write(data);

            // Always close files.
            bufferedWriter.close();

            System.out.println("Wrote " + data.getBytes().length + " bytes");
        }
        catch(IOException ex) {
            ex.printStackTrace();
        }
    }

    public void WriteBinaryFile(String writeFile, byte[] data){
        try {
            FileOutputStream outputStream = new FileOutputStream(writeFile);

            outputStream.write(data);

            // Always close files.
            outputStream.close();

            System.out.println("Wrote " + data.length + " bytes");
        }
        catch(IOException ex) {
             ex.printStackTrace();
        }
    }

    public void ReadCompressWrite(String fileName, String writeFile){

        try {
            Compression compression = new Compression();
            String read = ReadFile(fileName);
            byte[] compressed = compression.Compress(read);
            WriteBinaryFile(writeFile, compressed);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void ReadEncryptWrite(String fileName, String writeFile){

        Encryption encryption = new Encryption();
        String read = ReadFile(fileName);
        String encrypted = encryption.encrypt(EncrytionKeys.SHR_KEY,read);
        WriteFile(writeFile, encrypted);

    }

    public void ReadEncryptCompressWrite(String fileName, String writeFile){

        try {
            Encryption encryption = new Encryption();
            Compression compression = new Compression();
            String read = ReadFile(fileName);
            String encrypted = encryption.encrypt(EncrytionKeys.SHR_KEY, read);
            byte[] compressed = compression.Compress(encrypted);
            WriteBinaryFile(writeFile, compressed);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void ReadDecompressDecryptWrite(String fileName, String writeFile){

        try {
            Encryption encryption = new Encryption();
            Compression compression = new Compression();
            byte[] read = ReadBinaryFile(fileName);
            String decompressed = compression.Decompress(read);
            String decrypted = encryption.decrypt(EncrytionKeys.SHR_KEY, decompressed);
            WriteFile(writeFile, decrypted);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates a hexadecimal <code>String</code> representation of the
     * <code>byte[]</code> passed. Each element is converted to a
     * <code>String</code> via the {@link Integer#toHexString(int)} and
     * separated by <code>" "</code>. If the array is <code>null</code>, then
     * <code>""<code> is returned.
     *
     * @param array
     *            the <code>byte</code> array to convert.
     * @return the <code>String</code> representation of <code>array</code> in
     *         hexadecimal.
     */
    public static String ByteArrayToHexString(byte[] array) {

        String bufferString = "";

        if (array != null) {
            for (int i = 0; i < array.length; i++) {
                String hexChar = Integer.toHexString(array[i] & 0xFF);
                if (hexChar.length() == 1) {
                    hexChar = "0" + hexChar;
                }
                bufferString += hexChar.toUpperCase(Locale.US) + " ";
            }
        }
        return bufferString;
    }

    private static boolean isHexNumber(byte value) {
        if (!(value >= '0' && value <= '9') && !(value >= 'A' && value <= 'F')
                && !(value >= 'a' && value <= 'f')) {
            return false;
        }
        return true;
    }

    /**
     * Checks a hexadecimal <code>String</code> that is contained hexadecimal
     * value or not.
     *
     * @param string
     *            the string to check.
     * @return <code>true</code> the <code>string</code> contains Hex number
     *         only, <code>false</code> otherwise.
     * @throws NullPointerException
     *             if <code>string == null</code>.
     */
    public static boolean isHexNumber(String string) {
        if (string == null)
            throw new NullPointerException("string was null");

        boolean flag = true;

        for (int i = 0; i < string.length(); i++) {
            char cc = string.charAt(i);
            if (!isHexNumber((byte) cc)) {
                flag = false;
                break;
            }
        }
        return flag;
    }

    private static byte uniteBytes(byte src0, byte src1) {
        byte _b0 = Byte.decode("0x" + new String(new byte[] { src0 }))
                .byteValue();
        _b0 = (byte) (_b0 << 4);
        byte _b1 = Byte.decode("0x" + new String(new byte[] { src1 }))
                .byteValue();
        byte ret = (byte) (_b0 ^ _b1);
        return ret;
    }

    /**
     * Creates a <code>byte[]</code> representation of the hexadecimal
     * <code>String</code> passed.
     *
     * @param string
     *            the hexadecimal string to be converted.
     * @return the <code>array</code> representation of <code>String</code>.
     * @throws IllegalArgumentException
     *             if <code>string</code> length is not in even number.
     * @throws NullPointerException
     *             if <code>string == null</code>.
     * @throws NumberFormatException
     *             if <code>string</code> cannot be parsed as a byte value.
     */
    public static byte[] HexStringToByteArray(String string) {
        if (string == null)
            throw new NullPointerException("string was null");

        int len = string.length();

        if (len == 0)
            return new byte[0];
        if (len % 2 == 1)
            throw new IllegalArgumentException(
                    "string length should be an even number");

        byte[] ret = new byte[len / 2];
        byte[] tmp = string.getBytes();

        for (int i = 0; i < len; i += 2) {
            if (!isHexNumber(tmp[i]) || !isHexNumber(tmp[i + 1])) {
                throw new NumberFormatException(
                        "string contained invalid value");
            }
            ret[i / 2] = uniteBytes(tmp[i], tmp[i + 1]);
        }
        return ret;
    }

    /**
     * Creates a hexadecimal <code>String</code> representation of the
     * <code>byte[]</code> passed. Each element is converted to a
     * <code>String</code> via the {@link Integer#toHexString(int)} and
     * separated by <code>" "</code>. If the array is <code>null</code>, then
     * <code>""<code> is returned.
     *
     * @param array
     *            the <code>byte</code> array to convert.
     * @return the <code>String</code> representation of <code>array</code> in
     *         hexadecimal.
     */
    public static String toHexString(byte[] array) {

        String bufferString = "";

        if (array != null) {
            for (int i = 0; i < array.length; i++) {
                String hexChar = Integer.toHexString(array[i] & 0xFF);
                if (hexChar.length() == 1) {
                    hexChar = "0" + hexChar;
                }
                bufferString += hexChar.toUpperCase(Locale.US) + " ";
            }
        }
        return bufferString;
    }

    /**
     * Creates a <code>byte[]</code> representation of the hexadecimal
     * <code>String</code> passed.
     *
     * @param string
     *            the hexadecimal string to be converted.
     * @return the <code>array</code> representation of <code>String</code>.
     * @throws IllegalArgumentException
     *             if <code>string</code> length is not in even number.
     * @throws NullPointerException
     *             if <code>string == null</code>.
     * @throws NumberFormatException
     *             if <code>string</code> cannot be parsed as a byte value.
     */
    public static byte[] hexString2Bytes(String string) {
        if (string == null)
            throw new NullPointerException("string was null");

        int len = string.length();

        if (len == 0)
            return new byte[0];
        if (len % 2 == 1)
            throw new IllegalArgumentException(
                    "string length should be an even number");

        byte[] ret = new byte[len / 2];
        byte[] tmp = string.getBytes();

        for (int i = 0; i < len; i += 2) {
            if (!isHexNumber(tmp[i]) || !isHexNumber(tmp[i + 1])) {
                throw new NumberFormatException(
                        "string contained invalid value");
            }
            ret[i / 2] = uniteBytes(tmp[i], tmp[i + 1]);
        }
        return ret;
    }

    /**
     * Creates a <code>byte[]</code> representation of the hexadecimal
     * <code>String</code> in the EditText control.
     *
     * @param editText
     *            the EditText control which contains hexadecimal string to be
     *            converted.
     * @return the <code>array</code> representation of <code>String</code> in
     *         the EditText control. <code>null</code> if the string format is
     *         not correct.
     */
    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    public static byte[] getEditTextinHexBytes(EditText editText) {
        Editable edit = editText.getText();

        if (edit == null) {
            return null;
        }

        String rawdata = edit.toString();

        if (rawdata == null || rawdata.isEmpty()) {
            return null;
        }

        String command = rawdata.replace(" ", "").replace("\n", "");

        if (command.isEmpty() || command.length() % 2 != 0
                || isHexNumber(command) == false) {
            return null;
        }

        return hexString2Bytes(command);
    }

    public static byte[] getTextinHexBytes(String text){

        if (text == null || text.isEmpty()) {
            return null;
        }

        String command = text.replace(" ", "").replace("\n", "");

        if (command.isEmpty() || command.length() % 2 != 0
                || isHexNumber(command) == false) {
            return null;
        }

        return hexString2Bytes(command);
    }

    public static String hexToString(String txtInHex)
    {
        byte [] txtInByte = new byte [txtInHex.length() / 2];
        int j = 0;
        for (int i = 0; i < txtInHex.length(); i += 3)
        {
            txtInByte[j++] = Byte.parseByte(txtInHex.substring(i, i + 2), 16);
        }
        return new String(txtInByte);
    }

    public static String stringToHexString(String text){
        return toHexString(text.getBytes());
    }

    public static String byteArrayToString (byte[] data, int length)
    {
        String str = "";
        int indx = 0;

        while((data[indx] & 0xFF) != 0x00)
        {
            str  += (char)(data[indx] & 0xFF);
            indx++;
            if (indx == length)
                break;
        }

        return str;
    }

}
