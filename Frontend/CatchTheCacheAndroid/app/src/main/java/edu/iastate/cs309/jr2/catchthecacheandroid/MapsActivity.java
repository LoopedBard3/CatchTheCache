package edu.iastate.cs309.jr2.catchthecacheandroid;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import edu.iastate.cs309.jr2.catchthecacheandroid.models.cache_models.Cache;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Cache cache;
    private Marker marker;
    private Circle circle;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        Bundle extras = getIntent().getExtras();
        cache = (Cache) extras.getSerializable("CacheObject");
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Log.d("MAPSLOG", "Checking Maps");
        if(servicesOK()) {
            Log.d("MAPSLOG", "Initializing Maps");
            // Add a marker on gerdin and move the camera
            final LatLng cacheLocation = new LatLng(cache.getLatitude(), cache.getLongitude());
            //mMap.addMarker(new MarkerOptions().position(cacheLocation).title("Marker for " + cache.getName()));
            //Log.d("MAPSLOG", "Maps Marker Added");
            float zoomLevel = 17.5f;
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(cacheLocation, zoomLevel));
            Log.d("MAPSLOG", "Maps Camera Moved");
            mMap.getUiSettings().setScrollGesturesEnabled(true);
            mMap.getUiSettings().setZoomGesturesEnabled(true);
            mMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
                @Override
                public void onCameraMove() {
                    if(mMap.getCameraPosition().zoom < 16f && (marker == null || !marker.isVisible())){
                        if(marker == null) marker = mMap.addMarker(new MarkerOptions().position(cacheLocation).title("Marker for " + cache.getName()));
                        marker.setVisible(true);
                        Log.d("MAPSLOG", "Maps Marker Visible");
                    }else if(mMap.getCameraPosition().zoom >= 17.5f && marker != null ){
                        marker.setVisible(false);
                        Log.d("MAPSLOG", "Maps Marker Hidden");
                    }
                }
            });
            circle = mMap.addCircle(new CircleOptions()
                    .center(cacheLocation)
                    .radius(31)             //0.000279 single lat/longitude difference is equivalent to 31 meters
                    .strokeColor(Color.RED)
                    .fillColor(Color.BLUE & 0x44ffffff)); //First 2 hex values set the Alpha Channel
        }
    }

    private boolean servicesOK() {
        int result = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this);
        if(result == ConnectionResult.SUCCESS){
            return true;
        }else if(GoogleApiAvailability.getInstance().isUserResolvableError(result)){
            Log.d("MAPSLOG", "Error but user is recoverable");
            Toast.makeText(this, "User is resolvable", Toast.LENGTH_LONG).show();
        }else{
            Log.d("MAPSLOG", "Error with unrecoverable user.");
            Toast.makeText(this, "Error with google play!", Toast.LENGTH_LONG).show();
        }
        return false;
    }
}
