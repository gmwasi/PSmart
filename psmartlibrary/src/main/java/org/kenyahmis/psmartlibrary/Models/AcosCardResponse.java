package org.kenyahmis.psmartlibrary.Models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by GMwasi on 3/13/2018.
 */

public class AcosCardResponse {
    public byte [] bytes;
    public String hexString;
    public List<String> errors;

    public AcosCardResponse(byte[] bytes, String hexString, List<String> errors){
        this.bytes = bytes;
        this.hexString = hexString;
        this.errors = errors != null ? errors : new ArrayList<String>();
     }
}
