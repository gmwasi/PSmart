package org.kenyahmis.psmartlibrary;

//audiojack library

import org.kenyahmis.psmartlibrary.Models.AcosCardResponse;
import org.kenyahmis.psmartlibrary.userFiles.UserFile;

class Acr3x implements CardReader{


     @Override
     public AcosCardResponse ReadCard() {
         return null;
     }

    @Override
    public String readUserFile(UserFile userFile) {
        return null;
    }

    @Override
    public String writeUserFile(String data, UserFile userFile) {
        return null;
    }

    @Override
    public void clean() {

    }

    @Override
     public byte[] WriteCard(String data) {
         return null;
     }
 }
