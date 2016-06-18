package app.hcicourse.bbb.gps;

import android.os.Bundle;
import android.util.Log;
import android.net.Uri;
import android.content.Intent;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.support.v4.app.FragmentActivity;

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

import app.hcicourse.bbb.R;

/**
 * Created by Wolf on 2016/6/18.
 */
public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private Double latitude, longitude;
    private GoogleMap googleMap;
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        Intent i = getIntent();
        // Receiving the location Data
        latitude = i.getDoubleExtra("latitude", 0.0);
        longitude = i.getDoubleExtra("longitude", 0.0);

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
}