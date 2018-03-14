package org.kenyahmis.psmartlibrary.Models.SHR;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Muhoro on 2/24/2018.
 */

public class FullName {

    @SerializedName("FIRST_NAME")
    @Expose
    private String firstname;

    @SerializedName("MIDDLE_NAME")
    @Expose
    private String middlename;

    @SerializedName("LAST_NAME")
    @Expose
    private String lastname;


    public String getfirstname() {
        return firstname;
    }

    public void setfirstname(String fIRSTNAME) {
        this.firstname = fIRSTNAME;
    }

    public String getmiddlename() {
        return middlename;
    }

    public void setmiddlename(String mIDDLENAME) {
        this.middlename = mIDDLENAME;
    }

    public String getlastname() {
        return lastname;
    }

    public void setlastname(String lASTNAME) {
        this.lastname = lASTNAME;
    }

}
