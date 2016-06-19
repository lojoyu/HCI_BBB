package app.hcicourse.bbb.device;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import app.akexorcist.bluetotohspp.library.BluetoothState;
import app.hcicourse.bbb.R;

public class DeviceItem implements Item{

    private static String TAG = "bbb.DeviceItem";
    private HashMap<String, Object> map;
    private Device device;
    private int connectState;

    ViewHolder holder = null;
    List<Integer> idList = new ArrayList<Integer>();

    public DeviceItem(HashMap<String, Object> map) {
        this.map = map;
        device = (Device) map.get("device");
        connectState = (int) map.get("connectState");
    }

    public DeviceItem(Device device) {
        this.device = device;
    }

    private class ViewHolder {
        TextView name;
        TextView connectState;
    }

    @Override
    public int getViewType() {
        return DeviceListAdapter.RowType.LIST_ITEM.ordinal();
    }


    @Override
    public View getView(LayoutInflater inflater, View convertView, final Context context) {

        convertView = (View) inflater.inflate(R.layout.listitem_device, null);
        holder = new ViewHolder();
        holder.name = (TextView) convertView.findViewById(R.id.txt_device_name);
        holder.connectState = (TextView) convertView.findViewById(R.id.txt_connect_state);

        holder.name.setText(device.getName());
        holder.connectState.setText(connectStateToString());
        Log.d(TAG, "getView: name:"+device.getName()+"/connect"+connectState);

        convertView.setTag(holder);

        return convertView;
    }

    public Device getDevice(){
        return device;
    }

    public void setConnectState(int connectState) {
        this.connectState = connectState;
        holder.connectState.setText(connectStateToString());
    }

    String connectStateToString() {
        switch (connectState) {
            case(BluetoothState.STATE_CONNECTED):
                return "connected";
            case(BluetoothState.STATE_CONNECTING):
                return "connecting...";
            default:
                return "disconnected";
        }
    }
}


