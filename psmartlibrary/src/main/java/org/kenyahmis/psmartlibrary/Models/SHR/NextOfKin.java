package org.kenyahmis.psmartlibrary.Models.SHR;

/**
 * Created by Muhoro on 2/24/2018.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NextOfKin {

    @SerializedName("NOK_NAME")
    @Expose
    private FullName nokname;

    @SerializedName("RELATIONSHIP")
    @Expose
    private String relationship;

    @SerializedName("ADDRESS")
    @Expose
    private String address;

    @SerializedName("PHONE_NUMBER")
    @Expose
    private String phonenumber;

    @SerializedName("SEX")
    @Expose
    private String sex;

    @SerializedName("DATE_OF_BIRTH")
    @Expose
    private String dateofbirth;

    @SerializedName("CONTACT_ROLE")
    @Expose
    private String contactrole;


    public FullName getNokname() {
        return nokname;
    }

    public void setNokname(FullName nokname) {
        this.nokname = nokname;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getphonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getDateofbirth() {
        return dateofbirth;
    }

    public void setDateofbirth(String dateofbirth) {
        this.dateofbirth = dateofbirth;
    }

    public String getContactrole() {
        return contactrole;
    }

    public void setContactrole(String contactrole) {
        this.contactrole = contactrole;
    }

}