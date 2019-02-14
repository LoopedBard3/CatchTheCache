package edu.iastate.cs309.jr2.catchthecacheandroid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;

import edu.iastate.cs309.jr2.catchthecacheandroid.adapters.CacheListAdapter;
import edu.iastate.cs309.jr2.catchthecacheandroid.models.cache_models.CacheIndividual;
import edu.iastate.cs309.jr2.catchthecacheandroid.models.cache_models.CacheListResponse;
import edu.iastate.cs309.jr2.catchthecacheandroid.models.user_models.UserLoginResponse;

public class CacheListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<CacheIndividual> caches = new ArrayList<>();
    private RequestQueue queue;
    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        queue = Volley.newRequestQueue(getApplicationContext());
        gson = new Gson();
        setContentView(R.layout.activity_cache_list);
        recyclerView = (RecyclerView) findViewById(R.id.rvCacheList);
        //TODO: Get caches from server instead
        if(!getIntent().getExtras().getBoolean("ThroughServer")){
            addTestCaches();
        }else{
            JsonObjectRequest requestObject = new JsonObjectRequest(Request.Method.GET, getString(R.string.access_url) + "caches/list",null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            CacheListResponse respJson = gson.fromJson(response.toString(), CacheListResponse.class);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            });
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
    }

    private void addTestCaches(){
        caches.add(new CacheIndividual("test1", 123.124, 1241.124));
        caches.add(new CacheIndividual("test2", 123.125, 1241.125));
        caches.add(new CacheIndividual("test3", 123.126, 1241.126));
        caches.add(new CacheIndividual("test4", 123.127, 1241.1278));
        caches.add(new CacheIndividual("test5", 123.128, 1241.128));
        caches.add(new CacheIndividual("test6", 123.129, 1241.129));
        caches.add(new CacheIndividual("test7", 123.1, 1241.12));
        caches.add(new CacheIndividual("test8", 123.16, 1241.13));
        caches.add(new CacheIndividual("test9", 123.17, 1241.14));
        caches.add(new CacheIndividual("test11", 123.124, 1241.124));
        caches.add(new CacheIndividual("test12", 123.125, 1241.125));
        caches.add(new CacheIndividual("test13", 123.126, 1241.126));
        caches.add(new CacheIndividual("test14", 123.127, 1241.1278));
        caches.add(new CacheIndividual("test15", 123.128, 1241.128));
        caches.add(new CacheIndividual("test16", 123.129, 1241.129));
        caches.add(new CacheIndividual("test17", 123.1, 1241.12));
        caches.add(new CacheIndividual("test18", 123.16, 1241.13));
        caches.add(new CacheIndividual("test19", 123.17, 1241.14));
    }
}
