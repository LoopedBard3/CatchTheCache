package edu.iastate.cs309.jr2.catchthecacheandroid;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
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
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;

import edu.iastate.cs309.jr2.catchthecacheandroid.models.cache_models.Cache;
import edu.iastate.cs309.jr2.catchthecacheandroid.models.user_models.User;

public class CacheViewActivity extends AppCompatActivity implements OnMapReadyCallback{
    private User usr;
    private Cache cache;
    private TextView desc;
    private Button btn;
    private Button btn2;
    private GoogleMap mMap;

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
        btn2.setText("View the chat room!");
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CacheChatRoom.class);
                intent.putExtra("UserObject", usr);
                intent.putExtra("CacheObject", cache);
                startActivity(intent);
            }
        });
        getSupportActionBar().setTitle(cache.name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        SupportMapFragment mapFragment =  (SupportMapFragment)  getSupportFragmentManager().findFragmentById(R.id.mapView);
        mapFragment.getMapAsync(this);
    }

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

    private void finish_local(){
        finish();
    }

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


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Log.d("MAPS", "Checking Maps");
        if(servicesOK()) {
            Log.d("MAPS", "Initializing Maps");
            // Add a marker on gerdin and move the camera
            LatLng cacheLocation = new LatLng(cache.getLatitude(), cache.getLongitude());
            //mMap.addMarker(new MarkerOptions().position(cacheLocation).title("Marker for " + cache.getName()));
            //Log.d("MAPS", "Maps Marker Added");
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
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

//        final Dialog dialog = new Dialog(this); // Context, this, etc.
//        dialog.setContentView(R.layout.dialog_successful_find);
//        Button btnYes = findViewById(R.id.dialog_enter_cache_chat);
//        Button btnNo = findViewById(R.id.dialog_ignore_cache_chat);
//        btnYes.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                Intent intent = new Intent(getApplicationContext(), CacheChatRoom.class);
////                intent.putExtra("UserObject", usr);
////                intent.putExtra("CacheObject", cache);
////                startActivity(intent);
//            }
//        });
//
//        btnNo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//            }
//        });
//        dialog.setTitle("You found the Cache!");
//        dialog.show();
//    }
}
