package org.kenyahmis.psmartlibrary;

import android.util.Log;

import com.acs.bluetooth.BluetoothReader;
import org.kenyahmis.psmartlibrary.Models.Addendum.Addendum;
import org.kenyahmis.psmartlibrary.Models.Addendum.Identifier;
import org.kenyahmis.psmartlibrary.Models.ReadResponse;
import org.kenyahmis.psmartlibrary.Models.Response;
import org.kenyahmis.psmartlibrary.Models.SHR.CardDetail;
import org.kenyahmis.psmartlibrary.Models.SHR.HIVTest;
import org.kenyahmis.psmartlibrary.Models.SHR.Immunization;
import org.kenyahmis.psmartlibrary.Models.SHR.InternalPatientId;
import org.kenyahmis.psmartlibrary.Models.SHR.SHRMessage;
import org.kenyahmis.psmartlibrary.Models.TransmissionMessage;
import org.kenyahmis.psmartlibrary.Models.WriteResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by GMwasi on 2/9/2018.
 */

public class PSmartCard implements Card {

    private CardReader reader;
    private Encryption encryption;
    private Compression compression;
    private Serializer serializer;
    private Deserializer deserializer;


    public PSmartCard(BluetoothReader btreader){
        reader = new AcrBluetooth(btreader);
        this.compression = new Compression();
        this.encryption = new Encryption();
        this.serializer = new Serializer();
        this.deserializer = new Deserializer();
    }

    @Override
    public Response Read() {
        if (reader != null) {

            // read immunization
            String cardDetailsString = reader.readUserFile(SmartCardUtils.getUserFile(SmartCardUtils.CARD_DETAILS_USER_FILE_NAME));
            String immunizationString = reader.readUserFile(SmartCardUtils.getUserFile(SmartCardUtils.IMMUNIZATION_USER_FILE_NAME));
            String hivTestString = reader.readUserFile(SmartCardUtils.getUserFile(SmartCardUtils.HIV_TEST_USER_FILE_NAME));
            //String identifiers = reader.readUserFile(SmartCardUtils.getUserFile(SmartCardUtils.IDENTIFIERS_USER_FILE_ADDRESS_NAME));

            CardDetail cardDetail = deserializer.deserialize(CardDetail.class, cardDetailsString);
            Immunization[] immunizationArray = deserializer.deserialize(Immunization[].class, immunizationString);
            List<Immunization> immunizations = new ArrayList<>();
            for (Immunization immunization: immunizationArray ) {
                immunizations.add(immunization);
            }

            HIVTest[] hivTestsArray = deserializer.deserialize(HIVTest[].class, hivTestString);
            List<HIVTest> hivTests = new ArrayList<>();
            for (HIVTest hivTest : hivTestsArray ) {
                hivTests.add(hivTest);
            }

            SHRMessage shrMessage = new SHRMessage();
            shrMessage.setCardDetail(cardDetail);
            shrMessage.setImmunizations(immunizations);
            shrMessage.setHivTests(hivTests);

            return new ReadResponse(serializer.serialize(shrMessage), null);

////            String cardData = reader.ReadCard().hexString;
////            String encryptedData = Utils.hexToString(cardData);
//
//            /*String decompressedMessage = "";
//            try {
//                decompressedMessage = compression.Decompress(cardData);
//            } catch (IOException e) {
//                e.getMessage();
//            }*/
//
//            String decryptedMessage = encryption.decrypt(EncrytionKeys.SHR_KEY, encryptedData);
//
//            // Mock Message
//            String mockMessage = "{\n" +
//                    "  \"PATIENT_IDENTIFICATION\": {\n" +
//                    "    \"EXTERNAL_PATIENT_ID\": {\n" +
//                    "      \"ID\": \"110ec58a-a0f2-4ac4-8393-c866d813b8d1\",\n" +
//                    "      \"IDENTIFIER_TYPE\": \"GODS_NUMBER\",\n" +
//                    "      \"ASSIGNING_AUTHORITY\": \"MPI\",\n" +
//                    "      \"ASSIGNING_FACILITY\": \"10829\"\n" +
//                    "    },\n" +
//                    "    \"INTERNAL_PATIENT_ID\": [\n" +
//                    "      {\n" +
//                    "        \"ID\": \"12345678-ADFGHJY-0987654-NHYI890\",\n" +
//                    "        \"IDENTIFIER_TYPE\": \"CARD_SERIAL_NUMBER\",\n" +
//                    "        \"ASSIGNING_AUTHORITY\": \"CARD_REGISTRY\",\n" +
//                    "        \"ASSIGNING_FACILITY\": \"10829\"\n" +
//                    "      },\n" +
//                    "      {\n" +
//                    "        \"ID\": \"12345678\",\n" +
//                    "        \"IDENTIFIER_TYPE\": \"HEI_NUMBER\",\n" +
//                    "        \"ASSIGNING_AUTHORITY\": \"MCH\",\n" +
//                    "        \"ASSIGNING_FACILITY\": \"10829\"\n" +
//                    "      },\n" +
//                    "      {\n" +
//                    "        \"ID\": \"12345678\",\n" +
//                    "        \"IDENTIFIER_TYPE\": \"CCC_NUMBER\",\n" +
//                    "        \"ASSIGNING_AUTHORITY\": \"CCC\",\n" +
//                    "        \"ASSIGNING_FACILITY\": \"10829\"\n" +
//                    "      },\n" +
//                    "      {\n" +
//                    "        \"ID\": \"001\",\n" +
//                    "        \"IDENTIFIER_TYPE\": \"HTS_NUMBER\",\n" +
//                    "        \"ASSIGNING_AUTHORITY\": \"HTS\",\n" +
//                    "        \"ASSIGNING_FACILITY\": \"10829\"\n" +
//                    "      },\n" +
//                    "      {\n" +
//                    "        \"ID\": \"12345678\",\n" +
//                    "        \"IDENTIFIER_TYPE\": \"PMTCT_NUMBER\",\n" +
//                    "        \"ASSIGNING_AUTHORITY\": \"PMTCT\",\n" +
//                    "        \"ASSIGNING_FACILITY\": \"10829\"\n" +
//                    "      }\n" +
//                    "    ],\n" +
//                    "    \"PATIENT_NAME\": {\n" +
//                    "      \"FIRST_NAME\": \"THERESA\",\n" +
//                    "      \"MIDDLE_NAME\": \"MAY\",\n" +
//                    "      \"LAST_NAME\": \"WAIRIMU\"\n" +
//                    "    },\n" +
//                    "    \"DATE_OF_BIRTH\": \"20171111\",\n" +
//                    "    \"DATE_OF_BIRTH_PRECISION\": \"ESTIMATED/EXACT\",\n" +
//                    "    \"SEX\": \"F\",\n" +
//                    "    \"DEATH_DATE\": \"\",\n" +
//                    "    \"DEATH_INDICATOR\": \"N\",\n" +
//                    "    \"PATIENT_ADDRESS\": {\n" +
//                    "      \"PHYSICAL_ADDRESS\": {\n" +
//                    "        \"VILLAGE\": \"KWAKIMANI\",\n" +
//                    "        \"WARD\": \"KIMANINI\",\n" +
//                    "        \"SUB_COUNTY\": \"KIAMBU EAST\",\n" +
//                    "        \"COUNTY\": \"KIAMBU\",\n" +
//                    "        \"NEAREST_LANDMARK\": \"KIAMBU EAST\"\n" +
//                    "      },\n" +
//                    "      \"POSTAL_ADDRESS\": \"789 KIAMBU\"\n" +
//                    "    },\n" +
//                    "    \"PHONE_NUMBER\": \"254720278654\",\n" +
//                    "    \"MARITAL_STATUS\": \"\",\n" +
//                    "    \"MOTHER_DETAILS\": {\n" +
//                    "      \"MOTHER_NAME\": {\n" +
//                    "        \"FIRST_NAME\": \"WAMUYU\",\n" +
//                    "        \"MIDDLE_NAME\": \"MARY\",\n" +
//                    "        \"LAST_NAME\": \"WAITHERA\"\n" +
//                    "      },\n" +
//                    "      \"MOTHER_IDENTIFIER\": [\n" +
//                    "        {\n" +
//                    "          \"ID\": \"1234567\",\n" +
//                    "          \"IDENTIFIER_TYPE\": \"NATIONAL_ID\",\n" +
//                    "          \"ASSIGNING_AUTHORITY\": \"GOK\",\n" +
//                    "          \"ASSIGNING_FACILITY\": \"\"\n" +
//                    "        },\n" +
//                    "        {\n" +
//                    "          \"ID\": \"12345678\",\n" +
//                    "          \"IDENTIFIER_TYPE\": \"NHIF\",\n" +
//                    "          \"ASSIGNING_AUTHORITY\": \"NHIF\",\n" +
//                    "          \"ASSIGNING_FACILITY\": \"\"\n" +
//                    "        },\n" +
//                    "        {\n" +
//                    "          \"ID\": \"12345-67890\",\n" +
//                    "          \"IDENTIFIER_TYPE\": \"CCC_NUMBER\",\n" +
//                    "          \"ASSIGNING_AUTHORITY\": \"CCC\",\n" +
//                    "          \"ASSIGNING_FACILITY\": \"10829\"\n" +
//                    "        },\n" +
//                    "        {\n" +
//                    "          \"ID\": \"ABC567\",\n" +
//                    "          \"IDENTIFIER_TYPE\": \"ANC_NUMBER\",\n" +
//                    "          \"ASSIGNING_AUTHORITY\": \"ANC\",\n" +
//                    "          \"ASSIGNING_FACILITY\": \"10829\"\n" +
//                    "        }\n" +
//                    "      ]\n" +
//                    "    }\n" +
//                    "  },\n" +
//                    "  \"NEXT_OF_KIN\": [\n" +
//                    "    {\n" +
//                    "      \"NOK_NAME\": {\n" +
//                    "        \"FIRST_NAME\": \"WAIGURU\",\n" +
//                    "        \"MIDDLE_NAME\": \"KIMUTAI\",\n" +
//                    "        \"LAST_NAME\": \"WANJOKI\"\n" +
//                    "      },\n" +
//                    "      \"RELATIONSHIP\": \"**AS DEFINED IN GREENCARD\",\n" +
//                    "      \"ADDRESS\": \"4678 KIAMBU\",\n" +
//                    "      \"PHONE_NUMBER\": \"25489767899\",\n" +
//                    "      \"SEX\": \"F\",\n" +
//                    "      \"DATE_OF_BIRTH\": \"19871022\",\n" +
//                    "      \"CONTACT_ROLE\": \"T\"\n" +
//                    "    }\n" +
//                    "  ],\n" +
//                    "  \"HIV_TESTS\": [\n" +
//                    "    {\n" +
//                    "      \"DATE\": \"20180101\",\n" +
//                    "      \"RESULT\": \"POSITIVE/NEGATIVE/INCONCLUSIVE\",\n" +
//                    "      \"TYPE\": \"SCREENING/CONFIRMATORY\",\n" +
//                    "      \"FACILITY\": \"10829\",\n" +
//                    "      \"STRATEGY\": \"HP/NP/VI/VS/HB/MO/O\",\n" +
//                    "      \"PROVIDER_DETAILS\": {\n" +
//                    "        \"NAME\": \"AFYA JIJINI***STILL IN REVIEW\"\n" +
//                    "      }\n" +
//                    "    }\n" +
//                    "  ],\n" +
//                    "  \"IMMUNIZATIONS\": [\n" +
//                    "    {\n" +
//                    "      \"NAME\": \"BCG/OPV_AT_BIRTH/OPV1/OPV2/OPV3/PCV10-1/PCV10-2/PCV10-3/PENTA1/PENTA2/PENTA3/MEASLES6/MEASLES9/MEASLES18/ROTA1/ROTA2\",\n" +
//                    "      \"DATE_ADMINISTERED\": \"20180101\"\n" +
//                    "    }\n" +
//                    "  ],\n" +
//                    "  \"MERGE_PATIENT_INFORMATION\": {\n" +
//                    "    \"PRIOR_INTERNAL_IDENTIFIERS\": [\n" +
//                    "      {\n" +
//                    "        \"ID\": \"12345678-67676767-0987654-XXXXYYYY\",\n" +
//                    "        \"IDENTIFIER_TYPE\": \"CARD_SERIAL_NUMBER\",\n" +
//                    "        \"ASSIGNING_AUTHORITY\": \"CARD_REGISTRY\",\n" +
//                    "        \"ASSIGNING_FACILITY\": \"12345\",\n" +
//                    "        \"REPLACEMENT_REASON\": \"LOST\"\n" +
//                    "      }\n" +
//                    "    ]\n" +
//                    "  },\n" +
//                    "  \"CARD_DETAILS\": {\n" +
//                    "    \"STATUS\": \"ACTIVE/INACTIVE\",\n" +
//                    "    \"REASON\": \"\",\n" +
//                    "    \"LAST_UPDATED\": \"20180101\",\n" +
//                    "    \"LAST_UPDATED_FACILITY\": \"10829\"\n" +
//                    "  }\n" +
//                    "}";
//            //todo: replace mock message response with th read message
//            Response response = new ReadResponse(decryptedMessage, null);
//            return response;
        }
        return null;
    }


    @Override
    public Response Write(String shr){
        SHRMessage shrMessage = deserializer.deserialize(SHRMessage.class, shr);
        String cardDetailString = serializer.serialize(shrMessage.getCardDetail());
        String immunizationString = serializer.serialize(shrMessage.getImmunizations());
        String hivTestString = serializer.serialize(shrMessage.getHivTests());

        reader.clean();
        Log.i("WRITE", "Started writing");
        Log.i("WRITE", "card details length" + cardDetailString.getBytes().length);
        Log.i("WRITE", "card details length" + immunizationString.getBytes().length);
        Log.i("WRITE", "card details length" + hivTestString.getBytes().length);
        reader.writeUserFile(cardDetailString, SmartCardUtils.getUserFile(SmartCardUtils.CARD_DETAILS_USER_FILE_NAME));
        reader.writeUserFile(immunizationString, SmartCardUtils.getUserFile(SmartCardUtils.IMMUNIZATION_USER_FILE_NAME));
        reader.writeUserFile(hivTestString, SmartCardUtils.getUserFile(SmartCardUtils.HIV_TEST_USER_FILE_NAME));

        return null;
    }


//    @Override
//    public Response Write(String shr) {
//
//        SHRMessage SHRMessage = deserializer.deserialize(SHRMessage.class, shr);
//
//        String cardDetailString = serializer.serialize(SHRMessage.);
//
//
//        Addendum addendum = new Addendum();
//        addendum.setCardDetail(SHRMessage.getCardDetail());
//
//        List<InternalPatientId> internalPatientIds =  SHRMessage.getPatientIdentification().getInternalpatientids();
//        List<Identifier> addendumIdentifiers = new ArrayList<>();
//
//        // loop through the internal identifiers to construct Addendum Identifier
//        for (InternalPatientId id: internalPatientIds ) {
//            Identifier identifier = new Identifier();
//            identifier.setId(id.getID());
//            identifier.setAssigningAuthority(id.getAssigningauthority());
//            identifier.setAssigningFacility(id.getAssigningauthority());
//            identifier.setIdentifierType(id.getIdentifiertype());
//            addendumIdentifiers.add(identifier);
//        }
//
//        addendum.setIdentifiers(addendumIdentifiers);
//
//
//        String encryptedSHR = encryption.encrypt(EncrytionKeys.SHR_KEY, shr);
//
//        try {
//            byte[] compressedMessage = compression.Compress(encryptedSHR);
//            if (reader!=null) {
//                //byte[] output = reader.WriteCard(compressedMessage);
//            }
//        }
//        catch (IOException e) {
//            e.printStackTrace();
//            e.getMessage();
//        }
//        // TODO: if output is success return valid response
//        // TODO: else return invalid response according to the error
//
//        TransmissionMessage transmitMessage = new TransmissionMessage(encryptedSHR, addendum);
//        String transmitString = serializer.serialize(transmitMessage);
//        Response response = new WriteResponse(transmitString, null);
//        return response;
//    }
}
