package edu.iastate.cs309.jr2.catchthecacheandroid;

import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
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

import java.util.ArrayList;

import edu.iastate.cs309.jr2.catchthecacheandroid.adapters.CacheListAdapter;
import edu.iastate.cs309.jr2.catchthecacheandroid.models.cache_models.Cache;
import edu.iastate.cs309.jr2.catchthecacheandroid.models.cache_models.CacheAddRequest;
import edu.iastate.cs309.jr2.catchthecacheandroid.models.cache_models.CacheAddResponse;
import edu.iastate.cs309.jr2.catchthecacheandroid.models.cache_models.CacheListRequest;
import edu.iastate.cs309.jr2.catchthecacheandroid.models.cache_models.CacheListResponse;
import edu.iastate.cs309.jr2.catchthecacheandroid.models.user_models.User;

public class CacheListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Cache> caches = new ArrayList<>();
    private RequestQueue queue;
    private EditText mCacheName;
    private EditText mCacheLat;
    private EditText mCacheLong;
    private Button addBtn;
    private Gson gson;
    private User usr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        queue = Volley.newRequestQueue(getApplicationContext());
        gson = new Gson();
        setContentView(R.layout.activity_cache_list);
        mCacheName = findViewById(R.id.nameInput);
        mCacheLat = findViewById(R.id.latInput);
        mCacheLong = findViewById(R.id.longInput);
        addBtn  = findViewById(R.id.addCacheBtn);
        recyclerView = (RecyclerView) findViewById(R.id.rvCacheList);
        usr = new User(extras.getString("Name"), extras.getInt("Auth"));
        if(usr.getAuthority() < 2) {
            findViewById(R.id.textInputLayout).setVisibility(View.INVISIBLE);
            findViewById(R.id.textInputLayout2).setVisibility(View.INVISIBLE);
            findViewById(R.id.textInputLayout3).setVisibility(View.INVISIBLE);
            addBtn.setVisibility(View.INVISIBLE);
            ((ViewGroup.MarginLayoutParams)findViewById(R.id.listLayout).getLayoutParams()).bottomMargin = 0;
        }


        if(!extras.getBoolean("ThroughServer")){
            addTestCaches();
        }else {
            try {
                getCacheList();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // specify an adapter (see also next example)
        mAdapter = new CacheListAdapter(caches);
        recyclerView.setAdapter(mAdapter);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if(addCache()) {
                        getCacheList();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private boolean addCache() throws JSONException {
        if(mCacheLat.getText().length() == 0 || mCacheName.getText().length() == 0 || mCacheLong.getText().length() == 0) {
            return false;
        }
        JSONObject cacheToSend;
        cacheToSend = new JSONObject(gson.toJson(new CacheAddRequest(mCacheName.getText().toString(), Double.parseDouble(mCacheLat.getText().toString()), Double.parseDouble(mCacheLong.getText().toString()), usr.getUsername())));
        JsonObjectRequest requestObject = new JsonObjectRequest(Request.Method.POST, getString(R.string.access_url) + "caches", cacheToSend,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        CacheAddResponse respJson = gson.fromJson(response.toString(), CacheAddResponse.class);
                        try {
                            getCacheList();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                return;
            }
        });
        queue.add(requestObject);
        return true;
    }

    private void getCacheList() throws JSONException {
        //TODO: Get caches from server instead
            JSONObject requestJSON = new JSONObject(gson.toJson(new CacheListRequest()));
            Log.d("REQUESTJSON", gson.toJson(new CacheListRequest()).toString());
            JsonObjectRequest requestObject = new JsonObjectRequest(Request.Method.GET, getString(R.string.access_url) + "caches", null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            CacheListResponse cachesList = gson.fromJson(response.toString(), CacheListResponse.class);
                            caches.clear();
                            caches.addAll(cachesList.getCacheList());
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            });

            queue.add(requestObject);
        }

    private void addTestCaches(){
        caches.add(new Cache("test1", 123.124, 1241.124));
        caches.add(new Cache("test2", 123.125, 1241.125));
        caches.add(new Cache("test3", 123.126, 1241.126));
        caches.add(new Cache("test4", 123.127, 1241.1278));
        caches.add(new Cache("test5", 123.128, 1241.128));
        caches.add(new Cache("test6", 123.129, 1241.129));
        caches.add(new Cache("test7", 123.1, 1241.12));
        caches.add(new Cache("test8", 123.16, 1241.13));
        caches.add(new Cache("test9", 123.17, 1241.14));
        caches.add(new Cache("test11", 123.124, 1241.124));
        caches.add(new Cache("test12", 123.125, 1241.125));
        caches.add(new Cache("test13", 123.126, 1241.126));
        caches.add(new Cache("test14", 123.127, 1241.1278));
        caches.add(new Cache("test15", 123.128, 1241.128));
        caches.add(new Cache("test16", 123.129, 1241.129));
        caches.add(new Cache("test17", 123.1, 1241.12));
        caches.add(new Cache("test18", 123.16, 1241.13));
        caches.add(new Cache("test19", 123.17, 1241.14));
    }
}
