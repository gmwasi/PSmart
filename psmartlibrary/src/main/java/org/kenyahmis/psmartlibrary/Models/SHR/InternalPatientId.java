package org.kenyahmis.psmartlibrary.Models.SHR;

/**
 * Created by Muhoro on 2/24/2018.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InternalPatientId {

    @SerializedName("ID")
    @Expose
    private String id;
    @SerializedName("IDENTIFIER_TYPE")
    @Expose
    private String identifiertype;

    @SerializedName("ASSIGNING_AUTHORITY")
    @Expose
    private String assigningauthority;

    @SerializedName("ASSIGNING_FACILITY")
    @Expose
    private String assigningfacility;

    public String getID() {
        return id;
    }

    public void setID(String iD) {
        this.id = iD;
    }

    public String getIdentifiertype() {
        return identifiertype;
    }

    public void setidentifiertype(String identifiertype) {
        this.identifiertype = identifiertype;
    }

    public String getAssigningauthority() {
        return assigningauthority;
    }

    public void setAssigningauthority(String assigningauthority) {
        this.assigningauthority = assigningauthority;
    }

    public String getAssigningfacility() {
        return assigningfacility;
    }

    public void setAssigningfacility(String assigningfacility) {
        this.assigningfacility = assigningfacility;
    }

}
