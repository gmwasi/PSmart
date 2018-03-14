package org.kenyahmis.psmartlibrary.Models.Addendum;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.kenyahmis.psmartlibrary.Models.SHR.CardDetail;
import java.util.List;

/**
 * Created by Muhoro on 2/27/2018.
 */

public class Addendum {

    @SerializedName("CARD_DETAILS")
    @Expose
    private CardDetail CardDetail;

    @SerializedName("IDENTIFIERS")
    @Expose
    private List<Identifier> Identifiers;

    public CardDetail getCardDetail() {
        return CardDetail;
    }

    public void setCardDetail(CardDetail cardDetail) {
        CardDetail = cardDetail;
    }

    public List<Identifier> getIdentifiers() {
        return Identifiers;
    }

    public void setIdentifiers(List<Identifier> identifiers) {
        Identifiers = identifiers;
    }
}
