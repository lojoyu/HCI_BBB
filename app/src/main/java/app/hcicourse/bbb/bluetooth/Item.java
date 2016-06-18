package app.hcicourse.bbb.bluetooth;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

public interface Item {
    public int getViewType();
    public View getView(LayoutInflater inflater, View convertView, Context context);
}
