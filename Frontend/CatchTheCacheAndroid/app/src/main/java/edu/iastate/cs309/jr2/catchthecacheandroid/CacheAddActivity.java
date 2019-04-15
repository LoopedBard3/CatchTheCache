package edu.iastate.cs309.jr2.catchthecacheandroid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import edu.iastate.cs309.jr2.catchthecacheandroid.models.cache_models.CacheAddRequest;
import edu.iastate.cs309.jr2.catchthecacheandroid.models.cache_models.CacheAddResponse;
import edu.iastate.cs309.jr2.catchthecacheandroid.models.user_models.User;

/**
 * Activity that allows moderators to add a cache to the cache list
 * @author Parker Bibus
 */
public class CacheAddActivity extends AppCompatActivity
{
    private RequestQueue queue;
    private EditText mCacheName;
    private EditText mCacheLat;
    private EditText mCacheLong;
    private EditText mCacheDesc;
    private Button addBtn;
    private Button getCurrLocationBtn;
    private Gson gson;
    private User usr;
    String mCacheLongS;
    String mCacheLatS;
    String mCacheNameS;

    /**
     * Default method for starting the activity.
     * Sets up the Volley Queue, Gson json converter, and the add cache button
     * @author Parker Bibus
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        queue = Volley.newRequestQueue(getApplicationContext());
        gson = new Gson();
        setContentView(R.layout.activity_cache_add);
        usr = (User) extras.getSerializable("UserObject");
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Add Cache");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mCacheName = findViewById(R.id.add_cache_name);
        mCacheLat = findViewById(R.id.add_cache_latitude);
        mCacheLong = findViewById(R.id.add_cache_longitude);
        mCacheDesc = findViewById(R.id.add_cache_description);
        addBtn = findViewById(R.id.add_cache_button);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    addCache();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    /**
     * Checks if a specified set of cache inputs is value
     * @param mCacheLatS
     * @param mCacheNameS
     * @param mCacheLongS
     * @return  true if the cache is a valid cache and false otherwise
     */
    public boolean cacheValid (String mCacheLatS, String mCacheNameS, String mCacheLongS){
        if(mCacheLatS.length() == 0 || mCacheNameS.length() == 0 || mCacheLongS.length() == 0) {
            return false;
        }
        //This is checking the bounds of the Latitude
        String Latitude = mCacheLatS;
        try {
            int num = Integer.parseInt(Latitude);
            if (num > 180 || num < -180){
                return false;
            }
        } catch (NumberFormatException e) {
            Log.i("", Latitude + " is not within the bounds");
        }

        //This is checking the bounds of the Longitude
        String Longitude = mCacheLatS;
        try {
            int num = Integer.parseInt(Longitude);
            if (num > 180 || num < -180){
                return false;
            }
        } catch (NumberFormatException e) {
            Log.i("", Longitude + " is not within the bounds");
        }
        return true;
    }

    /**
     * Sends the cache information to the server using Volley.
     * @author Parker Bibus
     * @throws JSONException
     */
    public void addCache() throws JSONException {
        mCacheLatS = mCacheLat.getText().toString();
        mCacheLongS = mCacheLong.getText().toString();
        mCacheNameS = mCacheName.getText().toString();
        if (cacheValid(mCacheLatS, mCacheNameS, mCacheLongS)) { //I put the null checkers and stuff up in a different method. I dont think  broke anything


            JSONObject cacheToSend;
            cacheToSend = new JSONObject(gson.toJson(new CacheAddRequest(mCacheName.getText().toString(), Double.parseDouble(mCacheLong.getText().toString()), Double.parseDouble(mCacheLat.getText().toString()), usr.getUsername(), mCacheDesc.getText().toString())));
            JsonObjectRequest requestObject = new JsonObjectRequest(Request.Method.POST, getString(R.string.access_url) + "caches", cacheToSend,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            CacheAddResponse respJson = gson.fromJson(response.toString(), CacheAddResponse.class);
                            if (respJson.getSuccess()) {
                                finish_local();
                            } else {
                                mCacheName.setError("Unable to add cache.");
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("ERRORRESPONSE", "Error getting responses " + error.toString());
                }
            });
            queue.add(requestObject);
        }
    }

    /**
     * Method that gets called when an option on the appbar is selected.
     * This one sets up the back button to finish the activity.
     * @param item
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
     */
    private void finish_local(){
        Intent intent = new Intent();
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}
