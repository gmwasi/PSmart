package org.kenyahmis.psmartlibrary.Models.SHR;

/**
 * Created by Muhoro on 2/24/2018.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PhysicalAddress {

    @SerializedName("VILLAGE")
    @Expose
    private String village;

    @SerializedName("WARD")
    @Expose
    private String ward;

    @SerializedName("SUB_COUNTY")
    @Expose
    private String subcounty;

    @SerializedName("COUNTY")
    @Expose
    private String county;

    @SerializedName("NEAREST_LANDMARK")
    @Expose
    private String nearestlandmark;

    public String getVillage() {
        return village;
    }

    public void setVillage(String village) {
        this.village = village;
    }

    public String getWard() {
        return ward;
    }

    public void setWard(String ward) {
        this.ward = ward;
    }

    public String getSubcounty() {
        return subcounty;
    }

    public void setSubcounty(String subcounty) {
        this.subcounty = subcounty;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getNearestlandmark() {
        return nearestlandmark;
    }

    public void setNearestlandmark(String nearestlandmark) {
        this.nearestlandmark = nearestlandmark;
    }

}
