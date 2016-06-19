package app.hcicourse.bbb.device;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

public class DeviceListAdapter extends ArrayAdapter<Item> {
    private static String TAG = "bbb.DeviceListAdapter";
    private Context context;
    private LayoutInflater vi;

    public enum RowType {
        LIST_ITEM, HEADER_ITEM
    }

    public DeviceListAdapter(Context context, ArrayList<Item> objects) {
        super(context, 0, objects);
        this.context = context;
        vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getViewTypeCount() {
        return RowType.values().length;
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position).getViewType();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getItem(position).getView(vi, convertView, context);
    }

}
