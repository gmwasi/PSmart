package org.kenyahmis.psmartlibrary.Models.SHR;

/**
 * Created by Muhoro on 2/24/2018.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Immunization {

    @SerializedName("NAME")
    @Expose
    private String name;

    @SerializedName("DATE_ADMINISTERED")
    @Expose
    private String dateadministered;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDateadministered() {
        return dateadministered;
    }

    public void setDateadministered(String dateadministered) {
        this.dateadministered = dateadministered;
    }

}
