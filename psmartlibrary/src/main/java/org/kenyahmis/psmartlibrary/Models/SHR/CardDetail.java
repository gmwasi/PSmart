package org.kenyahmis.psmartlibrary.Models.SHR;

/**
 * Created by Muhoro on 2/24/2018.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CardDetail {

    @SerializedName("STATUS")
    @Expose
    private String status;

    @SerializedName("REASON")
    @Expose
    private String reason;

    @SerializedName("LAST_UPDATED")
    @Expose
    private String lastupdated;

    @SerializedName("LAST_UPDATED_FACILITY")
    @Expose
    private String lastupdatedfacility;


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getLastupdated() {
        return lastupdated;
    }

    public void setLastupdated(String lastupdated) {
        this.lastupdated = lastupdated;
    }

    public String getLastupdatedfacility() {
        return lastupdatedfacility;
    }

    public void setLastupdatedfacility(String lastupdatedfacility) {
        this.lastupdatedfacility = lastupdatedfacility;
    }

}
