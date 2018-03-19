package org.kenyahmis.psmartlibrary.Models.SHR;

/**
 * Created by Muhoro on 2/24/2018.
 */

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SHRMessage {

    @SerializedName("VERSION")
    @Expose
    private String version;
    @SerializedName("PATIENT_IDENTIFICATION")
    @Expose
    private PatientIdentification patientIdentification;
    @SerializedName("NEXT_OF_KIN")
    @Expose
    private List<NextOfKin> nextofkin = null;
    @SerializedName("HIV_TESTS")
    @Expose
    private List<HIVTest> hivTests = null;
    @SerializedName("IMMUNIZATIONS")
    @Expose
    private List<Immunization> immunizations = null;
    @SerializedName("MERGE_PATIENT_INFORMATION")
    @Expose
    private MergePatientInformation mergePatientInformation;
    @SerializedName("CARD_DETAILS")
    @Expose
    private CardDetail cardDetail;

    public PatientIdentification getPatientIdentification() {
        return patientIdentification;
    }

    public void setPatientIdentification(PatientIdentification patientIdentification) {
        this.patientIdentification = patientIdentification;
    }

    public List<NextOfKin> getNextofkin() {
        return nextofkin;
    }

    public void setNextofkin(List<NextOfKin> nextOfKins) {
        this.nextofkin = nextOfKins;
    }

    public List<HIVTest> getHivTests() {
        return hivTests;
    }

    public void setHivTests(List<HIVTest> hivTests) {
        this.hivTests = hivTests;
    }

    public List<Immunization> getImmunizations() {
        return immunizations;
    }

    public void setImmunizations(List<Immunization> immunizations) {
        this.immunizations = immunizations;
    }

    public MergePatientInformation getMergePatientInformation() {
        return mergePatientInformation;
    }

    public void setMergePatientInformation(MergePatientInformation mergePatientInformation) {
        this.mergePatientInformation = mergePatientInformation;
    }

    public CardDetail getCardDetail() {
        return cardDetail;
    }

    public void setCardDetail(CardDetail cardDetail) {
        this.cardDetail = cardDetail;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
