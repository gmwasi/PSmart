package org.kenyahmis.psmartlibrary;

/**
 * Created by Muhoro on 3/13/2018.
 */


import org.kenyahmis.psmartlibrary.userFiles.UserFile;
import org.kenyahmis.psmartlibrary.userFiles.UserFileDescriptor;

import java.util.HashMap;
import java.util.Map;

public class SmartCardUtils {
    public static final String CARD_DETAILS_USER_FILE = "Card Details";
    public static final String HIV_TEST_USER_FILE = "HIV Tests";
    public static final String IMMUNIZATION_USER_FILE = "Immunizations";
    public static final String NEXT_OF_KIN_USER_FILE = "Next of Kin";
    public static final String IDENTIFIERS_USER_FILE_INTERNAL = "Internal Patient Identifiers";
    public static final String IDENTIFIERS_USER_FILE_EXTERNAL = "External Patient Identifiers";
    public static final String IDENTIFIERS_USER_FILE_DEMOGRAPHICS = "Patient Names";
    public static final String IDENTIFIERS_USER_FILE_ADDRESS = "Patient Address";
    public static final String IDENTIFIERS_USER_FILE_DEMOGRAPHICS_OTHERS = "Additional Demographics";
    public static final String IDENTIFIERS_USER_FILE_MOTHERDETAILS="Mother Name";
    public static final String IDENTIFIERS_USER_FILE_MOTHERIDENTIFIER="Mother Identifier";

    public static  final String CARD_DETAILS_USER_FILE_NAME = "AA 00";
    public static final String HIV_TEST_USER_FILE_NAME = "CC 00";
    public static final String IMMUNIZATION_USER_FILE_NAME = "BB 00";
    public static final String NEXT_OF_KIN_USER_FILE_NAME = "DD 55";
    public static final String IDENTIFIERS_USER_FILE_INTERNAL_NAME = "DD 11";
    public static final String IDENTIFIERS_USER_FILE_EXTERNAL_NAME = "DD 00";
    public static final String IDENTIFIERS_USER_FILE_DEMOGRAPHICS_NAME = "DD 22";
    public static final String IDENTIFIERS_USER_FILE_ADDRESS_NAME = "DD 33";
    public static final String IDENTIFIERS_USER_FILE_DEMOGRAPHICS_OTHERS_NAME = "DD 44";
    public static final String IDENTIFIERS_USER_FILE_MOTHER_DETAIL_NAME="EE 00";
    public static final String IDENTIFIERS_USER_FILE_MOTHER_IDENTIFIER_NAME="EE 11";


    public static UserFile getUserFile (String name) {
        if (name != null || !name.isEmpty()) {
            return getAllUserFiles().get(name);
        }
        return null ;
    }

    public static Map<String, UserFile> getAllUserFiles() {
        Map<String, UserFile> allUserFiles = new HashMap<>();

        allUserFiles.put(SmartCardUtils.CARD_DETAILS_USER_FILE_NAME,
                new UserFile(SmartCardUtils.CARD_DETAILS_USER_FILE_NAME, CARD_DETAILS_USER_FILE, new UserFileDescriptor(new byte[] { (byte)0xAA, (byte)0x00 },255 )));
        allUserFiles.put(SmartCardUtils.IMMUNIZATION_USER_FILE_NAME,
                new UserFile(SmartCardUtils.IMMUNIZATION_USER_FILE_NAME, IMMUNIZATION_USER_FILE,  new UserFileDescriptor(new byte[] { (byte)0xBB, (byte)0x00 }, 255 )));

        allUserFiles.put(SmartCardUtils.HIV_TEST_USER_FILE_NAME,
                new UserFile(SmartCardUtils.HIV_TEST_USER_FILE_NAME, HIV_TEST_USER_FILE,  new UserFileDescriptor(new byte[] { (byte)0xCC, (byte)0x00 }, 255 )));

        allUserFiles.put(SmartCardUtils.IDENTIFIERS_USER_FILE_EXTERNAL_NAME,
                new UserFile(SmartCardUtils.IDENTIFIERS_USER_FILE_EXTERNAL_NAME, IDENTIFIERS_USER_FILE_EXTERNAL,  new UserFileDescriptor(new byte[] { (byte)0xDD, (byte)0x00 }, 255 )));

        allUserFiles.put(SmartCardUtils.IDENTIFIERS_USER_FILE_INTERNAL_NAME,
                new UserFile(SmartCardUtils.IDENTIFIERS_USER_FILE_INTERNAL_NAME, IDENTIFIERS_USER_FILE_INTERNAL,  new UserFileDescriptor(new byte[] { (byte)0xDD, (byte)0x11 }, 255 )));

        allUserFiles.put(SmartCardUtils.IDENTIFIERS_USER_FILE_DEMOGRAPHICS_NAME,
                new UserFile(SmartCardUtils.IDENTIFIERS_USER_FILE_DEMOGRAPHICS_NAME, IDENTIFIERS_USER_FILE_DEMOGRAPHICS,  new UserFileDescriptor(new byte[] { (byte)0xDD, (byte)0x22 }, 255 )));

        allUserFiles.put(SmartCardUtils.IDENTIFIERS_USER_FILE_ADDRESS_NAME,
                new UserFile(SmartCardUtils.IDENTIFIERS_USER_FILE_ADDRESS_NAME, IDENTIFIERS_USER_FILE_ADDRESS,  new UserFileDescriptor(new byte[] { (byte)0xDD, (byte)0x33 }, 255 )));

        allUserFiles.put(SmartCardUtils.IDENTIFIERS_USER_FILE_MOTHER_DETAIL_NAME,
                new UserFile(SmartCardUtils.IDENTIFIERS_USER_FILE_MOTHER_DETAIL_NAME,IDENTIFIERS_USER_FILE_MOTHERDETAILS,new UserFileDescriptor(new byte[] { (byte)0xEE, (byte)0x00 },255 )));

        allUserFiles.put(SmartCardUtils.IDENTIFIERS_USER_FILE_MOTHER_IDENTIFIER_NAME,
                new UserFile(SmartCardUtils.IDENTIFIERS_USER_FILE_MOTHER_IDENTIFIER_NAME,IDENTIFIERS_USER_FILE_MOTHERIDENTIFIER,new UserFileDescriptor(new byte[] { (byte)0xEE, (byte)0x11 },255 )));

        return allUserFiles;
    }
}