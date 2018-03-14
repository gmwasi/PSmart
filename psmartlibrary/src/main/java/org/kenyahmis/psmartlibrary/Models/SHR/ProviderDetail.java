package org.kenyahmis.psmartlibrary.Models.SHR;

/**
 * Created by Muhoro on 2/24/2018.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProviderDetail {

    @SerializedName("ID")
    @Expose
    private String id;

    @SerializedName("NAME")
    @Expose
    private String name;


    public String getId(){
        return id;
    }

    public void setId(String id){
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
