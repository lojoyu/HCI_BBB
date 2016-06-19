package app.hcicourse.bbb.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import app.hcicourse.bbb.device.Device;

public class DbDeviceQuery {
    private static final String TAG = "bbb.DbDeviceQuery";
    private String tableName = "device";
    private DbSqlHelper helper;

    public DbDeviceQuery(Context context){
        helper = DbSqlHelper.getInstance(context);
    }

    public long insertDevice(Device dv){
        SQLiteDatabase db = DatabaseManager.getInstance(helper).openDatabase();
        boolean flag = true;

        ContentValues cv = new ContentValues();
        cv.put("name", dv.getName());
        cv.put("addr", dv.getAddr());
        cv.put("gps", dv.getGps());

        long insertQ = db.insert(tableName, "", cv);

        if (insertQ == -1){
            Log.e(TAG, "insertDevice() error");
            Log.e(TAG, "name:"+dv.getName());
        }
        DatabaseManager.getInstance(helper).closeDatabase();

        return insertQ;
    }

    public List<Device> getDevices(String whereClaus){
        SQLiteDatabase db = DatabaseManager.getInstance(helper).openDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM device "+whereClaus, null);
        List<Device> dvList = new ArrayList<Device>();

        int rows_num = cursor.getCount();
        if(rows_num != 0) {
            cursor.moveToFirst();
            for(int i=0; i<rows_num; i++) {
                Device dv = new Device();
                dv.setId(cursor.getInt(cursor.getColumnIndex("id")));
                dv.setName(cursor.getString(cursor.getColumnIndex("name")));
                dv.setAddr(cursor.getString(cursor.getColumnIndex("addr")));
                dv.setGps(cursor.getString(cursor.getColumnIndex("gps")));

                dvList.add(dv);
                cursor.moveToNext();
            }
        }
        cursor.close();
        DatabaseManager.getInstance(helper).closeDatabase();
        return dvList;
    }

    public void setStringValue(String key, String value, String where){
        SQLiteDatabase db = DatabaseManager.getInstance(helper).openDatabase();

        ContentValues cv = new ContentValues();
        cv.put(key, value);

        db.update(tableName, cv, where, null);

        DatabaseManager.getInstance(helper).closeDatabase();
    }

    public void setStringValues(String[] key, String[] value, String where){
        SQLiteDatabase db = DatabaseManager.getInstance(helper).openDatabase();

        ContentValues cv = new ContentValues();
        for (int i=0; i<key.length; i++) {
            cv.put(key[i], value[i]);
        }
        db.update(tableName, cv, where, null);

        DatabaseManager.getInstance(helper).closeDatabase();
    }

    public List<String> getStringValue(String key, String whereClaus){
        SQLiteDatabase db = DatabaseManager.getInstance(helper).openDatabase();
        Cursor cursor = db.rawQuery("SELECT "+key+" FROM device "+whereClaus, null);
        List<String> strList = new ArrayList<String>();

        int rows_num = cursor.getCount();
        if(rows_num != 0) {
            cursor.moveToFirst();
            for(int i=0; i<rows_num; i++) {
                strList.add(cursor.getString(cursor.getColumnIndex(key)));
                cursor.moveToNext();
            }
        }
        cursor.close();
        DatabaseManager.getInstance(helper).closeDatabase();
        return strList;
    }

    public boolean deleteDevice(String where){
        SQLiteDatabase db = DatabaseManager.getInstance(helper).openDatabase();
        int result = db.delete(tableName, where, null);
        DatabaseManager.getInstance(helper).closeDatabase();

        return result > 0 ? true : false;

    }


}

/** Example

 DbDeviceQuery db;

 db = new DbDeviceQuery(this);
 //or
 db = new DbDeviceQuery((someActivity)getActivity());


 // INSERT
 Device newDv = new Device();
 newDv.setName("myName");
 newDv.setAddr("123:123:123:125");
 newDv.setGps("456.456:45678.457");
 long inId = db.insertDevice(newDv);

 // UPDATE
 String[] key = new String[2];
 String[] value = new String[2];
 key[0] = "name";
 key[1] = "addr";
 value[0] = "updateN";
 value[1] = "000:000:000:000";
 String where = "id=1";
 db.setStringValues(key, value, where);

 // UPDATE2
 db.setStringValues("name", "newName", "id=2");

 // GET STRING
 List<String> nameL = db.getStringValue("name", "WHERE addr='000:000:000:000'");


 // GET DEVICE
 List<Device> dvL = db.getDevices("");


 // DELETE
 db.deleteDevice("");
*/