package org.kenyahmis.psmartlibrary.Models.SHR;

/**
 * Created by Muhoro on 2/24/2018.
 */

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MotherDetail {

    @SerializedName("MOTHER_NAME")
    @Expose
    private FullName mothername;

    @SerializedName("MOTHER_IDENTIFIER")
    @Expose
    private List<MotherIdentifier> motherIdentifiers = null;

    public FullName getMotherName() {
        return mothername;
    }

    public void setMothername(FullName mothername) {
        this.mothername = mothername;
    }

    public List<MotherIdentifier> getMotherIdentifiers() {
        return motherIdentifiers;
    }

    public void setMotherIdentifiers(List<MotherIdentifier> motherIdentifiers) {
        this.motherIdentifiers = motherIdentifiers;
    }

}
