package org.kenyahmis.psmartlibrary.Models;

import org.kenyahmis.psmartlibrary.Models.Addendum.Addendum;

/**
 * Created by Muhoro on 2/27/2018.
 */

public class TransmissionMessage {

    private String SHR; // this is encrypted SHR
    private Addendum addendum;

    public TransmissionMessage(String shr, Addendum addendum){
        setSHR(shr);
        setAddendum(addendum);
    }

   public String getSHR() {
        return SHR;
    }

    public void setSHR(String SHR) {
        this.SHR = SHR;
    }

    public Addendum getAddendum() {
        return addendum;
    }

    public void setAddendum(Addendum addendum) {
        this.addendum = addendum;
    }
}
