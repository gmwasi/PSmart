package org.kenyahmis.psmartlibrary;

import com.google.gson.Gson;

/**
 * Created by Muhoro on 2/27/2018.
 */

public class Serializer {

    public String serialize(Object object){
        Gson gson = new Gson();
        return gson.toJson(object);
    }
}
