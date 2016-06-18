package app.hcicourse.bbb;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothSPP.AutoConnectionListener;
import app.akexorcist.bluetotohspp.library.BluetoothSPP.BluetoothConnectionListener;
import app.akexorcist.bluetotohspp.library.BluetoothState;
import app.akexorcist.bluetotohspp.library.DeviceList;
import app.hcicourse.bbb.bluetooth.DeviceItem;
import app.hcicourse.bbb.bluetooth.DeviceListAdapter;
import app.hcicourse.bbb.bluetooth.Item;
import app.hcicourse.bbb.db.DbDeviceQuery;


public class MainActivity extends Activity {

    static final String TAG = "MainActivity";
    private static final ScheduledExecutorService worker =
            Executors.newSingleThreadScheduledExecutor();
    private ArrayList<Item> adaptList = new ArrayList<Item>();
    private DeviceListAdapter adapter = null;
    private int connectState = BluetoothState.STATE_NONE;
    private String autoConnectName=null;


    DbDeviceQuery dbDeviceQuery;
    BluetoothSPP bt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbDeviceQuery = new DbDeviceQuery(this);
        btInit();

        Button addDvBtn = (Button) findViewById(R.id.btn_add_device);
        addDvBtn.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {
                if(bt.getServiceState() == BluetoothState.STATE_CONNECTED) {
                    bt.disconnect();
                }
                Intent intent = new Intent(getApplicationContext(), DeviceList.class);
                intent.putExtra("bluetooth_devices", "Paired devices");
                intent.putExtra("no_devices_found", "No device");
                intent.putExtra("select_device", "Select");
                intent.putExtra("layout_list", R.layout.listview_btpair);
                startActivityForResult(intent, BluetoothState.REQUEST_CONNECT_DEVICE);
            }

        });

        createList();
    }

    public void onStart() {
        super.onStart();
        if (!bt.isBluetoothEnabled()) {
            bt.enable();
        } else {
            if(!bt.isServiceAvailable()) {
                Log.d(TAG, "setupService");
                bt.setupService();
                bt.startService(BluetoothState.DEVICE_OTHER);
            }
        }
        setup();

    }

    public void onDestroy() {
        super.onDestroy();
        bt.stopService();
    }

    void setup() {
        List<String> nameL = dbDeviceQuery.getStringValue("name", "ORDER BY ID LIMIT 1");
        Log.d(TAG, "nameLSize:"+Integer.toString(nameL.size()));
        if (nameL.size() > 0) {
            autoConnectName = nameL.get(0);
            bt.autoConnect(autoConnectName);
        }
    }

    void createList() {
        List<Device> dvList = dbDeviceQuery.getDevices("");
        if (dvList.size() <= 0) Log.d(TAG, "createList conList size <= 0");

        ListView listView = (ListView) findViewById(R.id.lv_device);

        listView.setLongClickable(true);
        listView.setClickable(true);

        adaptList.clear();
        for(int i=0;i<dvList.size();i++){
            Device dv = dvList.get(i);

            HashMap<String, Object> map = new HashMap<String, Object>();

            map.put("device", dv);
            if (autoConnectName.equals(dv.getName())) map.put("connectState", connectState);
            else map.put("connectState", BluetoothState.STATE_NONE);
            adaptList.add(new DeviceItem(map));
        }
        if (adaptList.size() <= 0){
            TextView tv = (TextView) findViewById(R.id.txt_no_device);
            listView.setVisibility(View.GONE);
            tv.setVisibility(View.VISIBLE);
        }

        adapter = new DeviceListAdapter(this, adaptList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(itemOnclickListener);
        listView.setOnItemLongClickListener(new Onlongclick());

    }

    private class Onlongclick implements OnItemLongClickListener {

        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view,
                                       int position, long id) {
            Log.d(TAG, "onLongClick");
            //TODO: longclick?
//            if (isLongClickOn) return true;
//            isLongClickOn = true;
//            total = 0;
//            RelativeLayout hiddenLayout = (RelativeLayout) mainAct.findViewById(R.id.hiddenLayout);
//            RelativeLayout bottomMenu = (RelativeLayout) mainAct.findViewById(R.id.bottom_menu_contact);
//            hiddenLayout.setVisibility(View.VISIBLE);
//            bottomMenu.setVisibility(View.GONE);
//            for(int i=0;i<adaptList.size();i++)
//            {
//                if (adaptList.get(i).getViewType() == ContactListAdapter.RowType.LIST_ITEM.ordinal()){
//                    ContactItem contactItm= (ContactItem) adaptList.get(i);
//                    contactItm.setVisible(true);
//                    if (i==position) {
//                        contactItm.setCheck(true);
//                        total++;
//                        TextView totalTxt = (TextView)mainAct.findViewById(R.id.showTotal);
//                        totalTxt.setText("Total choose: "+total);
//                    }
//                }
//            }
//            notifyDataSetChanged();
            return true;
        }
    }

    private AdapterView.OnItemClickListener itemOnclickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Item itm = adaptList.get(position);
            if (itm.getViewType() == DeviceListAdapter.RowType.LIST_ITEM.ordinal()){
                Log.d(TAG, "onItemClick");
                Device device = ((DeviceItem) itm).getDevice();
                //TODO: click device to show gps
            }
        }
    };

    void btInit() {
        bt = new BluetoothSPP(this);

        if(!bt.isBluetoothAvailable()) {
            Toast.makeText(getApplicationContext()
                    , "Bluetooth is not available"
                    , Toast.LENGTH_SHORT).show();
            finish();
        }

        bt.setOnDataReceivedListener(new BluetoothSPP.OnDataReceivedListener() {
            public void onDataReceived(byte[] data, String message) {
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });

        bt.setBluetoothStateListener(new BluetoothSPP.BluetoothStateListener() {
            @Override
            public void onServiceStateChanged(int state) {
                Log.d(TAG, "State Change");
                connectState = state;
                createList();
            }
        });

        bt.setBluetoothConnectionListener(new BluetoothConnectionListener() {
            public void onDeviceConnected(String name, String address) {
                Toast.makeText(getApplicationContext()
                        , "Connected to " + name + "\n" + address
                        , Toast.LENGTH_SHORT).show();
                connectState = BluetoothState.STATE_CONNECTED;
                Device dv = new Device(name, address);
                dbDeviceQuery.insertDevice(dv);
                Log.d(TAG, "connect");

            }

            public void onDeviceDisconnected() {
                Toast.makeText(getApplicationContext()
                        , "Connection lost", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "connection lost");

            }

            public void onDeviceConnectionFailed() {
                Toast.makeText(getApplicationContext()
                        , "Unable to connect", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "unable to connect");
            }
        });

        bt.setAutoConnectionListener(new AutoConnectionListener() {
            public void onNewConnection(String name, String address) {
                Log.i("Check", "New Connection - " + name + " - " + address);
            }

            public void onAutoConnectionStarted() {
                Log.i("Check", "Auto menu_connection started");
            }
        });

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == BluetoothState.REQUEST_CONNECT_DEVICE) {
            if(resultCode == Activity.RESULT_OK) {
                bt.connect(data);
                Log.d(TAG, "CONNECT RESULT_OK");
            }
        } else if(requestCode == BluetoothState.REQUEST_ENABLE_BT) {
            if(resultCode == Activity.RESULT_OK) {
                Log.d(TAG, "RESULT_OK");
                bt.setupService();
                bt.startService(BluetoothState.DEVICE_OTHER);
                setup();
            } else {
                Toast.makeText(getApplicationContext()
                        , "Bluetooth was not enabled."
                        , Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

}

