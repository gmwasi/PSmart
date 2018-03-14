package org.kenyahmis.psmartlibrary.Models.SHR;

/**
 * Created by Muhoro on 2/24/2018.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HIVTest {

    @SerializedName("DATE")
    @Expose
    private String date;

    @SerializedName("RESULT")
    @Expose
    private String result;

    @SerializedName("TYPE")
    @Expose
    private String type;

    @SerializedName("FACILITY")
    @Expose
    private String facility;

    @SerializedName("STRATEGY")
    @Expose
    private String strategy;

    @SerializedName("PROVIDER_DETAILS")
    @Expose
    private ProviderDetail providerdetails;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFacility() {
        return facility;
    }

    public void setFacility(String facility) {
        this.facility = facility;
    }

    public String getStrategy() {
        return strategy;
    }

    public void setStrategy(String strategy) {
        this.strategy = strategy;
    }

    public ProviderDetail getProviderdetails() {
        return providerdetails;
    }

    public void setProviderdetails(ProviderDetail providerdetails) {
        this.providerdetails = providerdetails;
    }

}