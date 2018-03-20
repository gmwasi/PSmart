package org.kenyahmis.psmartlibrary.Models.SHR;

/**
 * Created by Muhoro on 2/24/2018.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.DiffResult;
import org.apache.commons.lang3.builder.DiffBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

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

    public static boolean compare(HIVTest hivTest, HIVTest hivTest1) {
        if(hivTest.date.equals(hivTest1.date) && hivTest.facility.equals(hivTest1.facility) && hivTest.type.equals(hivTest1.type)){
            return true;
        }
        return false;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null){
            return false;
        }
        if (obj instanceof HIVTest) {
            return ((HIVTest) obj).date.equals(date) && ((HIVTest) obj).facility.equals(facility) && ((HIVTest) obj).type.equals(type);
        }
        return false;
    }
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + (this.type + this.facility + this.date).hashCode();
        return hash;
    }

    //@Override
    public DiffResult diff(HIVTest hivTest) {
        return new DiffBuilder(this, hivTest, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("HIV Test Date: ", this.date, hivTest.date)
                .append("HIV Test Facility: ", this.facility, hivTest.facility)
                .append("HIV Test Type: ", this.type, hivTest.type)
                .build();
    }

}