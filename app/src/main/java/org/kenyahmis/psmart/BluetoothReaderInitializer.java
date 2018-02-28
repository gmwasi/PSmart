package org.kenyahmis.psmart;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.acs.bluetooth.Acr1255uj1Reader;
import com.acs.bluetooth.Acr3901us1Reader;
import com.acs.bluetooth.BluetoothReader;
import com.acs.bluetooth.BluetoothReaderGattCallback;
import com.acs.bluetooth.BluetoothReaderManager;

import static org.kenyahmis.psmart.ReaderSettingsActivity.TAG;

/**
 * Created by Muhoro on 2/28/2018.
 */

public class BluetoothReaderInitializer {

    private BluetoothReaderGattCallback bluetoothReaderGattCallback;
    private BluetoothReaderManager bluetoothReaderManager;
    private BluetoothReader bluetoothReader;
    private BluetoothGatt bluetoothGatt;
    private String bluetoothAddress;
    private Context context;
    boolean connected = false;

    public BluetoothReaderInitializer(Context context, String bluetoothDeviceAddress){
        bluetoothAddress = bluetoothDeviceAddress;
        this.context = context;
        initialize();
    }

    public String initialize(){


        try {

            bluetoothReaderGattCallback = new BluetoothReaderGattCallback();
            bluetoothReaderGattCallback.setOnConnectionStateChangeListener(new BluetoothReaderGattCallback.OnConnectionStateChangeListener() {
                @Override
                public void onConnectionStateChange(BluetoothGatt bluetoothGatt, int i, int i1) {

                }
            });

            // BluetoothReaderManager
            bluetoothReaderManager = new BluetoothReaderManager();
            bluetoothReaderManager.setOnReaderDetectionListener(new BluetoothReaderManager.OnReaderDetectionListener() {
                @Override
                public void onReaderDetection(BluetoothReader reader) {
                    if (reader instanceof Acr3901us1Reader) {
                            /* The connected reader is ACR3901U-S1 reader. */
                        Log.v(TAG, "On Acr3901us1Reader Detected.");
                    } else if (reader instanceof Acr1255uj1Reader) {
                            /* The connected reader is ACR1255U-J1 reader. */
                        Log.v(TAG, "On Acr1255uj1Reader Detected.");
                    }

                    bluetoothReader = reader;
                    activateReader(reader);
                    connected = connectReader();
                }
            });

            return "sucess";
        }

        catch (Exception ex){
            return ex.getMessage();
        }


    }

    public BluetoothReader getReader(){
        return bluetoothReader;
    }

    private void setBluetoothReaderListener(BluetoothReader reader){
        setOnBatteryStatusChangeListener();
        setOnCardStatusListener();
        setOnAuthenticationCompleteListener();
        setOnAtrAvailableListener();
    }

    void setOnBatteryStatusChangeListener(){
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

    void setOnCardStatusListener(){
        bluetoothReader
                .setOnCardStatusChangeListener(new BluetoothReader.OnCardStatusChangeListener() {

                    @Override
                    public void onCardStatusChange(
                            BluetoothReader bluetoothReader, final int sta) {

                        Log.i(TAG, "mCardStatusListener sta: " + sta);
                    }
                });
    }

    void setOnAuthenticationCompleteListener(){
        bluetoothReader
                .setOnAuthenticationCompleteListener(new BluetoothReader.OnAuthenticationCompleteListener() {

                    @Override
                    public void onAuthenticationComplete(
                            BluetoothReader bluetoothReader, final int errorCode) {

                        // TODO:
                    }
                });
    }

    void setOnAtrAvailableListener(){
        bluetoothReader
                .setOnAtrAvailableListener(new BluetoothReader.OnAtrAvailableListener() {

                    @Override
                    public void onAtrAvailable(BluetoothReader bluetoothReader,
                                               final byte[] atr, final int errorCode) {

                        // TODO:
                    }

                });
    }

    void setOnPowerOffCompleteListener(){
        bluetoothReader
                .setOnCardPowerOffCompleteListener(new BluetoothReader.OnCardPowerOffCompleteListener() {

                    @Override
                    public void onCardPowerOffComplete(
                            BluetoothReader bluetoothReader, final int result) {

                        // TODO:
                    }

                });
    }

    void setOnResponseApduAvailableListener(){
        bluetoothReader
                .setOnResponseApduAvailableListener(new BluetoothReader.OnResponseApduAvailableListener() {

                    @Override
                    public void onResponseApduAvailable(
                            BluetoothReader bluetoothReader, final byte[] apdu,
                            final int errorCode) {

                        // TODO:
                    }

                });
    }

    void setOnEscapeResponseAvailableListener(){
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

    void setOnDeviceInfoAvailableListener(){
        bluetoothReader
                .setOnDeviceInfoAvailableListener(new BluetoothReader.OnDeviceInfoAvailableListener() {

                    @Override
                    public void onDeviceInfoAvailable(
                            BluetoothReader bluetoothReader, final int infoId,
                            final Object o, final int status) {

                    }

                });
    }

    void setOnBatteryLevelAvailableListener(){
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

    void  setOnCardStatusAvailableListener(){
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

    void setOnEnableNotificationCompleteListener(){
        bluetoothReader
                .setOnEnableNotificationCompleteListener(new BluetoothReader.OnEnableNotificationCompleteListener() {

                    @Override
                    public void onEnableNotificationComplete(
                            BluetoothReader bluetoothReader, final int result) {

                        // TODO:
                    }

                });
    }

    private void activateReader(BluetoothReader reader) {
        if (reader == null) {
            return;
        }

        if (reader instanceof Acr3901us1Reader) {
            /* Start pairing to the reader. */
            ((Acr3901us1Reader) bluetoothReader).startBonding();
        } else if (bluetoothReader instanceof Acr1255uj1Reader) {
            /* Enable notification. */
            bluetoothReader.enableNotification(true);
        }
    }

    private boolean connectReader() {
        BluetoothManager bluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        if (bluetoothManager == null) {
            Log.w(TAG, "Unable to initialize BluetoothManager.");
            return false;
        }

        BluetoothAdapter bluetoothAdapter = bluetoothManager.getAdapter();
        if (bluetoothAdapter == null) {
            Log.w(TAG, "Unable to obtain a BluetoothAdapter.");
            return false;
        }

        /*
         * Connect Device.
         */
        /* Clear old GATT connection. */
        if (bluetoothGatt != null) {
            Log.i(TAG, "Clear old GATT connection");
            bluetoothGatt.disconnect();
            bluetoothGatt.close();
            bluetoothGatt = null;
        }

        /* Create a new connection. */
        final BluetoothDevice device = bluetoothAdapter
                .getRemoteDevice(bluetoothAddress);

        if (device == null) {
            Log.w(TAG, "Device not found. Unable to connect.");
            return false;
        }

        /* Connect to GATT server. */
        bluetoothGatt = device.connectGatt(context, false, bluetoothReaderGattCallback);
        return true;
    }

    /* Disconnects an established connection. */
    private void disconnectReader() {
        if (bluetoothGatt == null) {
            return;
        }
        bluetoothGatt.disconnect();
    }


}
