package org.kenyahmis.psmart;

import android.content.Context;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Developed by Rodney on 27/02/2018.
 */

public class Tokenizer {
    private Context mContext;
    public Tokenizer(Context mContext) {
        this.mContext = mContext;
    }

    public String hashToken(String message){
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(message.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i=0; i<messageDigest.length; i++)
                hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public boolean compareHash(String message, String hash){
        return hashToken(message).matches(hash);
    }
}
