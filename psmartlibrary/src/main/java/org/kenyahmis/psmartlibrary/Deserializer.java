package org.kenyahmis.psmartlibrary;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;

/**
 * Created by Muhoro on 2/27/2018.
 */

public class Deserializer {

    public <T> T deserialize(Class<T> cls, String jsonString) {
        Gson gson = new Gson();

        Type type = new GenericType<>().getType();
        T t = gson.fromJson(jsonString, cls);
        return t;
    }

    class GenericType<T> extends TypeToken<T> {
        public GenericType()
        {super();}
    }
}
