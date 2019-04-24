package edu.iastate.cs309.jr2.catchthecacheandroid;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.net.URI;
import java.util.ArrayList;
import java.util.Random;

import edu.iastate.cs309.jr2.catchthecacheandroid.models.WebSocketClient;
import edu.iastate.cs309.jr2.catchthecacheandroid.models.cache_models.Cache;
import edu.iastate.cs309.jr2.catchthecacheandroid.models.user_models.User;

/**
 * Activity for actually going caching. This is made up of a fragment map activity
 * and implements OnMapReadyCallback
 */
public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Cache cache;
    private Marker markerLocation, markerPlayer;
    private ArrayList<Marker> otherPlayers;
    private Circle circle;
    private Handler handler;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback mLocationCallback;
    private Location goal;
    private User usr;
    private WebSocketClient ws;

    /**
     * The default method called when the activity is created.
     * Sets up the map fragment and connects to the users location services
     * so we can check if the user found the cache.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        Bundle extras = getIntent().getExtras();
        cache = (Cache) extras.getSerializable("CacheObject");
        usr = (User) extras.getSerializable("UserObject");
        goal = getNearLocation(new LatLng(cache.getLatitude(), cache.getLongitude()), 31.0);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Create the LocationRequest object
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(5 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();

        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());
        task.addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                startLocationUpdates();
            }
        });

        task.addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    // Location settings are not satisfied, but this can be fixed
                    // by showing the user a dialog.
                    try {
                        // Show the dialog by calling startResolutionForResult(),
                        // and check the result in onActivityResult().
                        ResolvableApiException resolvable = (ResolvableApiException) e;
                        resolvable.startResolutionForResult(MapsActivity.this,
                                1);
                    } catch (IntentSender.SendIntentException sendEx) {
                        // Ignore the error.
                    }
                }
            }
        });

        otherPlayers = new ArrayList<>();

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    handleNewLocation(location);
                }
            }
        };

        try {
            ws = new WebSocketClient(new URI(getString(R.string.access_socket) + "caches/l/" + cache.getId() + "/websocket")) {
                @Override
                public void onMessage(String message) {
                    Log.d("WEBSOCKET", "Cache Map Socket returned: " + message);
                    //Add in on Message Logic, mostly updating where other Users are.
                    String[] split = message.split(":");
                    if (!split[0].equals(usr.getUsername())) {
                        if (split[1].equals("found")) {
                            goal = getNearLocation(new LatLng(cache.getLatitude(), cache.getLongitude()), 31.0);
                            //Possibly have popup notifying them that their goal has changed
                        } else {
                            String[] splitLoc = split[1].split(",");
                            LatLng loc = new LatLng(Double.valueOf(splitLoc[0]), Double.valueOf(splitLoc[1]));
                            handleMarkerUpdate(split[0], loc, split[1].equals("quit"));
                        }
                    }
                }

                @Override
                public void onClose(int code, String reason, boolean remote) {
                    super.onClose(code, reason, remote);
                    if (ws.isOpen())
                        ws.broadcast(usr.getUsername() + ":quit");
                }
            };

        } catch (Exception e) {
            Log.d("WEBSOCKET", "Cache Map Socket Exception: " + e.getMessage());
        }
        ws.connect();
        if (ws.isOpen()) Log.d("WEBSOCKET", "Cache Map Socket Connected");

    }

    //Create a method for updating a marker location and adding and deleting them.
    public void handleMarkerUpdate(String user, LatLng loc, boolean delete) {
        Log.d("MARKERUPDATE", user + ":" + loc.toString() + ":" + delete);
        int counter;
        boolean exists = false;
        if (!delete) {
            for (counter = 0; counter < otherPlayers.size(); counter++) {
                //Update marker in the list
                if (otherPlayers.get(counter).getTitle().compareTo(user) == 0) {
                    otherPlayers.get(counter).setPosition(loc);
                    exists = true;
                }
            }
            if (!exists) {
                //Add into the list and map
                int height = 75;
                int width = 75;
                BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.other_marker);
                Bitmap b = bitmapdraw.getBitmap();
                Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
                otherPlayers.add(mMap.addMarker(new MarkerOptions().position(loc).title(user).icon(BitmapDescriptorFactory.fromBitmap(smallMarker))));
            }
        } else {
            for (counter = 0; counter < otherPlayers.size(); counter++) {
                if (otherPlayers.get(counter).getTitle().compareTo(user) == 0) {
                    //Delete the user from the map and the list if they exist
                    otherPlayers.get(counter).remove();
                    otherPlayers.remove(counter);
                }
            }
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This includes moving the camera to where the User is and drawing
     * all of the entities on the map.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Log.d("MAPSLOG", "Checking Maps");
        if (servicesOK()) {
            Log.d("MAPSLOG", "Initializing Maps");
            final LatLng cacheLocation = new LatLng(cache.getLatitude(), cache.getLongitude());
            float zoomLevel = 17.5f;
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(cacheLocation, zoomLevel));
            Log.d("MAPSLOG", "Maps Camera Moved");
            mMap.getUiSettings().setScrollGesturesEnabled(true);
            mMap.getUiSettings().setZoomGesturesEnabled(true);
            mMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
                @Override
                public void onCameraMove() {
                    if (mMap.getCameraPosition().zoom < 16f && (markerLocation == null || !markerLocation.isVisible())) {
                        if (markerLocation == null)
                            markerLocation = mMap.addMarker(new MarkerOptions().position(cacheLocation).title("Marker for " + cache.getName()));
                        markerLocation.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.test_marker));
                        markerLocation.setVisible(true);
                        Log.d("MAPSLOG", "Maps Marker Visible");
                    } else if (mMap.getCameraPosition().zoom >= 17.5f && markerLocation != null) {
                        markerLocation.setVisible(false);
                        Log.d("MAPSLOG", "Maps Marker Hidden");
                    }
                }
            });
            if (usr.getAuthority() > 1) {
                int height = 75;
                int width = 75;
                BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.cache_goal);
                Bitmap b = bitmapdraw.getBitmap();
                Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
                mMap.addMarker(new MarkerOptions().position(new LatLng(goal.getLatitude(), goal.getLongitude())).title("Goal Location").anchor(0.5f, 0.5f).icon(BitmapDescriptorFactory.fromBitmap(smallMarker)));
            }
            circle = mMap.addCircle(new CircleOptions()
                    .center(cacheLocation)
                    .radius(31)             //0.000279 single lat/longitude difference is equivalent to 31 meters
                    .strokeColor(Color.RED)
                    .fillColor(Color.BLUE & 0x44ffffff)); //First 2 hex values set the Alpha Channel
        }
    }

    /**
     * Checks if Google Play Services is usable by the application and gives a popup if it is not.
     *
     * @return true on connection success and false otherwise.
     * @author Parker Bibus
     */
    private boolean servicesOK() {
        int result = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this);
        if (result == ConnectionResult.SUCCESS) {
            return true;
        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(result)) {
            Log.d("MAPSLOG", "Error but user is recoverable");
            Toast.makeText(this, "User is resolvable", Toast.LENGTH_LONG).show();
        } else {
            Log.d("MAPSLOG", "Error with unrecoverable user.");
            Toast.makeText(this, "Error with google play!", Toast.LENGTH_LONG).show();
        }
        return false;
    }

    /**
     * Method for resuming the application if a user paused the application
     */
    @Override
    protected void onResume() {
        super.onResume();
        startLocationUpdates();
    }

    /**
     * Starts getting the location of the user and sets up the
     * constant updates of the information.
     */
    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }
        fusedLocationClient.requestLocationUpdates(mLocationRequest,
                mLocationCallback,
                null /* Looper */);
    }

    /**
     * Method called when the user pauses the application.
     */
    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    /**
     * Stops getting the location updates. Used when finishing the
     * activity or pausing the application.
     */
    private void stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(mLocationCallback);
        if (ws.isOpen())
            ws.broadcast(usr.getUsername() + ":quit");
    }

    /**
     * Used to handle location updates. Sets the map marker to where
     * the person is and checks if they are inside the goal. If they are
     * inside the goal, it finishes the activity with a success result.
     *
     * @param location
     * @author Parker Bibus
     */
    private void handleNewLocation(Location location) {
        Log.d("Location", location.toString());

        double currentLatitude = location.getLatitude();
        double currentLongitude = location.getLongitude();
        LatLng latLng = new LatLng(currentLatitude, currentLongitude);
        if (markerPlayer == null) {
            MarkerOptions options = new MarkerOptions()
                    .position(latLng)
                    .title("I am here!")
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.test_marker));
            markerPlayer = mMap.addMarker(options);

        } else {
            markerPlayer.setPosition(latLng);
            markerPlayer.setTitle("I have updated!!");
            if (ws.isOpen()) {
                ws.broadcast(usr.getUsername() + ":" + latLng.latitude + "," + latLng.longitude);
            }
        }
        if (insideGoal(location, goal)) {
            stopLocationUpdates();
            ws.broadcast(usr.getUsername() + ":found");
            Intent intent = new Intent();
            intent.putExtra("UserObject", usr);
            intent.putExtra("CacheObject", cache);
            setResult(Activity.RESULT_OK, intent);
            finish();
        }
    }

    /**
     * Handles the result of the location permission request
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocationUpdates();
            }
        } else {
            finish();
        }
    }

    /**
     * Gets a goal location that is inside the specified radius around the location
     *
     * @param location       location to be the center of the radius
     * @param radiusInMeters radius for which to place the goal in
     * @return the location of the users goal location
     * @author Aidan Sherburne
     */
    public Location getNearLocation(LatLng location, double radiusInMeters) {
        // Center point for our circle
        double lat = location.latitude;
        double lon = location.longitude;
        double newLat, newLon;

        Random rand = new Random();

        // Convert radius from meters to degrees
        double radiusInDegrees = radiusInMeters / 111320f;

        // Get a random distance and a random angle.
        double angle = radiusInDegrees * Math.sqrt(rand.nextDouble());
        double dist = 2 * Math.PI * rand.nextDouble();

        // Get delta values using x as longitude and y as latitude
        double x = angle * Math.cos(dist);
        double y = angle * Math.sin(dist);

        // Compensate the x value
        x = x / Math.cos(Math.toRadians(lat));

        newLat = lat + y;
        newLon = lon + x;

        Location copy = new Location("NewLocation");
        copy.setLatitude(newLat);
        copy.setLongitude(newLon);
        return copy;
    }

    /**
     * Checks if the user is inside the passed goal location.
     *
     * @param currLoc current location of the user
     * @param goalLoc goal location of the user
     * @return true if the user is inside the goal, false if not in the goal
     * @author Parker Bibus
     */
    public boolean insideGoal(Location currLoc, Location goalLoc) {
        if (currLoc.distanceTo(goalLoc) < 5) {
            return true;
        }
        return false;
    }
}
