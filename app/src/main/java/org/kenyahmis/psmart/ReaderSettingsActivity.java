package org.kenyahmis.psmart;

import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toolbar;

import com.acs.bluetooth.Acr1255uj1Reader;
import com.acs.bluetooth.Acr3901us1Reader;
import com.acs.bluetooth.BluetoothReader;
import com.acs.bluetooth.BluetoothReaderGattCallback;
import com.acs.bluetooth.BluetoothReaderGattCallback.OnConnectionStateChangeListener;
import com.acs.bluetooth.BluetoothReaderManager;
import com.acs.bluetooth.BluetoothReaderManager.OnReaderDetectionListener;

import org.kenyahmis.psmartlibrary.Utils;

public class ReaderSettingsActivity extends AppCompatActivity implements
        TxPowerDialogFragment.TxPowerDialogListener{
    BluetoothCardReader bluetoothCardReader;
    public ReaderSettingsActivity() {
        bluetoothCardReader = new BluetoothCardReader();
    }

    public static final String TAG = ReaderSettingsActivity.class.getSimpleName();
    public static final String EXTRAS_DEVICE_NAME = "DEVICE_NAME";
    public static final String EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS";

    /* Default master key. */
    private static final String DEFAULT_3901_MASTER_KEY = "FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF";
    /* Get 8 bytes random number APDU. */
    private static final String DEFAULT_3901_APDU_COMMAND = "80 84 00 00 08";
    /* Get Serial Number command (0x02) escape command. */
    private static final String DEFAULT_3901_ESCAPE_COMMAND = "02";

    /* Default master key. */
    private static final String DEFAULT_1255_MASTER_KEY = "ACR1255U-J1 Auth";

    /* Read 16 bytes from the binary block 0x04 (MIFARE 1K or 4K). */
    private static final String DEFAULT_1255_APDU_COMMAND = "FF B0 00 04 01";
    /* Get firmware version escape command. */
    private static final String DEFAULT_1255_ESCAPE_COMMAND = "E0 00 00 18 00";

    private static final byte[] AUTO_POLLING_START = { (byte) 0xE0, 0x00, 0x00,
            0x40, 0x01 };
    private static final byte[] AUTO_POLLING_STOP = { (byte) 0xE0, 0x00, 0x00,
            0x40, 0x00 };

    /* UI control */
    private Button mClear;
    private Button mAuthentication;
    private Button mPowerOn;
    private Button mPowerOff;
    private Button mGetDeviceInfo;
    private Button mGetBatteryLevel;
    private Button mGetBatteryStatus;
    private Button mGetCardStatus;

    private TextView mTxtConnectionState;
    private TextView mTxtAuthentication;
    private TextView mTxtATR;
    private TextView mTxtSlotStatus;
    private TextView mTxtCardStatus;
    private TextView mTxtBatteryLevel;
    private TextView mTxtBatteryLevel2;
    private TextView mTxtBatteryStatus;
    private TextView mTxtBatteryStatus2;

    private TextView mTxtSystemId;
    private TextView mTxtModelNo;
    private TextView mTxtSerialNo;
    private TextView mTxtFirmwareRev;
    private TextView mTxtHardwareRev;
    private TextView mTxtManufacturerName;

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

    /*
     * Listen to Bluetooth bond status change event. And turns on reader's
     * notifications once the card reader is bonded.
     */
    private final BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            BluetoothAdapter bluetoothAdapter = null;
            BluetoothManager bluetoothManager = null;
            final String action = intent.getAction();

            if (!(mBluetoothReader instanceof Acr3901us1Reader)) {
                /* Only ACR3901U-S1 require bonding. */
                return;
            }

            if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
                Log.i(TAG, "ACTION_BOND_STATE_CHANGED");

                /* Get bond (pairing) state */
                if (mBluetoothReaderManager == null) {
                    Log.w(TAG, "Unable to initialize BluetoothReaderManager.");
                    return;
                }

                bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
                if (bluetoothManager == null) {
                    Log.w(TAG, "Unable to initialize BluetoothManager.");
                    return;
                }

                bluetoothAdapter = bluetoothManager.getAdapter();
                if (bluetoothAdapter == null) {
                    Log.w(TAG, "Unable to initialize BluetoothAdapter.");
                    return;
                }

                final BluetoothDevice device = bluetoothAdapter
                        .getRemoteDevice(mDeviceAddress);

                if (device == null) {
                    return;
                }

                final int bondState = device.getBondState();

                // TODO: remove log message
                Log.i(TAG, "BroadcastReceiver - getBondState. state = "
                        + getBondingStatusString(bondState));

                /* Enable notification */
                if (bondState == BluetoothDevice.BOND_BONDED) {
                    if (mBluetoothReader != null) {
                        mBluetoothReader.enableNotification(true);
                    }
                }

                /* Progress Dialog */
                if (bondState == BluetoothDevice.BOND_BONDING) {
                    mProgressDialog = ProgressDialog.show(context,
                            "ACR3901U-S1", "Bonding...");
                } else {
                    if (mProgressDialog != null) {
                        mProgressDialog.dismiss();
                        mProgressDialog = null;
                    }
                }

                /*
                 * Update bond status and show in the connection status field.
                 */
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mTxtConnectionState
                                .setText(getBondingStatusString(bondState));
                    }
                });
            }
        }

    };

    /* Clear the Card reader's response and notification fields. */
    private void clearAllUi() {
        /* Clear notification fields. */
        mTxtCardStatus.setText(R.string.noData);
        mTxtBatteryLevel.setText(R.string.noData);
        mTxtBatteryStatus.setText(R.string.noData);
        mTxtAuthentication.setText(R.string.noData);

        /* Clear card reader's response fields. */
        clearResponseUi();
    }

    /* Clear the Card reader's Response field. */
    private void clearResponseUi() {
        mTxtAuthentication.setText(R.string.noData);
        mTxtATR.setText(R.string.noData);
        mTxtBatteryLevel2.setText(R.string.noData);
        mTxtBatteryStatus2.setText(R.string.noData);
        mTxtSlotStatus.setText(R.string.noData);

        /* GATT Characteristic. */
        mTxtSystemId.setText(R.string.noData);
        mTxtModelNo.setText(R.string.noData);
        mTxtSerialNo.setText(R.string.noData);
        mTxtFirmwareRev.setText(R.string.noData);
        mTxtHardwareRev.setText(R.string.noData);
        mTxtManufacturerName.setText(R.string.noData);
    }

    private void findUiViews() {
        mAuthentication = (Button) findViewById(R.id.button_Authenticate);

        mPowerOn = (Button) findViewById(R.id.button_PowerOn);
        mPowerOff = (Button) findViewById(R.id.button_power_off_card);
        mClear = (Button) findViewById(R.id.button_Clear);
        mGetBatteryLevel = (Button) findViewById(R.id.button_GetBatteryLevel);
        mGetBatteryStatus = (Button) findViewById(R.id.button_GetBatteryStatus);
        mGetDeviceInfo = (Button) findViewById(R.id.button_GetInfo);
        mGetCardStatus = (Button) findViewById(R.id.button_GetCardStatus);

        mTxtConnectionState = (TextView) findViewById(R.id.textView_ReaderState);
        mTxtCardStatus = (TextView) findViewById(R.id.textView_IccState);
        mTxtAuthentication = (TextView) findViewById(R.id.textView_Authentication);
        mTxtATR = (TextView) findViewById(R.id.textView_ATR);
        mTxtSlotStatus = (TextView) findViewById(R.id.textView_SlotStatus);
        mTxtBatteryLevel = (TextView) findViewById(R.id.textView_BatteryLevel);
        mTxtBatteryLevel2 = (TextView) findViewById(R.id.textView_BatteryLevel2);
        mTxtBatteryStatus = (TextView) findViewById(R.id.textView_BatteryStatus);
        mTxtBatteryStatus2 = (TextView) findViewById(R.id.textView_BatteryStatus2);

        mTxtSystemId = (TextView) findViewById(R.id.textView_SystemId);
        mTxtModelNo = (TextView) findViewById(R.id.textView_ModelNumber);
        mTxtSerialNo = (TextView) findViewById(R.id.textView_SerialNumber);
        mTxtFirmwareRev = (TextView) findViewById(R.id.textView_FirmwareRevision);
        mTxtHardwareRev = (TextView) findViewById(R.id.textView_HardwareRevision);
        mTxtManufacturerName = (TextView) findViewById(R.id.textView_Manufacturer);
    }

    /*
     * Update listener
     */
    private void setListener(BluetoothReader reader) {
        /* Update status change listener */
        if (mBluetoothReader instanceof Acr3901us1Reader) {
            ((Acr3901us1Reader) mBluetoothReader)
                    .setOnBatteryStatusChangeListener(new Acr3901us1Reader.OnBatteryStatusChangeListener() {

                        @Override
                        public void onBatteryStatusChange(
                                BluetoothReader bluetoothReader,
                                final int batteryStatus) {

                            Log.i(TAG, "mBatteryStatusListener data: "
                                    + batteryStatus);

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mTxtBatteryStatus
                                            .setText(bluetoothCardReader.getBatteryStatusString(batteryStatus));
                                }
                            });
                        }

                    });
        } else if (mBluetoothReader instanceof Acr1255uj1Reader) {
            ((Acr1255uj1Reader) mBluetoothReader)
                    .setOnBatteryLevelChangeListener(new Acr1255uj1Reader.OnBatteryLevelChangeListener() {

                        @Override
                        public void onBatteryLevelChange(
                                BluetoothReader bluetoothReader,
                                final int batteryLevel) {

                            Log.i(TAG, "mBatteryLevelListener data: "
                                    + batteryLevel);

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mTxtBatteryLevel
                                            .setText(bluetoothCardReader.getBatteryLevelString(batteryLevel));
                                }
                            });
                        }

                    });
        }
        mBluetoothReader
                .setOnCardStatusChangeListener(new BluetoothReader.OnCardStatusChangeListener() {

                    @Override
                    public void onCardStatusChange(
                            BluetoothReader bluetoothReader, final int sta) {

                        Log.i(TAG, "mCardStatusListener sta: " + sta);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mTxtCardStatus
                                        .setText(bluetoothCardReader.getCardStatusString(sta));
                            }
                        });
                    }

                });

        /* Wait for authentication completed. */
        mBluetoothReader
                .setOnAuthenticationCompleteListener(new BluetoothReader.OnAuthenticationCompleteListener() {

                    @Override
                    public void onAuthenticationComplete(
                            BluetoothReader bluetoothReader, final int errorCode) {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (errorCode == BluetoothReader.ERROR_SUCCESS) {
                                    mTxtAuthentication
                                            .setText("Authentication Success!");
                                    mAuthentication.setEnabled(false);
                                } else {
                                    mTxtAuthentication
                                            .setText("Authentication Failed!");
                                }
                            }
                        });
                    }

                });

        /* Wait for receiving ATR string. */
        mBluetoothReader
                .setOnAtrAvailableListener(new BluetoothReader.OnAtrAvailableListener() {

                    @Override
                    public void onAtrAvailable(BluetoothReader bluetoothReader,
                                               final byte[] atr, final int errorCode) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (atr == null) {
                                    mTxtATR.setText(bluetoothCardReader.getErrorString(errorCode));
                                } else {
                                    mTxtATR.setText(Utils.toHexString(atr));
                                }
                            }
                        });
                    }

                });

        /* Wait for power off response. */
        mBluetoothReader
                .setOnCardPowerOffCompleteListener(new BluetoothReader.OnCardPowerOffCompleteListener() {

                    @Override
                    public void onCardPowerOffComplete(
                            BluetoothReader bluetoothReader, final int result) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mTxtATR.setText(bluetoothCardReader.getErrorString(result));
                            }
                        });
                    }

                });


        /* Wait for device info available. */
        mBluetoothReader
                .setOnDeviceInfoAvailableListener(new BluetoothReader.OnDeviceInfoAvailableListener() {

                    @Override
                    public void onDeviceInfoAvailable(
                            BluetoothReader bluetoothReader, final int infoId,
                            final Object o, final int status) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (status != BluetoothGatt.GATT_SUCCESS) {
                                    Toast.makeText(ReaderSettingsActivity.this,
                                            "Failed to read device info!",
                                            Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                switch (infoId) {
                                    case BluetoothReader.DEVICE_INFO_SYSTEM_ID: {
                                        mTxtSystemId.setText(Utils
                                                .toHexString((byte[]) o));
                                    }
                                    break;
                                    case BluetoothReader.DEVICE_INFO_MODEL_NUMBER_STRING:
                                        mTxtModelNo.setText((String) o);
                                        break;
                                    case BluetoothReader.DEVICE_INFO_SERIAL_NUMBER_STRING:
                                        mTxtSerialNo.setText((String) o);
                                        break;
                                    case BluetoothReader.DEVICE_INFO_FIRMWARE_REVISION_STRING:
                                        mTxtFirmwareRev.setText((String) o);
                                        break;
                                    case BluetoothReader.DEVICE_INFO_HARDWARE_REVISION_STRING:
                                        mTxtHardwareRev.setText((String) o);
                                        break;
                                    case BluetoothReader.DEVICE_INFO_MANUFACTURER_NAME_STRING:
                                        mTxtManufacturerName.setText((String) o);
                                        break;
                                    default:
                                        break;
                                }
                            }
                        });
                    }

                });

        /* Wait for battery level available. */
        if (mBluetoothReader instanceof Acr1255uj1Reader) {
            ((Acr1255uj1Reader) mBluetoothReader)
                    .setOnBatteryLevelAvailableListener(new Acr1255uj1Reader.OnBatteryLevelAvailableListener() {

                        @Override
                        public void onBatteryLevelAvailable(
                                BluetoothReader bluetoothReader,
                                final int batteryLevel, int status) {
                            Log.i(TAG, "mBatteryLevelListener data: "
                                    + batteryLevel);

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mTxtBatteryLevel2
                                            .setText(bluetoothCardReader.getBatteryLevelString(batteryLevel));
                                }
                            });

                        }

                    });
        }

        /* Handle on battery status available. */
        if (mBluetoothReader instanceof Acr3901us1Reader) {
            ((Acr3901us1Reader) mBluetoothReader)
                    .setOnBatteryStatusAvailableListener(new Acr3901us1Reader.OnBatteryStatusAvailableListener() {

                        @Override
                        public void onBatteryStatusAvailable(
                                BluetoothReader bluetoothReader,
                                final int batteryStatus, int status) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mTxtBatteryStatus2
                                            .setText(bluetoothCardReader.getBatteryStatusString(batteryStatus));
                                }
                            });
                        }

                    });
        }

        /* Handle on slot status available. */
        mBluetoothReader
                .setOnCardStatusAvailableListener(new BluetoothReader.OnCardStatusAvailableListener() {

                    @Override
                    public void onCardStatusAvailable(
                            BluetoothReader bluetoothReader,
                            final int cardStatus, final int errorCode) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (errorCode != BluetoothReader.ERROR_SUCCESS) {
                                    mTxtSlotStatus
                                            .setText(bluetoothCardReader.getErrorString(errorCode));
                                } else {
                                    mTxtSlotStatus
                                            .setText(bluetoothCardReader.getCardStatusString(cardStatus));
                                }
                            }
                        });
                    }

                });

        mBluetoothReader
                .setOnEnableNotificationCompleteListener(new BluetoothReader.OnEnableNotificationCompleteListener() {

                    @Override
                    public void onEnableNotificationComplete(
                            BluetoothReader bluetoothReader, final int result) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (result != BluetoothGatt.GATT_SUCCESS) {
                                    /* Fail */
                                    Toast.makeText(
                                            ReaderSettingsActivity.this,
                                            "The device is unable to set notification!",
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(ReaderSettingsActivity.this,
                                            "The device is ready to use!",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }

                });
    }

    /* Set Button onClick() events. */
    private void setOnClickListener() {
        /*
         * Update onClick listener.
         */

        /* Clear UI text. */
        mClear.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                clearResponseUi();
            }
        });

        /* Authentication function, authenticate the connected card reader. */
        mAuthentication.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (mBluetoothReader == null) {
                    mTxtAuthentication.setText(R.string.card_reader_not_ready);
                    return;
                }

            }
        });

        /* Power on the card. */
        mPowerOn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (mBluetoothReader == null) {
                    mTxtATR.setText(R.string.card_reader_not_ready);
                    return;
                }
                if (!mBluetoothReader.powerOnCard()) {
                    mTxtATR.setText(R.string.card_reader_not_ready);
                }
            }
        });

        /* Power off the card. */
        mPowerOff.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (mBluetoothReader == null) {
                    mTxtATR.setText(R.string.card_reader_not_ready);
                    return;
                }
                if (!mBluetoothReader.powerOffCard()) {
                    mTxtATR.setText(R.string.card_reader_not_ready);
                }
            }
        });

        /* Read the Battery status. */
        mGetBatteryLevel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (mBluetoothReader == null) {
                    mTxtBatteryLevel.setText(R.string.card_reader_not_ready);
                    return;
                }

                /* Only Acr1255uj1Reader support getBatteryLevel. */
                if (!(mBluetoothReader instanceof Acr1255uj1Reader)) {
                    return;
                }
                if (!((Acr1255uj1Reader) mBluetoothReader).getBatteryLevel()) {
                    mTxtBatteryLevel.setText(R.string.card_reader_not_ready);
                }
            }
        });

        /* Read the Battery status. */
        mGetBatteryStatus.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                /* Check for detected reader. */
                if (mBluetoothReader == null) {
                    mTxtBatteryStatus2.setText(R.string.card_reader_not_ready);
                    return;
                }

                /* Only Acr3901us1Reader support getBatteryStatus. */
                if (!(mBluetoothReader instanceof Acr3901us1Reader)) {
                    return;
                }
                if (!((Acr3901us1Reader) mBluetoothReader).getBatteryStatus()) {
                    mTxtBatteryStatus2.setText(R.string.card_reader_not_ready);
                }
            }
        });

        /* Get the card status. */
        mGetCardStatus.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (mBluetoothReader == null) {
                    mTxtSlotStatus.setText(R.string.card_reader_not_ready);
                    return;
                }
                if (!mBluetoothReader.getCardStatus()) {
                    mTxtSlotStatus.setText(R.string.card_reader_not_ready);
                }
            }
        });

        /* Read the GATT characteristics. */
        mGetDeviceInfo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (mBluetoothReader == null) {
                    return;
                }
                if (!(mBluetoothReader instanceof Acr3901us1Reader)
                        && !(mBluetoothReader instanceof Acr1255uj1Reader)) {
                    mTxtManufacturerName.setText("Reader not supported");
                    return;
                }
                if (mBluetoothReader
                        .getDeviceInfo(BluetoothReader.DEVICE_INFO_MANUFACTURER_NAME_STRING)) {
                    mBluetoothReader
                            .getDeviceInfo(BluetoothReader.DEVICE_INFO_SYSTEM_ID);
                    mBluetoothReader
                            .getDeviceInfo(BluetoothReader.DEVICE_INFO_MODEL_NUMBER_STRING);
                    mBluetoothReader
                            .getDeviceInfo(BluetoothReader.DEVICE_INFO_SERIAL_NUMBER_STRING);
                    mBluetoothReader
                            .getDeviceInfo(BluetoothReader.DEVICE_INFO_FIRMWARE_REVISION_STRING);
                    mBluetoothReader
                            .getDeviceInfo(BluetoothReader.DEVICE_INFO_HARDWARE_REVISION_STRING);
                } else {
                    mTxtManufacturerName
                            .setText(R.string.card_reader_not_ready);
                }
            }
        });

    }

    /* Start the process to enable the reader's notifications. */
    private void activateReader(BluetoothReader reader) {
        if (reader == null) {
            return;
        }

        if (reader instanceof Acr3901us1Reader) {
            /* Start pairing to the reader. */
            ((Acr3901us1Reader) mBluetoothReader).startBonding();
        } else if (mBluetoothReader instanceof Acr1255uj1Reader) {
            /* Enable notification. */
            mBluetoothReader.enableNotification(true);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader_settings);
        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final Intent intent = getIntent();
        mDeviceName = intent.getStringExtra(EXTRAS_DEVICE_NAME);
        mDeviceAddress = intent.getStringExtra(EXTRAS_DEVICE_ADDRESS);

        /* Update UI. */
        findUiViews();
        updateUi(null);

        /* Set the onClick() event handlers. */
        setOnClickListener();

        /* Initialize BluetoothReaderGattCallback. */
        mGattCallback = new BluetoothReaderGattCallback();

        /* Register BluetoothReaderGattCallback's listeners */
        mGattCallback
                .setOnConnectionStateChangeListener(new OnConnectionStateChangeListener() {

                    @Override
                    public void onConnectionStateChange(
                            final BluetoothGatt gatt, final int state,
                            final int newState) {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (state != BluetoothGatt.GATT_SUCCESS) {
                                    /*
                                     * Show the message on fail to
                                     * connect/disconnect.
                                     */
                                    mConnectState = BluetoothReader.STATE_DISCONNECTED;

                                    if (newState == BluetoothReader.STATE_CONNECTED) {
                                        mTxtConnectionState
                                                .setText(R.string.connect_fail);
                                    } else if (newState == BluetoothReader.STATE_DISCONNECTED) {
                                        mTxtConnectionState
                                                .setText(R.string.disconnect_fail);
                                    }
                                    clearAllUi();
                                    updateUi(null);
                                    invalidateOptionsMenu();
                                    return;
                                }

                                updateConnectionState(newState);

                                if (newState == BluetoothProfile.STATE_CONNECTED) {
                                    /* Detect the connected reader. */
                                    if (mBluetoothReaderManager != null) {
                                        mBluetoothReaderManager.detectReader(
                                                gatt, mGattCallback);
                                    }
                                } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                                    mBluetoothReader = null;
                                    /*
                                     * Release resources occupied by Bluetooth
                                     * GATT client.
                                     */
                                    if (mBluetoothGatt != null) {
                                        mBluetoothGatt.close();
                                        mBluetoothGatt = null;
                                    }
                                }
                            }
                        });
                    }
                });

        /* Initialize mBluetoothReaderManager. */
        mBluetoothReaderManager = new BluetoothReaderManager();

        /* Register BluetoothReaderManager's listeners */
        mBluetoothReaderManager
                .setOnReaderDetectionListener(new OnReaderDetectionListener() {

                    @Override
                    public void onReaderDetection(BluetoothReader reader) {
                        updateUi(reader);

                        if (reader instanceof Acr3901us1Reader) {
                            /* The connected reader is ACR3901U-S1 reader. */
                            Log.v(TAG, "On Acr3901us1Reader Detected.");
                        } else if (reader instanceof Acr1255uj1Reader) {
                            /* The connected reader is ACR1255U-J1 reader. */
                            Log.v(TAG, "On Acr1255uj1Reader Detected.");
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(ReaderSettingsActivity.this,
                                            "The device is not supported!",
                                            Toast.LENGTH_SHORT).show();

                                    /* Disconnect Bluetooth reader */
                                    Log.v(TAG, "Disconnect reader!!!");
                                    disconnectReader();
                                    updateConnectionState(BluetoothReader.STATE_DISCONNECTED);
                                }
                            });
                            return;
                        }

                        mBluetoothReader = reader;
                        setListener(reader);
                        activateReader(reader);
                    }
                });

        /* Connect the reader. */
        connectReader();

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onResume() {
        Log.i(TAG, "onResume()");
        super.onResume();

        final IntentFilter intentFilter = new IntentFilter();

        /* Start to monitor bond state change */
        intentFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        registerReceiver(mBroadcastReceiver, intentFilter);

        /* Clear unused dialog. */
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }

    @Override
    protected void onPause() {
        Log.i(TAG, "onPause()");
        super.onPause();

        /* Stop to monitor bond state change */
        unregisterReceiver(mBroadcastReceiver);

        /* Disconnect Bluetooth reader */
        disconnectReader();
    }

    @Override
    protected void onStop() {
        Log.i(TAG, "onStop()");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.i(TAG, "onDestroy()");
        super.onDestroy();
    }

    /* Get the Bonding status string. */
    private String getBondingStatusString(int bondingStatus) {
        if (bondingStatus == BluetoothDevice.BOND_BONDED) {
            return "BOND BONDED";
        } else if (bondingStatus == BluetoothDevice.BOND_NONE) {
            return "BOND NONE";
        } else if (bondingStatus == BluetoothDevice.BOND_BONDING) {
            return "BOND BONDING";
        }
        return "BOND UNKNOWN.";
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /* Inflate the menu; this adds items to the action bar if it is present. */
        getMenuInflater().inflate(R.menu.reader, menu);
        if (mConnectState == BluetoothReader.STATE_CONNECTED) {
            menu.findItem(R.id.menu_connect).setVisible(false);
            menu.findItem(R.id.menu_connecting).setVisible(false);
            menu.findItem(R.id.menu_disconnect).setVisible(true);
        } else if (mConnectState == BluetoothReader.STATE_CONNECTING) {
            menu.findItem(R.id.menu_connect).setVisible(false);
            menu.findItem(R.id.menu_connecting).setVisible(true);
            menu.findItem(R.id.menu_disconnect).setVisible(false);
            menu.findItem(R.id.menu_refresh).setActionView(
                    R.layout.actionbar_indeterminate_progress);
        } else {
            menu.findItem(R.id.menu_connect).setVisible(true);
            menu.findItem(R.id.menu_connecting).setVisible(false);
            menu.findItem(R.id.menu_disconnect).setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.menu_connect){
            /* Connect Bluetooth reader */
            Log.v(TAG, "Start to connect!!!");
            connectReader();
            return true;
        }else if(item.getItemId() == R.id.menu_connect){

        }else if(item.getItemId() == R.id.menu_disconnect){
        /* Disconnect Bluetooth reader */
            Log.v(TAG, "Start to disconnect!!!");
            disconnectReader();
            return true;
        }else if(item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /* Show and hide UI resources and set the default master key and commands. */
    private void updateUi(final BluetoothReader bluetoothReader) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (bluetoothReader instanceof Acr3901us1Reader) {
                    /* The connected reader is ACR3901U-S1 reader. */

                    mClear.setEnabled(true);
                    mAuthentication.setEnabled(true);
                    mPowerOn.setEnabled(true);
                    mPowerOff.setEnabled(true);
                    mGetDeviceInfo.setEnabled(true);
                    mGetBatteryLevel.setEnabled(false);
                    mGetBatteryStatus.setEnabled(true);
                    mGetCardStatus.setEnabled(true);
                } else if (bluetoothReader instanceof Acr1255uj1Reader) {
                    /* The connected reader is ACR1255U-J1 reader. */
                    mClear.setEnabled(true);
                    mAuthentication.setEnabled(true);
                    mPowerOn.setEnabled(true);
                    mPowerOff.setEnabled(true);
                    mGetDeviceInfo.setEnabled(true);
                    mGetBatteryLevel.setEnabled(true);
                    mGetBatteryStatus.setEnabled(false);
                    mGetCardStatus.setEnabled(true);
                } else {
                    mClear.setEnabled(true);
                    mAuthentication.setEnabled(false);
                    mPowerOn.setEnabled(false);
                    mPowerOff.setEnabled(false);
                    mGetDeviceInfo.setEnabled(false);
                    mGetBatteryLevel.setEnabled(false);
                    mGetBatteryStatus.setEnabled(false);
                    mGetCardStatus.setEnabled(false);
                }
            }
        });
    }



    /* Update the display of Connection status string. */
    private void updateConnectionState(final int connectState) {

        mConnectState = connectState;

        if (connectState == BluetoothReader.STATE_CONNECTING) {
            mTxtConnectionState.setText(R.string.connecting);
        } else if (connectState == BluetoothReader.STATE_CONNECTED) {
            mTxtConnectionState.setText(R.string.connected);
        } else if (connectState == BluetoothReader.STATE_DISCONNECTING) {
            mTxtConnectionState.setText(R.string.disconnecting);
        } else {
            mTxtConnectionState.setText(R.string.disconnected);
            clearAllUi();
            updateUi(null);
        }
        invalidateOptionsMenu();
    }

    /*
     * Create a GATT connection with the reader. And detect the connected reader
     * once service list is available.
     */
    private boolean connectReader() {
        BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        if (bluetoothManager == null) {
            Log.w(TAG, "Unable to initialize BluetoothManager.");
            updateConnectionState(BluetoothReader.STATE_DISCONNECTED);
            return false;
        }

        BluetoothAdapter bluetoothAdapter = bluetoothManager.getAdapter();
        if (bluetoothAdapter == null) {
            Log.w(TAG, "Unable to obtain a BluetoothAdapter.");
            updateConnectionState(BluetoothReader.STATE_DISCONNECTED);
            return false;
        }

        /*
         * Connect Device.
         */
        /* Clear old GATT connection. */
        if (mBluetoothGatt != null) {
            Log.i(TAG, "Clear old GATT connection");
            mBluetoothGatt.disconnect();
            mBluetoothGatt.close();
            mBluetoothGatt = null;
        }

        /* Create a new connection. */
        final BluetoothDevice device = bluetoothAdapter
                .getRemoteDevice(mDeviceAddress);

        if (device == null) {
            Log.w(TAG, "Device not found. Unable to connect.");
            return false;
        }

        /* Connect to GATT server. */
        updateConnectionState(BluetoothReader.STATE_CONNECTING);
        mBluetoothGatt = device.connectGatt(this, false, mGattCallback);
        return true;
    }

    /* Disconnects an established connection. */
    private void disconnectReader() {
        if (mBluetoothGatt == null) {
            updateConnectionState(BluetoothReader.STATE_DISCONNECTED);
            return;
        }
        updateConnectionState(BluetoothReader.STATE_DISCONNECTING);
        mBluetoothGatt.disconnect();
    }

    @Override
    public void onDialogItemClick(DialogFragment dialog, int which) {

        byte[] command = { (byte) 0xE0, 0x00, 0x00, 0x49, (byte) which };

        if (mBluetoothReader == null) {

            mTxtATR.setText(R.string.card_reader_not_ready);
            return;
        }

        if (!mBluetoothReader.transmitEscapeCommand(command)) {
            mTxtATR.setText(R.string.card_reader_not_ready);
        }
    }
}
