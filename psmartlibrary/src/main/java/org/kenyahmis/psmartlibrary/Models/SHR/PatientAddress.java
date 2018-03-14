package org.kenyahmis.psmartlibrary.Models.SHR;

/**
 * Created by Muhoro on 2/24/2018.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PatientAddress {

    @SerializedName("PHYSICAL_ADDRESS")
    @Expose
    private PhysicalAddress physicalAddress;

    @SerializedName("POSTAL_ADDRESS")
    @Expose
    private String postaladdress;

    public PhysicalAddress getPhysicalAddress() {
        return physicalAddress;
    }

    public void setPhysicalAddress(PhysicalAddress physicalAddress) {
        this.physicalAddress = physicalAddress;
    }

    public String getPostaladdress() {
        return postaladdress;
    }

    public void setPostaladdress(String postaladdress) {
        this.postaladdress = postaladdress;
    }

}
