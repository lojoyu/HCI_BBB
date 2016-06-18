package app.hcicourse.bbb.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DbSqlHelper extends SQLiteOpenHelper{

    public static final String DATABASE_NAME = "bbbDB";
    private static final int VERSION = 1;
    private static DbSqlHelper mInstance;


    public DbSqlHelper(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DbSqlHelper(Context context, String name) {
        this(context, name, null, VERSION);
    }

    public DbSqlHelper(Context context, String name, int version) {
        this(context, name, null, version);
    }

    public DbSqlHelper(Context context){
        super(context, DATABASE_NAME, null, VERSION);
    }

    public synchronized static DbSqlHelper getInstance(Context context) {
        if (mInstance == null) mInstance = new DbSqlHelper(context.getApplicationContext());
        return mInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createDeviceTable =
                "CREATE TABLE IF NOT EXISTS device("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                    + "name VARCHAR,"
                    + "addr VARCHAR UNIQUE,"
                    + "gps VARCHAR)";

        db.execSQL(createDeviceTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        if (newVersion >= oldVersion) {
            db.beginTransaction();
            boolean success = false;

            switch (oldVersion) {
                case 0:
                    oldVersion++;
                case 1:
                    String createDeviceTable =
                            "CREATE TABLE IF NOT EXISTS device("
                                    + "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                                    + "name VARCHAR,"
                                    + "addr VARCHAR UNIQUE,"
                                    + "gps VARCHAR)";

                    db.execSQL(createDeviceTable);
            }
            if (success) {
                db.setTransactionSuccessful();
            }
            db.endTransaction();
        }
        else {
            db.execSQL("DROP TABLE IF EXISTS device");
            onCreate(db);
        }
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }

    @Override
    public synchronized void close() {
        super.close();
    }


}

