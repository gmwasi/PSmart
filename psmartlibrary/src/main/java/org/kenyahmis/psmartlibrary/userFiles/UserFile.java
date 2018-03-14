package org.kenyahmis.psmartlibrary.userFiles;

/**
 * Created by Muhoro on 3/13/2018.
 */


public class UserFile {
    String name;
    String description;
    UserFileDescriptor fileDescriptor;

    public UserFile(String name, String description, UserFileDescriptor fileDescriptor) {
        this.name = name;
        this.description = description;
        this.fileDescriptor = fileDescriptor;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UserFileDescriptor getFileDescriptor() {
        return fileDescriptor;
    }

    public void setFileDescriptor(UserFileDescriptor fileDescriptor) {
        this.fileDescriptor = fileDescriptor;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}