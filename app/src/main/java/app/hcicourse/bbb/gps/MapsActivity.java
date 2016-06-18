package app.hcicourse.bbb.gps;

import android.os.Bundle;
import android.util.Log;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Marker;

import app.hcicourse.bbb.R;

/**
 * Created by Wolf on 2016/6/18.
 */
public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    Double latitude, longitude;
    private GoogleMap googleMap;
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
    }

    @Override
    public void onMapReady(GoogleMap map) {
        // Add a marker in Sydney, Australia, and move the camera.
        LatLng location = new LatLng(latitude, longitude);
        map.addMarker(new MarkerOptions().position(location).title("Current Location"));
        map.moveCamera(CameraUpdateFactory.newLatLng(location));
    }

}