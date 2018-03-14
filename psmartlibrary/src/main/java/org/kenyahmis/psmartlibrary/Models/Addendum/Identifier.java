package org.kenyahmis.psmartlibrary.Models.Addendum;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Muhoro on 2/27/2018.
 */

public class Identifier {

    @SerializedName("ID")
    @Expose
    private String id;

    @SerializedName("IDENTIFIER_TYPE")
    @Expose
    private String identifierType;

    @SerializedName("ASSIGNING_AUTHORITY")
    @Expose
    private String assigningAuthority;

    @SerializedName("ASSIGNING_FACILITY")
    @Expose
    private String assigningFacility;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdentifierType() {
        return identifierType;
    }

    public void setIdentifierType(String identifierType) {
        this.identifierType = identifierType;
    }

    public String getAssigningAuthority() {
        return assigningAuthority;
    }

    public void setAssigningAuthority(String assigningAuthority) {
        this.assigningAuthority = assigningAuthority;
    }

    public String getAssigningFacility() {
        return assigningFacility;
    }

    public void setAssigningFacility(String assigningFacility) {
        this.assigningFacility = assigningFacility;
    }
}
