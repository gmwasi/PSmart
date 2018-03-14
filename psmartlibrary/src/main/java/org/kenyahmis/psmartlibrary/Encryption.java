package org.kenyahmis.psmartlibrary;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;
import org.kenyahmis.psmartlibrary.EncrytionKeys;
/**
 * Created by GMwasi on 2/9/2018.
 */

public class Encryption {

    private String intializationVector = "PdSgVkXp2s5v8y/B";
    private String encryptedString;
    private String originalMessage;

    public String encrypt(String key, String message) { // encyption.encrpt(EncypronKey.SHRKey, message)
        try {
            IvParameterSpec iv = new IvParameterSpec(intializationVector.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

            byte[] encrypted = cipher.doFinal(message.getBytes());
            encryptedString = Base64.encodeBase64String(encrypted);
        } catch (Exception ex) {
            ex.getMessage();
        }

        return encryptedString;
    }

    public String decrypt(String key, String encrypted) {
        try {
            IvParameterSpec iv = new IvParameterSpec(intializationVector.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

            byte[] original = cipher.doFinal(Base64.decodeBase64(encrypted));

            originalMessage = new  String(original);
        } catch (Exception ex) {
            ex.getMessage();
        }

        return originalMessage;
    }
}

