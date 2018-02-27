package org.kenyahmis.psmart;

import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.util.Log;

import com.acs.bluetooth.BluetoothReader;
import com.acs.bluetooth.BluetoothReaderGattCallback;
import com.acs.bluetooth.BluetoothReaderManager;

/**
 * Created by GMwasi on 2/27/2018.
 */

public class BluetoothCardReader {

    public static final String TAG = BluetoothCardReader.class.getSimpleName();

    /* Reader to be connected. */
    private String mDeviceName;
    private String mDeviceAddress;
    private int mConnectState = BluetoothReader.STATE_DISCONNECTED;

    /* Detected reader. */
    private BluetoothReader mBluetoothReader;
    /* ACS Bluetooth reader library. */
    private BluetoothReaderManager mBluetoothReaderManager;
    private BluetoothReaderGattCallback mGattCallback;
    private ProgressDialog mProgressDialog;
    /* Bluetooth GATT client. */
    private BluetoothGatt mBluetoothGatt;

    /* Get the Error string. */
    public String getErrorString(int errorCode) {
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

    /* Get the Card status string. */
    public String getCardStatusString(int cardStatus) {
        if (cardStatus == BluetoothReader.CARD_STATUS_ABSENT) {
            return "Absent.";
        } else if (cardStatus == BluetoothReader.CARD_STATUS_PRESENT) {
            return "Present.";
        } else if (cardStatus == BluetoothReader.CARD_STATUS_POWERED) {
            return "Powered.";
        } else if (cardStatus == BluetoothReader.CARD_STATUS_POWER_SAVING_MODE) {
            return "Power saving mode.";
        }
        return "The card status is unknown.";
    }

    /* Get the Battery status string. */
    public static String getBatteryStatusString(int batteryStatus) {
        if (batteryStatus == BluetoothReader.BATTERY_STATUS_NONE) {
            return "No battery.";
        } else if (batteryStatus == BluetoothReader.BATTERY_STATUS_FULL) {
            return "The battery is full.";
        } else if (batteryStatus == BluetoothReader.BATTERY_STATUS_USB_PLUGGED) {
            return "The USB is plugged.";
        }
        return "The battery is low.";
    }

    /* Get the Battery level string. */
    public String getBatteryLevelString(int batteryLevel) {
        if (batteryLevel < 0 || batteryLevel > 100) {
            return "Unknown.";
        }
        return String.valueOf(batteryLevel) + "%";
    }



}
