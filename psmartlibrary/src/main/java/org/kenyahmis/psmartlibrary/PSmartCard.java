package org.kenyahmis.psmartlibrary;

import android.util.Log;

import com.acs.bluetooth.BluetoothReader;

import org.kenyahmis.psmartlibrary.Models.AcosCardResponse;
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
    public ReadResponse Read() {

            ReadResponse response = (ReadResponse) reader.ReadCard();
            String cardSerial = reader.getCardSerial();

            if(response.getErrors().isEmpty()) {
                SHRMessage shrMessage = deserializer.deserialize(SHRMessage.class, response.getMessage());

                InternalPatientId internalPatientId = new InternalPatientId();
                internalPatientId.setID(cardSerial);
                internalPatientId.setidentifiertype("CARD_SERIAL_NUMBER");
                internalPatientId.setAssigningfacility("");
                internalPatientId.setAssigningauthority("CARD_REGISTRY");
                shrMessage.getPatientIdentification().getInternalpatientids().add(internalPatientId);

                response = new ReadResponse(serializer.serialize(shrMessage), null);
            }
            return response;
       }


    @Override
    public Response Write(String shr){

        try {
            SHRMessage shrMessage = deserializer.deserialize(SHRMessage.class, shr);

            List<String> demographics = new ArrayList<>();
            StringBuilder otherDemographics = new StringBuilder();
            otherDemographics
                    .append("\"DATE_OF_BIRTH\": \"").append(shrMessage.getPatientIdentification().getDateofbirth()).append("\"")
                    .append(", \"DATE_OF_BIRTH_PRECISION\": \"").append(shrMessage.getPatientIdentification().getDateofbirthprecision()).append("\"")
                    .append(", \"SEX\": \"").append(shrMessage.getPatientIdentification().getSex()).append("\"")
                    .append(", \"DEATH_DATE\": \"").append(shrMessage.getPatientIdentification().getDeathdate()).append("\"")
                    .append(", \"DEATH_INDICATOR\": \"").append(shrMessage.getPatientIdentification().getDeathindicator()).append("\"")
                    .append(", \"PHONE_NUMBER\": \"").append(shrMessage.getPatientIdentification().getPhonenumber()).append("\"")
                    .append(", \"MARITAL_STATUS\": \"").append(shrMessage.getPatientIdentification().getMaritalstatus()).append("\"");
            String patientName = serializer.serialize(shrMessage.getPatientIdentification().getPatientname());
            demographics.add(patientName);
            demographics.add(otherDemographics.toString());
            String patientExternalIdentifiers = serializer.serialize(shrMessage.getPatientIdentification().getExternalpatientid());
            String cardDetails = serializer.serialize(shrMessage.getCardDetail());
            String motherDetails = serializer.serialize(shrMessage.getPatientIdentification().getMotherDetail().getMotherName());
            List<String> immunizationDetails = getStringArr(shrMessage, "IMMUNIZATION");
            List<String> hivTests = getStringArr(shrMessage, "HIV_TEST");
            List<String> internalIdentifiers = getStringArr(shrMessage, "INTERNAL_PATIENT_ID");
            List<String> motherIdentifiers = getStringArr(shrMessage, "MOTHER_IDENTIFIER");
            String addressDetails = serializer.serialize(shrMessage.getPatientIdentification().getPatientaddress());


            reader.clean();

            //write user files
            reader.writeUserFile(SmartCardUtils.getUserFile(SmartCardUtils.CARD_DETAILS_USER_FILE_NAME), cardDetails, (byte)0x00);
            Log.i("cardDetails","ok");
            reader.writeUserFile(SmartCardUtils.getUserFile(SmartCardUtils.IDENTIFIERS_USER_FILE_EXTERNAL_NAME), patientExternalIdentifiers, (byte)0x00);
            Log.i("patientExternal","ok");
            reader.writeUserFile(SmartCardUtils.getUserFile(SmartCardUtils.IDENTIFIERS_USER_FILE_ADDRESS_NAME), addressDetails, (byte)0x00);
            Log.i("addressDetails","ok");
            reader.writeUserFile(SmartCardUtils.getUserFile(SmartCardUtils.IDENTIFIERS_USER_FILE_MOTHER_DETAIL_NAME), motherDetails, (byte)0x00);
            Log.i("motherDetails","ok");

            //write arrays
            reader.writeArray(immunizationDetails, SmartCardUtils.getUserFile(SmartCardUtils.IMMUNIZATION_USER_FILE_NAME));
            Log.i("immunizationDetails","ok");
            reader.writeArray(hivTests, SmartCardUtils.getUserFile(SmartCardUtils.HIV_TEST_USER_FILE_NAME));
            Log.i("hivTests","ok");
            reader.writeArray(internalIdentifiers, SmartCardUtils.getUserFile(SmartCardUtils.IDENTIFIERS_USER_FILE_INTERNAL_NAME));
            Log.i("internalIdentifiers","ok");
            reader.writeArray(demographics, SmartCardUtils.getUserFile(SmartCardUtils.IDENTIFIERS_USER_FILE_DEMOGRAPHICS_NAME));
            Log.i("demographics","ok");
            reader.writeArray(motherIdentifiers, SmartCardUtils.getUserFile(SmartCardUtils.IDENTIFIERS_USER_FILE_MOTHER_IDENTIFIER_NAME));
            Log.i("motherIdentifiers","ok");

            // get serial
            String serial = reader.getCardSerial();

            reader.powerOff();

            // create addendum
            Addendum addendum = new Addendum();
            addendum.setCardDetail(shrMessage.getCardDetail());
            Identifier cardSerialIdentifier = new Identifier();
            cardSerialIdentifier.setId(serial);
            cardSerialIdentifier.setIdentifierType("CARD_SERIAL_NUMBER");
            cardSerialIdentifier.setAssigningFacility("");
            cardSerialIdentifier.setAssigningAuthority("CARD_REGISTRY");
            addendum.getIdentifiers().add(cardSerialIdentifier);

            List<InternalPatientId> internalPatientIds = shrMessage.getPatientIdentification().getInternalpatientids();
            List<Identifier> addendumIdentifiers = new ArrayList<>();

            // loop through the internal identifiers to construct Addendum Identifier
            for (InternalPatientId id : internalPatientIds) {
                Identifier identifier = new Identifier();
                identifier.setId(id.getID());
                identifier.setAssigningAuthority(id.getAssigningauthority());
                identifier.setAssigningFacility(id.getAssigningauthority());
                identifier.setIdentifierType(id.getIdentifiertype());
                addendumIdentifiers.add(identifier);
            }

            addendum.setIdentifiers(addendumIdentifiers);


            String encryptedSHR = encryption.encrypt(EncrytionKeys.SHR_KEY, shr);
            TransmissionMessage transmitMessage = new TransmissionMessage(encryptedSHR, addendum);
            String transmitString = serializer.serialize(transmitMessage);
            Response response = new WriteResponse(transmitString, null);
            return response;
        }

        catch (Exception ex){
            ex.printStackTrace();
            return null;
        }
    }

    public List<String> getStringArr(SHRMessage shr, String context){
        List<String> stringArr = new ArrayList<>();
        switch (context) {
            case "INTERNAL_PATIENT_ID":
                if(shr.getPatientIdentification().getInternalpatientids() !=null) {
                    for (int i = 0; i < shr.getPatientIdentification().getInternalpatientids().size(); i++) {
                        stringArr.add(serializer.serialize(shr.getPatientIdentification().getInternalpatientids().get(i)));
                    }
                }
                break;

            case "HIV_TEST":
                if(shr.getHivTests() != null) {
                    for (int i = 0; i < shr.getHivTests().size(); i++) {
                        stringArr.add(serializer.serialize(shr.getHivTests().get(i)));
                    }
                }
                break;
            case "IMMUNIZATION":
                if(shr.getImmunizations() != null) {
                    for (int i = 0; i < shr.getImmunizations().size(); i++) {
                        stringArr.add(serializer.serialize(shr.getImmunizations().get(i)));
                    }
                }
                break;
            case "MOTHER_IDENTIFIER":
                if(shr.getPatientIdentification().getMotherDetail().getMotherIdentifiers() !=null) {
                    for (int i = 0; i < shr.getPatientIdentification().getMotherDetail().getMotherIdentifiers().size(); i++) {
                        stringArr.add(serializer.serialize(shr.getPatientIdentification().getMotherDetail().getMotherIdentifiers().get(i)));
                    }
                }
                break;
            default:
                break;
        }
        return stringArr;
    }

    public ReadResponse MockRead() {

            // Mock Message
            String mockMessage = "{\n" +
                    "  \"VERSION\": \"1.0.0\",\n" +
                    "  \"PATIENT_IDENTIFICATION\": {\n" +
                    "    \"EXTERNAL_PATIENT_ID\": {\n" +
                    "      \"ID\": \"110ec58a-a0f2-4ac4-8393-c866d813b8d1\",\n" +
                    "      \"IDENTIFIER_TYPE\": \"GODS_NUMBER\",\n" +
                    "      \"ASSIGNING_AUTHORITY\": \"MPI\",\n" +
                    "      \"ASSIGNING_FACILITY\": \"10829\"\n" +
                    "    },\n" +
                    "    \"INTERNAL_PATIENT_ID\": [\n" +
                    "      {\n" +
                    "        \"ID\": \"12345678-ADFGHJY-0987654-NHYI890\",\n" +
                    "        \"IDENTIFIER_TYPE\": \"CARD_SERIAL_NUMBER\",\n" +
                    "        \"ASSIGNING_AUTHORITY\": \"CARD_REGISTRY\",\n" +
                    "        \"ASSIGNING_FACILITY\": \"10829\"\n" +
                    "      },\n" +
                    "      {\n" +
                    "        \"ID\": \"12345678\",\n" +
                    "        \"IDENTIFIER_TYPE\": \"HEI_NUMBER\",\n" +
                    "        \"ASSIGNING_AUTHORITY\": \"MCH\",\n" +
                    "        \"ASSIGNING_FACILITY\": \"10829\"\n" +
                    "      },\n" +
                    "      {\n" +
                    "        \"ID\": \"12345678\",\n" +
                    "        \"IDENTIFIER_TYPE\": \"CCC_NUMBER\",\n" +
                    "        \"ASSIGNING_AUTHORITY\": \"CCC\",\n" +
                    "        \"ASSIGNING_FACILITY\": \"10829\"\n" +
                    "      },\n" +
                    "      {\n" +
                    "        \"ID\": \"001\",\n" +
                    "        \"IDENTIFIER_TYPE\": \"HTS_NUMBER\",\n" +
                    "        \"ASSIGNING_AUTHORITY\": \"HTS\",\n" +
                    "        \"ASSIGNING_FACILITY\": \"10829\"\n" +
                    "      },\n" +
                    "      {\n" +
                    "        \"ID\": \"ABC567\",\n" +
                    "        \"IDENTIFIER_TYPE\": \"ANC_NUMBER\",\n" +
                    "        \"ASSIGNING_AUTHORITY\": \"ANC\",\n" +
                    "        \"ASSIGNING_FACILITY\": \"10829\"\n" +
                    "      }\n" +
                    "    ],\n" +
                    "    \"PATIENT_NAME\": {\n" +
                    "      \"FIRST_NAME\": \"THERESA\",\n" +
                    "      \"MIDDLE_NAME\": \"MAY\",\n" +
                    "      \"LAST_NAME\": \"WAIRIMU\"\n" +
                    "    },\n" +
                    "    \"DATE_OF_BIRTH\": \"20171111\",\n" +
                    "    \"DATE_OF_BIRTH_PRECISION\": \"ESTIMATED/EXACT\",\n" +
                    "    \"SEX\": \"F\",\n" +
                    "    \"DEATH_DATE\": \"\",\n" +
                    "    \"DEATH_INDICATOR\": \"N\",\n" +
                    "    \"PATIENT_ADDRESS\": {\n" +
                    "      \"PHYSICAL_ADDRESS\": {\n" +
                    "        \"VILLAGE\": \"KWAKIMANI\",\n" +
                    "        \"WARD\": \"KIMANINI\",\n" +
                    "        \"SUB_COUNTY\": \"KIAMBU EAST\",\n" +
                    "        \"COUNTY\": \"KIAMBU\",\n" +
                    "        \"NEAREST_LANDMARK\": \"KIAMBU EAST\"\n" +
                    "      },\n" +
                    "      \"POSTAL_ADDRESS\": \"789 KIAMBU\"\n" +
                    "    },\n" +
                    "    \"PHONE_NUMBER\": \"254720278654\",\n" +
                    "    \"MARITAL_STATUS\": \"\",\n" +
                    "    \"MOTHER_DETAILS\": {\n" +
                    "      \"MOTHER_NAME\": {\n" +
                    "        \"FIRST_NAME\": \"WAMUYU\",\n" +
                    "        \"MIDDLE_NAME\": \"MARY\",\n" +
                    "        \"LAST_NAME\": \"WAITHERA\"\n" +
                    "      },\n" +
                    "      \"MOTHER_IDENTIFIER\": [\n" +
                    "        {\n" +
                    "          \"ID\": \"1234567\",\n" +
                    "          \"IDENTIFIER_TYPE\": \"NATIONAL_ID\",\n" +
                    "          \"ASSIGNING_AUTHORITY\": \"GOK\",\n" +
                    "          \"ASSIGNING_FACILITY\": \"\"\n" +
                    "        },\n" +
                    "        {\n" +
                    "          \"ID\": \"12345678\",\n" +
                    "          \"IDENTIFIER_TYPE\": \"NHIF\",\n" +
                    "          \"ASSIGNING_AUTHORITY\": \"NHIF\",\n" +
                    "          \"ASSIGNING_FACILITY\": \"\"\n" +
                    "        },\n" +
                    "        {\n" +
                    "          \"ID\": \"12345-67890\",\n" +
                    "          \"IDENTIFIER_TYPE\": \"CCC_NUMBER\",\n" +
                    "          \"ASSIGNING_AUTHORITY\": \"CCC\",\n" +
                    "          \"ASSIGNING_FACILITY\": \"10829\"\n" +
                    "        },\n" +
                    "        {\n" +
                    "          \"ID\": \"12345678\",\n" +
                    "          \"IDENTIFIER_TYPE\": \"PMTCT_NUMBER\",\n" +
                    "          \"ASSIGNING_AUTHORITY\": \"PMTCT\",\n" +
                    "          \"ASSIGNING_FACILITY\": \"10829\"\n" +
                    "        }\n" +
                    "      ]\n" +
                    "    }\n" +
                    "  },\n" +
                    "  \"NEXT_OF_KIN\": [\n" +
                    "    {\n" +
                    "      \"NOK_NAME\": {\n" +
                    "        \"FIRST_NAME\": \"WAIGURU\",\n" +
                    "        \"MIDDLE_NAME\": \"KIMUTAI\",\n" +
                    "        \"LAST_NAME\": \"WANJOKI\"\n" +
                    "      },\n" +
                    "      \"RELATIONSHIP\": \"**AS DEFINED IN GREENCARD\",\n" +
                    "      \"ADDRESS\": \"4678 KIAMBU\",\n" +
                    "      \"PHONE_NUMBER\": \"25489767899\",\n" +
                    "      \"SEX\": \"F\",\n" +
                    "      \"DATE_OF_BIRTH\": \"19871022\",\n" +
                    "      \"CONTACT_ROLE\": \"T\"\n" +
                    "    }\n" +
                    "  ],\n" +
                    "  \"HIV_TEST\": [\n" +
                    "    {\n" +
                    "      \"DATE\": \"20180101\",\n" +
                    "      \"RESULT\": \"POSITIVE/NEGATIVE/INCONCLUSIVE\",\n" +
                    "      \"TYPE\": \"SCREENING/CONFIRMATORY\",\n" +
                    "      \"FACILITY\": \"10829\",\n" +
                    "      \"STRATEGY\": \"HP/NP/VI/VS/HB/MO/O\",\n" +
                    "      \"PROVIDER_DETAILS\": {\n" +
                    "        \"NAME\": \"MATTHEW NJOROGE, MD\",\n" +
                    "        \"ID\": \"12345-67890-abcde\"\n" +
                    "      }\n" +
                    "    }\n" +
                    "  ],\n" +
                    "  \"IMMUNIZATION\": [\n" +
                    "    {\n" +
                    "      \"NAME\": \"BCG/OPV_AT_BIRTH/OPV1/OPV2/OPV3/PCV10-1/PCV10-2/PCV10-3/PENTA1/PENTA2/PENTA3/MEASLES6/MEASLES9/MEASLES18/ROTA1/ROTA2\",\n" +
                    "      \"DATE_ADMINISTERED\": \"20180101\"\n" +
                    "    }\n" +
                    "  ],\n" +
                    "  \"CARD_DETAILS\": {\n" +
                    "    \"STATUS\": \"ACTIVE/INACTIVE\",\n" +
                    "    \"REASON\": \"LOST/DEATH/DAMAGED\",\n" +
                    "    \"LAST_UPDATED\": \"20180101\",\n" +
                    "    \"LAST_UPDATED_FACILITY\": \"10829\"\n" +
                    "  }\n" +
                    "}";
            //todo: replace mock message response with th read message
            Response response = new ReadResponse(mockMessage, null);
            return (ReadResponse) response;
    }

}
