package app.hcicourse.bbb.device;

import android.os.Parcel;
import android.os.Parcelable;

public class Device implements Parcelable{
    private String name;
    private String addr;
    private String gps;
    private int id;

    public Device() {
        name = addr = gps = null;
        id = 0;
    }

    public Device(String name, String addr) {
        this.name = name;
        this.addr = addr;
        gps = null;
        id = 0;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setAddr(String addr){
        this.addr = addr;
    }

    public void setGps(String gps){
        this.gps = gps;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getName(){
        return name;
    }

    public String getAddr(){
        return addr;
    }

    public String getGps(){
        return gps;
    }

    public int getId(){
        return id;
    }

    static public String GPStoString(double lat, double lng) {
        return Double.toString(lat)+","+Double.toString(lng);
    }

    static public double[] GPStoDouble(String gpsStr) {
        if (gpsStr == null) return null;

        String[] arr = gpsStr.split(",");
        double[] gpsD = new double[2];
        gpsD[0] = Double.valueOf(arr[0]);
        gpsD[1] = Double.valueOf(arr[1]);
        return gpsD;
    }


    public Device(Parcel in) {
        readFromParcel(in);
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(addr);
        dest.writeString(gps);
        dest.writeInt(id);

    }

    private void readFromParcel(Parcel in) {
        name = in.readString();
        addr = in.readString();
        gps = in.readString();
        id = in.readInt();
    }

    public static final Parcelable.Creator<Device> CREATOR = new Parcelable.Creator<Device>() {
        public Device createFromParcel(Parcel in) {
            return new Device(in);
        }
        public Device[] newArray(int size) {
            return new Device[size];
        }
    };
}
