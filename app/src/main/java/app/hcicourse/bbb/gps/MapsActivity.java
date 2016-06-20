package app.hcicourse.bbb.gps;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.net.Uri;
import android.content.Intent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.support.v4.app.FragmentActivity;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.StreetViewPanorama;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.OnStreetViewPanoramaReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.StreetViewPanoramaFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Marker;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothState;
import app.akexorcist.bluetotohspp.library.DeviceList;
import app.hcicourse.bbb.MainActivity;
import app.hcicourse.bbb.R;

/**
 * Created by Wolf on 2016/6/18.
 */
public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private Double latitude, longitude;
    private GoogleMap googleMap;
    private WebView webView;
    private Button btnLight;
    private Button btnFlag;
    private Button btnSong;
    boolean Lightclick = false;
    boolean Flagclick = false;
    boolean Songclick = false;
    BluetoothSPP bt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        Intent i = getIntent();
        // Receiving the location Data
        latitude = i.getDoubleExtra("latitude", 0.0);
        longitude = i.getDoubleExtra("longitude", 0.0);
        Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
        bt = MainActivity.bt;

        final Button LightBtn = (Button) findViewById(R.id.callBtnLight);
        btnLight = LightBtn;
        LightBtn.setBackgroundColor(Color.WHITE);
        LightBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Lightclick = !Lightclick;
                if(Lightclick) {
                    LightBtn.setText("Light ON");
                    LightBtn.setBackgroundColor(Color.RED);
                    bt.send("LT", true);
                } else {
                    LightBtn.setText("Light OFF");
                    LightBtn.setBackgroundColor(Color.WHITE);
                    bt.send("LF", true);
                }
            }
        });

        final Button FlagBtn = (Button) findViewById(R.id.callBtnFlag);
        btnFlag = FlagBtn;
        FlagBtn.setBackgroundColor(Color.WHITE);
        FlagBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Flagclick = !Flagclick;
                if(Flagclick) {
                    FlagBtn.setText("Flag ON");
                    FlagBtn.setBackgroundColor(Color.RED);
                    bt.send("GT", true);
                } else {
                    FlagBtn.setText("Flag OFF");
                    FlagBtn.setBackgroundColor(Color.WHITE);
                    bt.send("GF", true);
                }
            }
        });

        final Button SongBtn = (Button) findViewById(R.id.callBtnSong);
        btnSong = SongBtn;
        SongBtn.setBackgroundColor(Color.WHITE);
        SongBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Songclick = !Songclick;
                if(Songclick) {
                    SongBtn.setText("Song ON");
                    SongBtn.setBackgroundColor(Color.RED);
                    bt.send("ST", true);
                } else {
                    SongBtn.setText("Song OFF");
                    SongBtn.setBackgroundColor(Color.WHITE);
                    bt.send("SF", true);
                }
            }
        });

        bt.setBluetoothStateListener(new BluetoothSPP.BluetoothStateListener() {
            @Override
            public void onServiceStateChanged(int state) {
                setBtnEnable(state);
            }
        });
        setBtnEnable(bt.getServiceState());
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

//        StreetViewPanoramaFragment streetViewPanoramaFragment = (StreetViewPanoramaFragment) getFragmentManager().findFragmentById(R.id.streetviewpanorama);
//        StreetViewPanorama panorama = streetViewPanoramaFragment.getStreetViewPanorama();
//        panorama.setPosition(new LatLng(latitude, longitude));
        mappingWidgets();
    }


    private void mappingWidgets() {

        webView = (WebView) findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);

        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setSupportZoom(false);

        // If you want to load it from assets (you can customize it if you want)
        Uri uri = Uri.parse("file:///android_asset/streetviewscript.html");
        // If you want to load it directly
        //Uri uri = Uri.parse("https://google-developers.appspot.com/maps/documentation/javascript/examples/full/streetview-simple");
        webView.loadUrl(uri.toString());
        webView.addJavascriptInterface(new JavaScriptInterface(this), "Android");
        webView.setWebViewClient(new WebViewClient(){
            public void onPageFinished(WebView view, String url){
                webView.loadUrl("javascript:initialize(" + latitude +"," + longitude + ")");
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap map) {
        // Add a marker in Sydney, Australia, and move the camera.
        LatLng location = new LatLng(latitude, longitude);
        map.addMarker(new MarkerOptions().position(location).title("Current Location"));
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 18), 200, null);
        //map.animateCamera(CameraUpdateFactory.zoomTo(17), 200, null);
        //map.moveCamera(CameraUpdateFactory.newLatLng(location));
    }

    public void onDestroy() {
        bt.send("F", true);
        super.onDestroy();

    }

    void setBtnEnable(int state) {
        if (state == BluetoothState.STATE_CONNECTED) {
            btnLight.setEnabled(true);
            btnFlag.setEnabled(true);
            btnSong.setEnabled(true);
        } else {
            Lightclick = false;
            btnLight.setText("Light OFF");
            btnLight.setEnabled(false);
            btnLight.setBackgroundColor(Color.WHITE);
            Flagclick = false;
            btnFlag.setText("Flag OFF");
            btnFlag.setEnabled(false);
            btnFlag.setBackgroundColor(Color.WHITE);
            Songclick = false;
            btnSong.setText("Song OFF");
            btnSong.setEnabled(false);
            btnSong.setBackgroundColor(Color.WHITE);
        }
    }
}