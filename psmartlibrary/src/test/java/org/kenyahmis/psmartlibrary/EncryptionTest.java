package org.kenyahmis.psmartlibrary;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by GMwasi on 2/9/2018.
 */

public class EncryptionTest {

    private Encryption x = new Encryption();
    private String message = "{\n" +
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
    @Test
    public void MessageCanBeEncrypted() throws Exception {

        String result = x.encrypt(EncrytionKeys.SHR_KEY, message);
        Assert.assertNotNull(result);
        Assert.assertNotEquals(message, result);
    }

    @Test
    public void MessageCanBeDecrypted() throws Exception {
        String result = x.encrypt(EncrytionKeys.SHR_KEY,message);
        String messageDecrypt = x.decrypt(EncrytionKeys.SHR_KEY, result);
        Assert.assertEquals(message, messageDecrypt);
    }
}