package org.kenyahmis.psmartlibrary.Models;

import java.util.List;

/**
 * Created by Muhoro on 2/24/2018.
 */

public class ReadResponse extends Response {

    private String serializedSHR;

    public ReadResponse(String serializedSHR, List<String> errors){
        super(errors);
        this.serializedSHR = serializedSHR;
    }

    public String getMessage(){
        return  this.serializedSHR;
    }
}
