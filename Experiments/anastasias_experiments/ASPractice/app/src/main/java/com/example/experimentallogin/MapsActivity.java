package com.example.experimentallogin;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker on gerdin and move the camera
        LatLng gerdin = new LatLng(42.025139, -93.644478);
        mMap.addMarker(new MarkerOptions().position(gerdin).title("Marker on Gerdin"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(gerdin));
        /**
         * not sure why I want to move the camera yet
         */
        //lets add another marker, testing map and checking lat longs, not moving camera
        //LatLng chloe = new LatLng(42.022215, -93.654911);
       // mMap.addMarker(new MarkerOptions().position(gerdin).title("Marker on Chloe"));

    }
}
