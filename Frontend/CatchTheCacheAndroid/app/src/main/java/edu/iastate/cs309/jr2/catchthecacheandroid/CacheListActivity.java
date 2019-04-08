package edu.iastate.cs309.jr2.catchthecacheandroid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.util.ArrayList;

import edu.iastate.cs309.jr2.catchthecacheandroid.adapters.CacheListAdapter;
import edu.iastate.cs309.jr2.catchthecacheandroid.models.WebSocketClient;
import edu.iastate.cs309.jr2.catchthecacheandroid.models.cache_models.Cache;
import edu.iastate.cs309.jr2.catchthecacheandroid.models.cache_models.CacheAddRequest;
import edu.iastate.cs309.jr2.catchthecacheandroid.models.cache_models.CacheAddResponse;
import edu.iastate.cs309.jr2.catchthecacheandroid.models.cache_models.CacheListRequest;
import edu.iastate.cs309.jr2.catchthecacheandroid.models.cache_models.CacheListResponse;
import edu.iastate.cs309.jr2.catchthecacheandroid.models.chat_models.MessageListResponse;
import edu.iastate.cs309.jr2.catchthecacheandroid.models.user_models.User;

public class CacheListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Cache> caches = new ArrayList<>();
    private RequestQueue queue;
    private Gson gson;
    private User usr;
    private ProgressBar pbar;
    private WebSocketClient ws;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        queue = Volley.newRequestQueue(getApplicationContext());
        gson = new Gson();
        setContentView(R.layout.activity_cache_list);
        recyclerView = (RecyclerView) findViewById(R.id.rvCacheList);
        usr = (User) extras.getSerializable("UserObject");
        pbar = findViewById(R.id.cache_retrieve_progress);



        if(extras.containsKey("ThroughServer") && !extras.getBoolean("ThroughServer")){
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
        mAdapter = new CacheListAdapter(caches, usr);
        recyclerView.setAdapter(mAdapter);



        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Caches for you");
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        //websocket stuff

        try {
            ws = new WebSocketClient(new URI(getString(R.string.access_socket) + "caches/websocket")) {

                @Override
                public void onMessage(String message) {
                    Log.d("WEBSOCKET", "Cache List Socket returned: " + message);
                    if (message.equals("refresh")) {
                        try {
                            getCacheListInvisible();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            };

        }catch (Exception e){
            Log.d("WEBSOCKET", "Cache List Socket Exception: " + e.getMessage());
        }
        ws.connect();
        if(ws.isOpen()) Log.d("WEBSOCKET", "Cache List Socket Connected");
    }



    private void getCacheList() throws JSONException {
            JSONObject requestJSON = new JSONObject(gson.toJson(new CacheListRequest()));
            Log.d("REQUESTJSON", gson.toJson(new CacheListRequest()).toString());
        pbar.setVisibility(View.VISIBLE);
            JsonObjectRequest requestObject = new JsonObjectRequest(Request.Method.GET, getString(R.string.access_url) + "caches", null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            CacheListResponse cachesList = gson.fromJson(response.toString(), CacheListResponse.class);
                            caches.clear();
                            caches.addAll(cachesList.getCacheList());
                            pbar.setVisibility(View.GONE);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    pbar.setVisibility(View.GONE);
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

        MenuItem refresh_caches_item = menu.findItem(R.id.action_refresh);
        refresh_caches_item.setVisible(false);
        refresh_caches_item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        refresh_caches_item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                try {
                    getCacheList();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return true;
            }
        });

        MenuItem open_admin_add_cache = menu.findItem(R.id.action_admin_add_cache);
        open_admin_add_cache.setVisible(usr.getAuthority() >= 2);
        open_admin_add_cache.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(getApplicationContext(), CacheAddActivity.class);
                intent.putExtra("UserObject", usr);
                startActivityForResult(intent, 1);
                //startActivity(intent);
                return true;
            }
        });
        return true;
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
        if(ws != null && !ws.isClosed()){
            ws.close();
        }
        finish();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == RESULT_OK){
                try {
                    getCacheList();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (resultCode == RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }
    private void getCacheListInvisible() throws JSONException {
        //TODO: Get caches from server instead
        //  JSONObject requestJSON = new JSONObject(gson.toJson(new CacheListRequest()));
        // Log.d("REQUESTJSON", gson.toJson(new CacheListRequest()).toString());
        JsonObjectRequest requestObject = new JsonObjectRequest(Request.Method.GET, getString(R.string.access_url) + "caches" , null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        CacheListResponse cachesList = gson.fromJson(response.toString(), CacheListResponse.class);
                        caches.clear();
                        caches.addAll(cachesList.getCacheList());
                        recyclerView.getAdapter().notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        queue.add(requestObject);
    }



}
