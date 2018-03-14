package org.kenyahmis.psmartlibrary;


import org.kenyahmis.psmartlibrary.Models.AcosCardResponse;
import org.kenyahmis.psmartlibrary.Models.HexString;
import org.kenyahmis.psmartlibrary.userFiles.UserFile;

/**
 * Created by GMwasi on 2/10/2018.
 */

interface CardReader {

    // Returns:
    //     Data read from the card as byte array
    AcosCardResponse ReadCard();

    String readUserFile(UserFile userFile);

    String writeUserFile(String data, UserFile userFile);

    void clean();

    // Params:
    //Data to be written on card as byte array
    // Returns:
    //     Message stating if written successfully or if not error message
    byte[] WriteCard(String data);
}
