package org.kenyahmis.psmartlibrary.Models.SHR;

/**
 * Created by Muhoro on 2/24/2018.
 */

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PatientIdentification {

    @SerializedName("EXTERNAL_PATIENT_ID")
    @Expose
    private ExternalPatientId externalpatientid;
    @SerializedName("INTERNAL_PATIENT_ID")
    @Expose
    private List<InternalPatientId> internalpatientids = null;
    @SerializedName("PATIENT_NAME")
    @Expose
    private FullName patientname;
    @SerializedName("DATE_OF_BIRTH")
    @Expose
    private String dateofbirth;
    @SerializedName("DATE_OF_BIRTH_PRECISION")
    @Expose
    private String dateofbirthprecision;
    @SerializedName("SEX")
    @Expose
    private String sex;
    @SerializedName("DEATH_DATE")
    @Expose
    private String deathdate;
    @SerializedName("DEATH_INDICATOR")
    @Expose
    private String deathindicator;
    @SerializedName("PATIENT_ADDRESS")
    @Expose
    private PatientAddress patientaddress;
    @SerializedName("PHONE_NUMBER")
    @Expose
    private String phonenumber;
    @SerializedName("MARITAL_STATUS")
    @Expose
    private String maritalstatus;
    @SerializedName("MOTHER_DETAILS")
    @Expose
    private MotherDetail motherDetail;

    public ExternalPatientId getExternalpatientid() {
        return externalpatientid;
    }

    public void setExternalpatientid(ExternalPatientId externalPatientId) {
        this.externalpatientid = externalPatientId;
    }

    public List<InternalPatientId> getInternalpatientids() {
        return internalpatientids;
    }

    public void setInternalpatientids(List<InternalPatientId> internalPatientIds) {
        this.internalpatientids = internalPatientIds;
    }

    public FullName getPatientname() {
        return patientname;
    }

    public void setPatientname(FullName patientname) {
        this.patientname = patientname;
    }

    public String getDateofbirth() {
        return dateofbirth;
    }

    public void setDateofbirth(String dateofbirth) {
        this.dateofbirth = dateofbirth;
    }

    public String getDateofbirthprecision() {
        return dateofbirthprecision;
    }

    public void setDateofbirthprecision(String dateofbirthprecision) {
        this.dateofbirthprecision = dateofbirthprecision;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getDeathdate() {
        return deathdate;
    }

    public void setDeathdate(String deathdate) {
        this.deathdate = deathdate;
    }

    public String getDeathindicator() {
        return deathindicator;
    }

    public void setDeathindicator(String deathindicator) {
        this.deathindicator = deathindicator;
    }

    public PatientAddress getPatientaddress() {
        return patientaddress;
    }

    public void setPatientaddress(PatientAddress patientAddress) {
        this.patientaddress = patientAddress;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getMaritalstatus() {
        return maritalstatus;
    }

    public void setMaritalstatus(String maritalstatus) {
        this.maritalstatus = maritalstatus;
    }

    public MotherDetail getMotherDetail() {
        return motherDetail;
    }

    public void setMotherDetail(MotherDetail motherDetail) {
        this.motherDetail = motherDetail;
    }

}