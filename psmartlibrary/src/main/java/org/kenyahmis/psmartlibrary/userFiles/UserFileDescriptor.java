package org.kenyahmis.psmartlibrary.userFiles;
import java.util.Arrays;
import java.util.List;

public class UserFileDescriptor {
    byte[] fileId;
    int expLength;

    public UserFileDescriptor(byte[] fileId, int expLength) {
        this.fileId = fileId;
        this.expLength = expLength;
    }

    public byte[] getFileId() {
        return fileId;
    }

    public void setFileId(byte[] fileId) {
        this.fileId = fileId;
    }

    public int getExpLength() {
        return expLength;
    }

    public void setExpLength(int expLength) {
        this.expLength = expLength;
    }

    public static List<UserFileDescriptor> getUserFileDescriptors () {
        return Arrays.asList(

        );


    }
}