package org.kenyahmis.psmartlibrary.Models.SHR;

/**
 * Created by Muhoro on 2/24/2018.
 */

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MergePatientInformation {

    @SerializedName("PRIOR_INTERNAL_IDENTIFIERS")
    @Expose
    private List<PriorInternalIdentifier> priorInternalIdentifiers = null;

    public List<PriorInternalIdentifier> getPriorInternalIdentifiers() {
        return priorInternalIdentifiers;
    }

    public void setPriorInternalIdentifiers(List<PriorInternalIdentifier> priorInternalIdentifiers) {
        this.priorInternalIdentifiers = priorInternalIdentifiers;
    }

}
