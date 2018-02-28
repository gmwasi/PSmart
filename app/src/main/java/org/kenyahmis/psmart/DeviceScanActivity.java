package org.kenyahmis.psmart;

import java.util.Set;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.acs.bluetooth.Acr1255uj1Reader;
import com.acs.bluetooth.Acr3901us1Reader;
import com.acs.bluetooth.BluetoothReader;
import com.acs.bluetooth.BluetoothReaderManager;

import org.kenyahmis.psmartlibrary.Models.Response;
import org.kenyahmis.psmartlibrary.PSmartCard;

import static org.kenyahmis.psmart.ReaderSettingsActivity.TAG;

/**
 * Activity for scanning and displaying available Bluetooth LE devices.
 */
public class DeviceScanActivity extends AppCompatActivity {

    private RecyclerView recyclerView, recycler_paired;

    private BluetoothAdapter mBTAdapter;
    private Set<BluetoothDevice> mPairedDevices;
    private DeviceAdapter adapter, paired_adapter;
    private TextView paired, scanned;

    private final static int REQUEST_ENABLE_BT = 1; // used to identify adding bluetooth names


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_scan);
        setResult(RESULT_CANCELED);
        recyclerView = findViewById(R.id.recycler_scanned_devices);
        recycler_paired = findViewById(R.id.recycler_paired_devices);

        paired = findViewById(R.id.tv_paired_devices);
        scanned = findViewById(R.id.tv_new_devices);

        recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        adapter = new DeviceAdapter(getBaseContext());
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getBaseContext(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                BluetoothDevice device = adapter.getDevice(position);
                Intent intent = new Intent();
                intent.putExtra("device",device);
                setResult(RESULT_OK,intent);
                finish();
            }
        }));
        mBTAdapter = BluetoothAdapter.getDefaultAdapter();

        // Ask for location permission if not already allowed
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);

        /*
         * Use this check to determine whether BLE is supported on the device.
         * Then you can selectively disable BLE-related features.
         */
        if (!getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT)
                    .show();
            finish();
        }
        bluetoothOn();

    }

    private void setupPaired() {
        paired.setVisibility(View.VISIBLE);
        recycler_paired = findViewById(R.id.recycler_paired_devices);

        recycler_paired.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        paired_adapter = new DeviceAdapter(getBaseContext());
        recycler_paired.setAdapter(paired_adapter);
        recycler_paired.addOnItemTouchListener(new RecyclerItemClickListener(getBaseContext(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                BluetoothDevice device = paired_adapter.getDevice(position);
                Intent intent = new Intent();
                intent.putExtra("device",device);
                setResult(RESULT_OK,intent);
                finish();
            }
        }));
    }

    private void bluetoothOn() {
        if (!mBTAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            Toast.makeText(getApplicationContext(), "Bluetooth turned on", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(getApplicationContext(), "Bluetooth is already on", Toast.LENGTH_SHORT).show();
            listPairedDevices();
            discover();
        }
    }

    // Enter here after user selects "yes" or "no" to enabling radio
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent Data) {
        // Check which request we're responding to
        if (requestCode == REQUEST_ENABLE_BT) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                // The user picked a contact.
                // The Intent's data Uri identifies which contact was selected.
                Toast.makeText(getApplicationContext(), "Bluetooth Enabled", Toast.LENGTH_SHORT).show();
                listPairedDevices();
                discover();
            } else {
                Toast.makeText(getApplicationContext(), "Bluetooth Disabled", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @Override
    public void finish() {
        if (blReceiver !=null)
            unregisterReceiver(blReceiver);
        super.finish();
    }

    private void listPairedDevices() {
        mPairedDevices = mBTAdapter.getBondedDevices();
        if (mPairedDevices.size() > 0) setupPaired();
        if (mBTAdapter.isEnabled()) {
            // put it's one to the adapter
            for (BluetoothDevice device : mPairedDevices) {
                paired_adapter.addDevice(device);
                Log.d("Paired", device.getName());
            }

            Toast.makeText(getApplicationContext(), "Show Paired Devices", Toast.LENGTH_SHORT).show();
        } else
            Toast.makeText(getApplicationContext(), "Bluetooth not on", Toast.LENGTH_SHORT).show();
    }

    private void discover() {
        // Check if the device is already discovering
        if (mBTAdapter.isDiscovering()) {
            mBTAdapter.cancelDiscovery();
            Toast.makeText(getApplicationContext(), "Discovery stopped", Toast.LENGTH_SHORT).show();
            discover();
        } else {
            if (mBTAdapter.isEnabled()) {
                adapter.clearAll();
                mBTAdapter.startDiscovery();
                Toast.makeText(getApplicationContext(), "Discovery started", Toast.LENGTH_SHORT).show();
                registerReceiver(blReceiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));
            } else {
                Toast.makeText(getApplicationContext(), "Bluetooth not on", Toast.LENGTH_SHORT).show();
            }
        }
    }

    final BroadcastReceiver blReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // add the name to the list
                adapter.addDevice(device);
                Log.d("Discovered", device.getName() + " MAC " + device.getAddress());
            }
        }
    };
}

