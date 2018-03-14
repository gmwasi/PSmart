package org.kenyahmis.psmartlibrary;

import android.util.Log;

import com.acs.bluetooth.Acr1255uj1Reader;
import com.acs.bluetooth.Acr3901us1Reader;
import com.acs.bluetooth.BluetoothReader;

import org.kenyahmis.psmartlibrary.AcosCard.OptionRegister;
import org.kenyahmis.psmartlibrary.AcosCard.SecurityOptionRegister;
import org.kenyahmis.psmartlibrary.Models.AcosCardResponse;
import org.kenyahmis.psmartlibrary.Models.AcosCommand;
import org.kenyahmis.psmartlibrary.Models.ApduCommand;
import org.kenyahmis.psmartlibrary.userFiles.UserFile;

import java.util.ArrayList;

/**
 * Created by GMwasi on 2/10/2018.
 */

class AcrBluetooth implements CardReader {


    private boolean authenticated = false;
    private boolean apduAvailable = false;
    private String responseInHexString = null;
    private byte[] responseApdu = null;
    private byte[] byteResponse = null;
    private boolean successfulResponse = false;

    private String TAG = "BluetoothReader";
    private BluetoothReader bluetoothReader;

    public enum CODE_TYPE
    {
        AC1(0x01),
        AC2(0x02),
        AC3(0x03),
        AC4(0x04),
        AC5(0x05),
        PIN(0x06),
        IC(0x07);

        private final int _id;
        CODE_TYPE(int id){this._id = id;}
    }

    public enum INTERNAL_FILE
    {
        MCUID_FILE(0),
        MANUFACTURER_FILE(1),
        PERSONALIZATION_FILE(2),
        SECURITY_FILE(3),
        USER_FILE_MGMT_FILE(4),
        ACCOUNT_FILE(5),
        ACCOUNT_SECURITY_FILE(6),
        ATR_FILE(7);

        private final int _id;
        INTERNAL_FILE(int id){this._id = id;}
    }

    public enum CARD_INFO_TYPE
    {
        CARD_SERIAL,
        EEPROM,
        VERSION_NUMBER;
    }

    AcrBluetooth(BluetoothReader reader){
        if(reader != null) bluetoothReader = reader;
        else throw new NullPointerException();

        registerReaderListeners();
    }

    @Override
    public AcosCardResponse ReadCard() {

        authenticate();
        bluetoothReader.powerOnCard();
        if(!checkIfAuthenticated()){

        }
        selectFile();
        //readRecord();

        int max = 5;
        int counter = 0;
        while(!apduAvailable) {
            try{
                Thread.sleep(1000);
                counter+=1;
                if(counter == max) break;

            } catch (InterruptedException ex){}
        }

        if(!apduAvailable)
        {
            return new AcosCardResponse(null, null, new ArrayList<String>(){{add("Response not available");}});
        }

        else
        {
            if(successfulResponse){
                return new AcosCardResponse(byteResponse, responseInHexString, null);
            }
            return new AcosCardResponse(null, null, new ArrayList<String>(){{add(responseInHexString);}});
        }

    }

    @Override
    public String readUserFile(UserFile userFile) {
        return null;
    }

    @Override
    public String writeUserFile(String data, UserFile userFile) {

        byte[] fileId = new byte[2];
        int expLength = 0;
        String tmpStr = "";
        byte[] tmpArray = new byte[56];

        try
        {
            fileId = userFile.getFileDescriptor().getFileId();
            expLength = userFile.getFileDescriptor().getExpLength();

            // Select user file
            authenticate();
            bluetoothReader.powerOnCard();
            formatCard();

            selectFile(fileId);

            tmpStr = data;
            tmpArray = new byte[expLength];
            int indx = 0;
            while (indx < data.length())
            {
                tmpArray[indx] = tmpStr.getBytes()[indx];
                indx++;
            }
            while (indx < expLength)
            {
                tmpArray[indx] = (byte)0x00;
                indx++;
            }

            writeRecord((byte)0x00, (byte)0x00, tmpArray);

            // TODO wait
            int limit = 5;
            int counter = 0;
            while(!apduAvailable)
            {
                try{
                    if(counter == limit)
                        break;
                    Thread.sleep(1000);
                    counter+=1;
                }
                catch (Exception ex){ex.printStackTrace();}
            }

        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
        return null;
    }

    @Override
    public byte[] WriteCard(String data) {

        authenticate();
        bluetoothReader.powerOnCard();
        if(!checkIfAuthenticated()){

        }
        selectFile();
        //writeRecord();
        return null;
    }

    private void registerReaderListeners(){
        setOnBatteryStatusChangeListener();
        setOnCardStatusListener();
        setOnAuthenticationCompleteListener();
        setOnAtrAvailableListener();
        setOnPowerOffCompleteListener();
        setOnResponseApduAvailableListener();
        setOnEscapeResponseAvailableListener();
        setOnDeviceInfoAvailableListener();
        setOnBatteryLevelAvailableListener();
        setOnCardStatusAvailableListener();
        setOnEnableNotificationCompleteListener();

    }

    // Listeners
    private void setOnBatteryStatusChangeListener(){
        if (bluetoothReader instanceof Acr3901us1Reader) {
            ((Acr3901us1Reader) bluetoothReader)
                    .setOnBatteryStatusChangeListener(new Acr3901us1Reader.OnBatteryStatusChangeListener() {

                        @Override
                        public void onBatteryStatusChange(
                                BluetoothReader bluetoothReader,
                                final int batteryStatus) {

                            Log.i(TAG, "mBatteryStatusListener data: "
                                    + batteryStatus);
                        }

                    });
        } else if (bluetoothReader instanceof Acr1255uj1Reader) {
            ((Acr1255uj1Reader) bluetoothReader)
                    .setOnBatteryLevelChangeListener(new Acr1255uj1Reader.OnBatteryLevelChangeListener() {

                        @Override
                        public void onBatteryLevelChange(
                                BluetoothReader bluetoothReader,
                                final int batteryLevel) {

                            Log.i(TAG, "mBatteryLevelListener data: "
                                    + batteryLevel);
                        }

                    });
        }
    }

    private void setOnCardStatusListener(){
        bluetoothReader
                .setOnCardStatusChangeListener(new BluetoothReader.OnCardStatusChangeListener() {

                    @Override
                    public void onCardStatusChange(
                            BluetoothReader bluetoothReader, final int sta) {

                        Log.i(TAG, "mCardStatusListener sta: " + sta);
                    }
                });
    }

    private void setOnAuthenticationCompleteListener(){
        bluetoothReader
                .setOnAuthenticationCompleteListener(new BluetoothReader.OnAuthenticationCompleteListener() {

                    @Override
                    public void onAuthenticationComplete(
                            BluetoothReader bluetoothReader, final int errorCode) {
                        // TODO:
                        authenticated = true;
                    }
                });
    }

    private void setOnAtrAvailableListener(){
        bluetoothReader
                .setOnAtrAvailableListener(new BluetoothReader.OnAtrAvailableListener() {

                    @Override
                    public void onAtrAvailable(BluetoothReader bluetoothReader,
                                               final byte[] atr, final int errorCode) {

                        // TODO:
                    }

                });
    }

    private void setOnPowerOffCompleteListener(){
        bluetoothReader
                .setOnCardPowerOffCompleteListener(new BluetoothReader.OnCardPowerOffCompleteListener() {

                    @Override
                    public void onCardPowerOffComplete(
                            BluetoothReader bluetoothReader, final int result) {

                        // TODO:
                    }

                });
    }

    private void setOnResponseApduAvailableListener(){
        bluetoothReader
                .setOnResponseApduAvailableListener(new BluetoothReader.OnResponseApduAvailableListener() {

                    @Override
                    public void onResponseApduAvailable(
                            BluetoothReader bluetoothReader, final byte[] apdu,
                            final int errorCode) {
                        // TODO:
                        responseInHexString = getResponseString(apdu, errorCode);
                        responseApdu = apdu;
                        apduAvailable = true;
                    }

                });
    }

    private void setOnEscapeResponseAvailableListener(){
        bluetoothReader
                .setOnEscapeResponseAvailableListener(new BluetoothReader.OnEscapeResponseAvailableListener() {

                    @Override
                    public void onEscapeResponseAvailable(
                            BluetoothReader bluetoothReader,
                            final byte[] response, final int errorCode) {

                        // TODO:
                    }

                });
    }

    private void setOnDeviceInfoAvailableListener(){
        bluetoothReader
                .setOnDeviceInfoAvailableListener(new BluetoothReader.OnDeviceInfoAvailableListener() {

                    @Override
                    public void onDeviceInfoAvailable(
                            BluetoothReader bluetoothReader, final int infoId,
                            final Object o, final int status) {

                    }

                });
    }

    private void setOnBatteryLevelAvailableListener(){
        if (bluetoothReader instanceof Acr1255uj1Reader) {
            ((Acr1255uj1Reader) bluetoothReader)
                    .setOnBatteryLevelAvailableListener(new Acr1255uj1Reader.OnBatteryLevelAvailableListener() {

                        @Override
                        public void onBatteryLevelAvailable(
                                BluetoothReader bluetoothReader,
                                final int batteryLevel, int status) {
                            Log.i(TAG, "mBatteryLevelListener data: "
                                    + batteryLevel);


                        }

                    });
        }

        /* Handle on battery status available. */
        if (bluetoothReader instanceof Acr3901us1Reader) {
            ((Acr3901us1Reader) bluetoothReader)
                    .setOnBatteryStatusAvailableListener(new Acr3901us1Reader.OnBatteryStatusAvailableListener() {

                        @Override
                        public void onBatteryStatusAvailable(
                                BluetoothReader bluetoothReader,
                                final int batteryStatus, int status) {
                            // TODO:
                        }

                    });
        }
    }

    private void setOnCardStatusAvailableListener(){
        bluetoothReader
                .setOnCardStatusAvailableListener(new BluetoothReader.OnCardStatusAvailableListener() {

                    @Override
                    public void onCardStatusAvailable(
                            BluetoothReader bluetoothReader,
                            final int cardStatus, final int errorCode) {

                        // TODO:
                    }

                });
    }

    private void setOnEnableNotificationCompleteListener(){
        bluetoothReader
                .setOnEnableNotificationCompleteListener(new BluetoothReader.OnEnableNotificationCompleteListener() {

                    @Override
                    public void onEnableNotificationComplete(
                            BluetoothReader bluetoothReader, final int result) {

                        // TODO:
                    }

                });
    }

    // Authentication
    private boolean authenticate(){
        byte masterKey[] = Utils.getTextinHexBytes(AcosCommand.AUTHENTICATION_KEY);
        if(masterKey != null && masterKey.length > 0)
            return bluetoothReader.authenticate(masterKey);
        return false;
    }

    // start session
    private boolean startSession(){
        byte command[] = Utils.getTextinHexBytes(AcosCommand.START_SESSION);
        if(command != null && command.length > 0)
            return bluetoothReader.transmitApdu(command);
        return false;
    }

    private boolean createFile(){
        byte command[] = new byte[]
        {
            (byte)0x00,
            (byte)0x30D40,
            (byte)0x00,
            (byte)0x00,
            (byte)0xDD,
            (byte)0x55,
            (byte)0x80
        };
        if(command != null && command.length > 0)
            return bluetoothReader.transmitApdu(command);
        return false;
    }

    // select file
    private boolean selectFile(){
        byte command[] = Utils.getTextinHexBytes(AcosCommand.SELECT_FILE);
        if(command != null && command.length > 0)
            return bluetoothReader.transmitApdu(command);
        return false;
    }

    private boolean checkIfAuthenticated(){
        int limit = 5;
        int counter = 0;
        while(!authenticated)
        {
            try{
                if(counter == limit)
                    break;
                Thread.sleep(1000);
                counter+=1;
            }
            catch (Exception ex){ex.printStackTrace();}
        }
        return authenticated;
    }

    private boolean readRecord(byte recordNumber, byte offset, byte lengthToRead){
        ApduCommand apduCommand = new ApduCommand();
        apduCommand.setCommand(AcosCommand.CLA, AcosCommand.READ_INS, recordNumber, offset, lengthToRead );
        return bluetoothReader.transmitApdu(apduCommand.createCommand());
    }

    private boolean writeRecord(byte recordNumber, byte offset, byte[] dataToWrite){
        //byte command[] = Utils.getTextinHexBytes(AcosCommand.WRITE_BINARY);
        ApduCommand apduCommand = new ApduCommand();
        apduCommand.setCommand(AcosCommand.CLA, AcosCommand.WRITE_INS, recordNumber, offset, (byte)dataToWrite.length);
        apduCommand.setData(dataToWrite);
        apduAvailable = false;
        return bluetoothReader.transmitApdu(apduCommand.createCommand());
    }

    private String getResponseString(byte[] response, int errorCode) {
        if (errorCode == BluetoothReader.ERROR_SUCCESS) {
            if (response != null && response.length > 0) {
                successfulResponse = true;
                return Utils.toHexString(response);
            }
            return "";
        }
        return getErrorString(errorCode);
    }

    private String getErrorString(int errorCode) {
        if (errorCode == BluetoothReader.ERROR_SUCCESS) {
            return "";
        } else if (errorCode == BluetoothReader.ERROR_INVALID_CHECKSUM) {
            return "The checksum is invalid.";
        } else if (errorCode == BluetoothReader.ERROR_INVALID_DATA_LENGTH) {
            return "The data length is invalid.";
        } else if (errorCode == BluetoothReader.ERROR_INVALID_COMMAND) {
            return "The command is invalid.";
        } else if (errorCode == BluetoothReader.ERROR_UNKNOWN_COMMAND_ID) {
            return "The command ID is unknown.";
        } else if (errorCode == BluetoothReader.ERROR_CARD_OPERATION) {
            return "The card operation failed.";
        } else if (errorCode == BluetoothReader.ERROR_AUTHENTICATION_REQUIRED) {
            return "Authentication is required.";
        } else if (errorCode == BluetoothReader.ERROR_LOW_BATTERY) {
            return "The battery is low.";
        } else if (errorCode == BluetoothReader.ERROR_CHARACTERISTIC_NOT_FOUND) {
            return "Error characteristic is not found.";
        } else if (errorCode == BluetoothReader.ERROR_WRITE_DATA) {
            return "Write command to reader is failed.";
        } else if (errorCode == BluetoothReader.ERROR_TIMEOUT) {
            return "Timeout.";
        } else if (errorCode == BluetoothReader.ERROR_AUTHENTICATION_FAILED) {
            return "Authentication is failed.";
        } else if (errorCode == BluetoothReader.ERROR_UNDEFINED) {
            return "Undefined error.";
        } else if (errorCode == BluetoothReader.ERROR_INVALID_DATA) {
            return "Received data error.";
        } else if (errorCode == BluetoothReader.ERROR_COMMAND_FAILED) {
            return "The command failed.";
        }
        return "Unknown error.";
    }

    public String getErrorMessage(byte[] statusWord)
    {
        if (statusWord == null )
            return "Invalid Parameters.";

        else if (statusWord[0] == (byte)0x6B && statusWord[1] == (byte)0x20)
            return "Amount too large.";

        else if (statusWord[0] == (byte)0x62 && statusWord[1] == (byte)0x81)
            return "Account data may be corrupted.";

        else if (statusWord[0] == (byte)0x67 && statusWord[1] == (byte)0x00)
            return "Specified Length plus offset is larger than record length.";

        else if (statusWord[0] == (byte)0x69 && statusWord[1] == (byte)0x66)
            return "Command not available; option bit not set.";

        else if (statusWord[0] == (byte)0x69 && statusWord[1] == (byte)0x81)
            return "Command incompatible with file structure.";

        else if (statusWord[0] == (byte)0x69 && statusWord[1] == (byte)0x82)
            return "Security status not satisfied; PIN not submitted prior to issuing this command.";

        else if (statusWord[0] == (byte)0x69 && statusWord[1] == (byte)0x83)
            return "Specified code is locked; Terminal Authentication Key is locked, Authentication process cannot be executed.";

        else if (statusWord[0] == (byte)0x69 && statusWord[1] == (byte)0x85)
            return "No data available; the INQUIRE ACCOUNT command was not executed immediately prior to the GET RESPONSE command; Mutual authentication not successfully completed prior to the SUBMIT Code command; No file selected.";

        else if (statusWord[0] == (byte)0x69 && statusWord[1] == (byte)0xF0)
            return "Data in account is inconsistent � no access unless in Issuer Mode.";

        else if (statusWord[0] == (byte)0x6A && statusWord[1] == (byte)0x82)
            return "File does not exist; Account does not exist.";

        else if (statusWord[0] == (byte)0x6A && statusWord[1] == (byte)0x83)
            return "Record not found � file too short.";

        else if (statusWord[0] == (byte)0x6F && statusWord[1] == (byte)0x00)
            return "I/O error; data to be accessed resides in invalid address.";

        else if (statusWord[0] == (byte)0x6F && statusWord[1] == (byte)0x10)
            return "Account Transaction Counter at maximum � no more transaction possible.";
        else if (statusWord[0] == (byte)0x69 && statusWord[1] == (byte)0x88)
            return "MAC does not match the data.";
        else if (statusWord[0] == (byte)0x6E && statusWord[1] == (byte)0x00)
            return "Invalid CLA.";

        else if (statusWord[0] == (byte)0x63)
        {
            return String.format("Invalid Pin/key/code; %02X  retries left; MAC cryptographic checksum is wrong.", statusWord[1] & 0x0F);
        }

        else
        {
            return String.format("Unknown Status Word (%02x%02x)", statusWord[0], statusWord[1]);
        }
    }

    private void clearCard() throws Exception
    {
        ApduCommand apdu = new ApduCommand();
        apduAvailable = false;
        apdu.setCommand((byte)0x80, (byte)0x30, (byte)0x00, (byte)0x00, (byte)0x00);
        byte[] apduCommand = apdu.createCommand();
        bluetoothReader.transmitApdu(apduCommand);
        setApduResponse(apdu);
        if (apdu.getSw()[0] != (byte)0x90)
            throw new Exception (getErrorMessage(apdu.getSw()));
    }

    private String submitCode(CODE_TYPE codeType, String code) throws Exception{
        ApduCommand apdu;

        apdu = new ApduCommand();
        apdu.setCommand((byte)0x80, (byte)0x20, (byte)codeType._id,(byte)0x00, (byte)0x08);

        apdu.setData(code.getBytes("ASCII"));

        byte[] apduCommand = apdu.createCommand();
        apduAvailable = false;
        bluetoothReader.transmitApdu(apduCommand);
        setApduResponse(apdu);
        if (apdu.getSw()[0] == (byte)0x63)
        {
            int triesLeft = apdu.getSw()[1] - (byte)0xC0;

            if (triesLeft == 0)
                throw new Exception ("PIN/Code is locked");
            else if (triesLeft == 1)
                throw new Exception ("Invalid PIN/Code, you only have " + triesLeft + " try left");
            else
                throw new Exception ("Invalid PIN/Code, you only have " + triesLeft + " tries left");
        }
        else if (apdu.getSw()[0] == (byte)0x69 && apdu.getSw()[1] == (byte)0x83)
            throw new Exception ("PIN/Code is locked");
        else if (apdu.getSw()[0] == (byte)0x69 && apdu.getSw()[1] == (byte)0x85)
            throw new Exception ("Authentication incomplete");
        else if (apdu.getSw()[0] == (byte)0x90)
            return "Valid";
        else
            return "Unknown state";
    }

    private String submitCode(CODE_TYPE codeType, byte[] code) throws Exception{
        ApduCommand apdu;

        apdu = new ApduCommand();
        apdu.setCommand((byte)0x80, (byte)0x20, (byte)codeType._id,(byte)0x00, (byte)0x08);
        apdu.setData(code);

        byte[] apduCommand = apdu.createCommand();
        apduAvailable = false;

        bluetoothReader.transmitApdu(apduCommand);
        setApduResponse(apdu);
        if (apdu.getSw()[0] == (byte)0x63)
        {
            int triesLeft = apdu.getSw()[1] - (byte)0xC0;

            if (triesLeft == 0)
                throw new Exception ("PIN/Code is locked");
            else if (triesLeft == 1)
                throw new Exception ("Invalid PIN/Code, you only have " + triesLeft + " try left");
            else
                throw new Exception ("Invalid PIN/Code, you only have " + triesLeft + " tries left");
        }
        else if (apdu.getSw()[0] == (byte)0x69 && apdu.getSw()[1] == (byte)0x83)
            throw new Exception ("PIN/Code is locked");
        else if (apdu.getSw()[0] == (byte)0x69 && apdu.getSw()[1] == (byte)0x85)
            throw new Exception ("Authentication incomplete");
        else if (apdu.getSw()[0] == (byte)0x90)
            return "Valid";
        else
            return "Unknown state";
    }

    private void selectFile(INTERNAL_FILE internalFile) throws Exception{
        byte[] fileID;

        if (internalFile == INTERNAL_FILE.MCUID_FILE)
            fileID = new byte[] { (byte)0xFF, (byte)0x00 };
        else if (internalFile == INTERNAL_FILE.MANUFACTURER_FILE)
            fileID = new byte[] { (byte)0xFF, (byte)0x01 };
        else if (internalFile == INTERNAL_FILE.PERSONALIZATION_FILE)
            fileID = new byte[] { (byte)0xFF, (byte)0x02 };
        else if (internalFile == INTERNAL_FILE.SECURITY_FILE)
            fileID = new byte[] { (byte)0xFF, (byte)0x03 };
        else if (internalFile == INTERNAL_FILE.USER_FILE_MGMT_FILE)
            fileID = new byte[] { (byte)0xFF, (byte)0x04 };
        else if (internalFile == INTERNAL_FILE.ACCOUNT_FILE)
            fileID = new byte[] { (byte)0xFF, (byte)0x05 };
        else if (internalFile == INTERNAL_FILE.ACCOUNT_SECURITY_FILE)
            fileID = new byte[] { (byte)0xFF, (byte)0x06 };
        else if (internalFile == INTERNAL_FILE.ATR_FILE)
            fileID = new byte[] { (byte)0xFF, (byte)0x07 };
        else
            throw new Exception ("Invalid internal file");

        this.selectFile(fileID);
    }

    private void selectFile(byte[] fileID) throws Exception
    {
        ApduCommand apdu;

        apdu = new ApduCommand();
        if (fileID == null || fileID.length != 2)
            throw new Exception("File ID length should be 2 bytes");

        apdu.setCommand((byte)0x80, (byte)0xA4, (byte)0x00,(byte)0x00, (byte)0x02);
        apdu.setData(fileID);
        apduAvailable = false;
        byte[] apduCommand = apdu.createCommand();

        bluetoothReader.transmitApdu(apduCommand);
        setApduResponse(apdu);
        //todo: check response and act
    }


    public void configurePersonalizationFile(OptionRegister optionRegister,
                                             SecurityOptionRegister securityRegister, byte NumberOfFiles) throws Exception
    {
        try
        {
            byte[] data;

            this.selectFile(INTERNAL_FILE.PERSONALIZATION_FILE);

            data = new byte[] { optionRegister.getRawValue(), securityRegister.getRawValue(), NumberOfFiles, 0x00 };
            this.writeRecord((byte)0x00, (byte)0x00, data);
            ApduCommand command = new ApduCommand();
            setApduResponse(command);

        }
        catch (Exception ex)
        {
            throw new Exception(ex.getMessage());
        }
    }

    private void setApduResponse(ApduCommand apduCommand){
        int limit = 10;
        int counter = 0;
        while(!apduAvailable)
        {
            try{
                if(counter == limit)
                    break;
                Thread.sleep(500);
                counter+=1;
            }
            catch (Exception ex){ex.printStackTrace();}
        }
        if(apduAvailable){
            apduCommand.setSw(responseApdu);
        }
    }

    private void formatCard()
    {
        try {
            // submit code
            submitCode(CODE_TYPE.AC1, "ACOSTEST");

            // clearCard
            clearCard();

            // submit code
            submitCode(CODE_TYPE.AC1, "ACOSTEST");

            // select file FF 02
            selectFile(new byte[]{(byte) 0xFF, (byte) 0x02});

            //write card
            writeRecord((byte) 0x00, (byte) 0x00, new byte[]{(byte) 0x00, (byte) 0x00, (byte) 0x07, (byte) 0x00});

            // select card
            selectFile(new byte[]{(byte) 0xFF, (byte) 0x04});

            // submit code
            submitCode(CODE_TYPE.AC1, "ACOSTEST");

            // configure personalization
            //Set Option Registers and Security Option Registers
            //See Personalization File of ACOS3 Reference Manual for more information
            OptionRegister optionRegister = new OptionRegister();

            optionRegister.setRequireMutualAuthenticationOnInquireAccount(false);
            optionRegister.setRequireMutualAuthenticationOnAccountTransaction(false);
            optionRegister.setEnableRevokeDebitCommand(false);
            optionRegister.setEnableChangePinCommand(false);
            optionRegister.setEnableDebitMac(false);
            optionRegister.setRequirePinDuringDebit(false);
            optionRegister.setEnableAccount(false);


            SecurityOptionRegister securityOptionRegister = new SecurityOptionRegister();

            securityOptionRegister.setIssuerCode(false);
            securityOptionRegister.setPin(false);
            securityOptionRegister.setAccessCondition5(false);
            securityOptionRegister.setAccessCondition4(false);
            securityOptionRegister.setAccessCondition3(false);
            securityOptionRegister.setAccessCondition2(false);
            securityOptionRegister.setAccessCondition1(false);

            //Write record to Personalization File
            //Number of File = 3
            //Select Personalization File

            configurePersonalizationFile(optionRegister, securityOptionRegister, (byte)0x07);

            //
            submitCode(CODE_TYPE.IC, "ACOSTEST");
            selectFile(new byte[]{(byte) 0xFF, (byte) 0x04});

            // Write to FF 04
            //   Write to first record of FF 04 (AA 00)
            writeRecord((byte) 0x00, (byte) 0x00, new byte[]{(byte) 0xFF, (byte) 0x0A, (byte) 0x00, (byte) 0x00, (byte) 0xAA, (byte) 0x00, (byte) 0x00});


            // Write to second record of FF 04 (BB 22)
            writeRecord((byte) 0x01, (byte) 0x00, new byte[]{(byte) 0xFF, (byte) 0x20, (byte) 0x00, (byte) 0x00, (byte) 0xBB, (byte) 0x00, (byte) 0x00});


            // write to third record of FF 04 (CC 33)
            writeRecord((byte) 0x02, (byte) 0x00, new byte[]{(byte) 0xFF, (byte) 0x0A, (byte) 0x00, (byte) 0x00, (byte) 0xCC, (byte) 0x00, (byte) 0x00});


            // write to fourth record of FF 04 (DD 44)
            writeRecord((byte) 0x03, (byte) 0x00, new byte[]{(byte) 0xFF, (byte) 0x40, (byte) 0x00, (byte) 0x00, (byte) 0xDD, (byte) 0x00, (byte) 0x00});


            // write to fifth record of FF 04 (DD 44)
            writeRecord((byte) 0x04, (byte) 0x00, new byte[]{(byte) 0xFF, (byte) 0x40, (byte) 0x00, (byte) 0x00, (byte) 0xDD, (byte) 0x11, (byte) 0x00});


            // write to sixth record of FF 04 (DD 44)
            writeRecord((byte) 0x05, (byte) 0x00, new byte[]{(byte) 0xFF, (byte) 0x40, (byte) 0x00, (byte) 0x00, (byte) 0xDD, (byte) 0x22, (byte) 0x00});


            // write to seventh record of FF 04 (DD 44)
            writeRecord((byte) 0x06, (byte) 0x00, new byte[]{(byte) 0xFF, (byte) 0x40, (byte) 0x00, (byte) 0x00, (byte) 0xDD, (byte) 0x33, (byte) 0x00});

        }

        catch (Exception ex){

        }
    }
}
