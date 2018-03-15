package org.kenyahmis.psmartlibrary;

//audiojack library

import org.kenyahmis.psmartlibrary.Models.AcosCardResponse;
import org.kenyahmis.psmartlibrary.Models.Response;
import org.kenyahmis.psmartlibrary.userFiles.UserFile;

import java.util.List;

class Acr3x implements CardReader{


     @Override
     public Response ReadCard() {
         return null;
     }

    @Override
    public String readArray(UserFile userFile) {
        return null;
    }

    @Override
    public String writeUserFile(UserFile userFile,String data, byte recordNumber) {
        return null;
    }

    @Override
    public void writeArray(List<String> elements, UserFile userFile) {

    }

    @Override
    public void clean() {

    }

    @Override
     public byte[] WriteCard(String data) {
         return null;
     }

    @Override
    public void powerOff() {

    }
}
