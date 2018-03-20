package org.kenyahmis.psmartlibrary;


import org.kenyahmis.psmartlibrary.Models.AcosCardResponse;
import org.kenyahmis.psmartlibrary.Models.HexString;
import org.kenyahmis.psmartlibrary.Models.Response;
import org.kenyahmis.psmartlibrary.userFiles.UserFile;

import java.util.List;

/**
 * Created by GMwasi on 2/10/2018.
 */

interface CardReader {

    // Returns:
    //     Data read from the card as byte array
    Response ReadCard();

    String readArray(UserFile userFile);

    String writeUserFile(UserFile userFile, String data, byte recordNumber);

    void writeArray(List<String> elements, UserFile userFile);

    void hardClean();

    void softClean();

    // Params:
    //Data to be written on card as byte array
    // Returns:
    //     Message stating if written successfully or if not error message
    byte[] WriteCard(String data);

    void powerOff();

    String getCardSerial();
}
