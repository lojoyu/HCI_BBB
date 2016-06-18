package app.hcicourse.bbb;

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
