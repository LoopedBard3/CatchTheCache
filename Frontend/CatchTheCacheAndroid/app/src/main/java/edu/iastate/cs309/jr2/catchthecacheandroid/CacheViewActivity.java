package edu.iastate.cs309.jr2.catchthecacheandroid;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
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


import edu.iastate.cs309.jr2.catchthecacheandroid.models.cache_models.Cache;
import edu.iastate.cs309.jr2.catchthecacheandroid.models.user_models.User;

/**
 * Activity for displaying information about an individual cache
 */
public class CacheViewActivity extends AppCompatActivity implements OnMapReadyCallback{
    private User usr;
    private Cache cache;
    private TextView desc;
    private Button btn;
    private Button btn2;
    private GoogleMap mMap;

    /**
     * Default method for starting the activity.
     * Sets up the button for starting going caching.
     * @author Parker Bibus
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)

    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cache_view);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle extras = getIntent().getExtras();
        usr = (User) extras.getSerializable("UserObject");
        cache = (Cache) extras.getSerializable("CacheObject");

        desc = findViewById(R.id.CacheDescription);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            desc.setTextAppearance(R.style.TextAppearance_AppCompat_Large);
        }
        if(TextUtils.isEmpty(cache.description)) desc.setText(getString(R.string.cache_description_testing, cache.getLatitude(), cache.getLongitude()));
        else desc.setText(cache.getDescription());

        btn = findViewById(R.id.start_caching_button);
        btn.setText(getString(R.string.btn_start_caching, cache.getName()));
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                intent.putExtra("UserObject", usr);
                intent.putExtra("CacheObject", cache);
                startActivityForResult(intent, 1);
            }
        });

        btn2 = findViewById(R.id.enter_chat_room);
        btn2.setText("Testing Button");
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openChatOptionDialog();
            }
        });
        if(usr.getAuthority() > 0) btn2.setVisibility(View.VISIBLE);
        else btn2.setVisibility(View.GONE);
        getSupportActionBar().setTitle(cache.name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        SupportMapFragment mapFragment =  (SupportMapFragment)  getSupportFragmentManager().findFragmentById(R.id.mapView);
        mapFragment.getMapAsync(this);
    }

    /**
     * Method that gets called when an option on the appbar is selected.
     * This one sets up the back button to finish the activity.
     * @author Parker Bibus
     * @param item menu item clicked
     * @return true on success, false on fail or unknown item
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        Log.d("OPTIONSSELECT", String.valueOf(item.getItemId()));
        switch(item.getItemId()) {
            case android.R.id.home:
                finish_local();
                return true;

            default:
                return false;
        }
    }

    /**
     * Finish method that returns an ok result and allows for finish
     * call when not at base level of nesting.
     * @author Parker Bibus
     */
    private void finish_local(){
        finish();
    }


    /**
     * Method called by android to create the options menu at the top
     * that pop up when you press the three dots in the upper right.
     * @param menu menu item that the options get inflated into
     * @return true on success, false otherwise.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_cache, menu);
        MenuItem to_chat_item = menu.findItem(R.id.action_to_chat);
        to_chat_item.setVisible(true);
        to_chat_item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                return true;
            }
        });
        return true;
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This includes moving the camera to where the cache is and drawing
     * all of the entities on the map.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Log.d("MAPS", "Checking Maps");
        if(servicesOK()) {
            Log.d("MAPS", "Initializing Maps");
            LatLng cacheLocation = new LatLng(cache.getLatitude(), cache.getLongitude());
            float zoomLevel = 17.5f;
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(cacheLocation, zoomLevel));
            Log.d("MAPS", "Maps Camera Moved");
            mMap.getUiSettings().setScrollGesturesEnabled(false);
            mMap.getUiSettings().setZoomGesturesEnabled(false);

            Circle circle = mMap.addCircle(new CircleOptions()
                    .center(cacheLocation)
                    .radius(31)             //0.000279 single lat/longitude difference is equivalent to 31 meters
                    .strokeColor(Color.RED)
                    .fillColor(Color.BLUE & 0x44ffffff)); //First 2 hex values set the Alpha Channel


        }
    }

    /**
     * Checks if Google Play Services is usable by the application and gives a popup if it is not.
     * @author Parker Bibus
     * @return true on connection success and false otherwise.
     */
    private boolean servicesOK() {
        int result = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this);
        if(result == ConnectionResult.SUCCESS){
            return true;
        }else if(GoogleApiAvailability.getInstance().isUserResolvableError(result)){
            Log.d("MAPS", "Error but user is recoverable");
            Toast.makeText(this, "User is resolvable", Toast.LENGTH_LONG).show();
        }else{
            Log.d("MAPS", "Error with unrecoverable user.");
            Toast.makeText(this, "Error with google play!", Toast.LENGTH_LONG).show();
        }
        return false;
    }


    /**
     * Method called when an activity started for a result returns.
     * This one opens the chat option dialog.
     * @author Parker Bibus
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == RESULT_OK){
                //Ask if the user wants to go to the chat
                Bundle extras = getIntent().getExtras();
                usr = (User) extras.getSerializable("UserObject");
                cache = (Cache) extras.getSerializable("CacheObject");
                openChatOptionDialog();
            }
            if (resultCode == RESULT_CANCELED) {
                //Do nothing because the user didn't get the cache.
            }
        }
    }


    /**
     * Opens the dialog that starts the cache chat activity allowing users
     * that found the cache to leave a chat.
     * @author Parker Bibus
     */
    public void openChatOptionDialog(){
        new AlertDialog.Builder(this)
                .setTitle("You found the Cache!!")
                .setMessage("Do you want to leave a message at the Cache?")

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getApplicationContext(), CacheChatRoom.class);
                        intent.putExtra("UserObject", usr);
                        intent.putExtra("CacheObject", cache);
                        startActivity(intent);
                    }
                })

                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton(android.R.string.no, null)
                .setIcon(R.drawable.logo)
                .show();
    }
}
