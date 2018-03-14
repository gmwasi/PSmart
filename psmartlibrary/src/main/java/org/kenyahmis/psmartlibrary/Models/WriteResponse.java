package org.kenyahmis.psmartlibrary.Models;

import java.util.List;

/**
 * Created by Muhoro on 2/24/2018.
 */

public class WriteResponse extends Response {
    private String encryptedTransmitMessage;

    public WriteResponse(String encrypredMessage, List<String> errors){
        super(errors);
        this.encryptedTransmitMessage = encrypredMessage;
    }

    public String getMessage(){
        return this.encryptedTransmitMessage;
    }
}
