package org.kenyahmis.psmart;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ekirapa on 2/28/18.
 */

public class DeviceAdapter extends RecyclerView.Adapter<DeviceAdapter.DeviceHolder> {
    private Context context;
    private List<BluetoothDevice> bluetoothDevices = new ArrayList<>();

    public DeviceAdapter(Context context) {
        this.context = context;
    }

    @Override
    public DeviceHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.device_item_layout, parent, false);
        return new DeviceHolder(v);
    }

    @Override
    public void onBindViewHolder(DeviceHolder holder, int position) {
        holder.device_address.setText(bluetoothDevices.get(position).getAddress());
        holder.device_name.setText((bluetoothDevices.get(position).getName() != null) ? bluetoothDevices.get(position).getName() : context.getString(R.string.unknown_device));
        holder.device_name.setTextColor((bluetoothDevices.get(position).getType() == BluetoothDevice.DEVICE_TYPE_LE) ? ContextCompat.getColor(context, R.color.green) : ContextCompat.getColor(context, R.color.black));
    }

    @Override
    public int getItemCount() {
        return bluetoothDevices.size();
    }

    public void addDevice(BluetoothDevice device) {
        if (device.getType() == BluetoothDevice.DEVICE_TYPE_LE) bluetoothDevices.add(0, device);
        else
            bluetoothDevices.add(device);
        notifyDataSetChanged();
    }

    public void clearAll() {
        bluetoothDevices.clear();
        notifyDataSetChanged();
    }

    class DeviceHolder extends RecyclerView.ViewHolder {
        TextView device_name, device_address;

        public DeviceHolder(View itemView) {
            super(itemView);
            device_name = itemView.findViewById(R.id.tv_device_name);
            device_address = itemView.findViewById(R.id.tv_device_address);
        }
    }
}
