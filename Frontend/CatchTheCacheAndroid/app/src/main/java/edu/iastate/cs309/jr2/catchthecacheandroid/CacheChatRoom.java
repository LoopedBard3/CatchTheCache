package edu.iastate.cs309.jr2.catchthecacheandroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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

import java.util.ArrayList;

import edu.iastate.cs309.jr2.catchthecacheandroid.adapters.CacheChatAdapter;
import edu.iastate.cs309.jr2.catchthecacheandroid.models.cache_models.Cache;
import edu.iastate.cs309.jr2.catchthecacheandroid.models.cache_models.CacheListRequest;
import edu.iastate.cs309.jr2.catchthecacheandroid.models.cache_models.CacheListResponse;
import edu.iastate.cs309.jr2.catchthecacheandroid.models.chat_models.Message;
import edu.iastate.cs309.jr2.catchthecacheandroid.models.chat_models.MessageListResponse;
import edu.iastate.cs309.jr2.catchthecacheandroid.models.chat_models.MessageRequest;
import edu.iastate.cs309.jr2.catchthecacheandroid.models.chat_models.MessageResponse;
import edu.iastate.cs309.jr2.catchthecacheandroid.models.user_models.User;

public class CacheChatRoom extends AppCompatActivity {
    private User usr;
    private Cache cache;
    private RecyclerView chatRecycleView;
    private LinearLayoutManager layoutManager;
    private ArrayList<Message> messages = new ArrayList<>();
    private Gson gson;
    private RequestQueue queue;
    private ProgressBar pbar;
    private EditText send_text;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cache_chat_room);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        chatRecycleView = (RecyclerView) findViewById(R.id.rvCacheChat);

        Bundle extras = getIntent().getExtras();
        usr = (User) extras.getSerializable("UserObject");
        cache = (Cache) extras.getSerializable("CacheObject");
        pbar = findViewById(R.id.cache_chat_retrieve_progress);
        send_text = findViewById(R.id.new_message);
        getSupportActionBar().setTitle(cache.name + " chat");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        queue = Volley.newRequestQueue(getApplicationContext());
        gson = new Gson();

        try {
            getCacheChatList();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        chatRecycleView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        chatRecycleView.setLayoutManager(layoutManager);

        // specify an adapter (see also next example)
        CacheChatAdapter mAdapter = new CacheChatAdapter(messages, usr);
        chatRecycleView.setAdapter(mAdapter);

        //initialize button
        ImageButton addMsg = findViewById(R.id.send_message);
        addMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    addCacheMessage();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

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

    private void getCacheChatList() throws JSONException {
        //TODO: Get caches from server instead
      //  JSONObject requestJSON = new JSONObject(gson.toJson(new CacheListRequest()));
       // Log.d("REQUESTJSON", gson.toJson(new CacheListRequest()).toString());
        pbar.setVisibility(View.VISIBLE);
        JsonObjectRequest requestObject = new JsonObjectRequest(Request.Method.GET, getString(R.string.access_url) + "caches/m/" + cache.getId(), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        MessageListResponse messageList = gson.fromJson(response.toString(), MessageListResponse.class);
                        messages.clear();
                        messages.addAll(messageList.getMessageList());
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

    private boolean addCacheMessage() throws JSONException {
        if(send_text.getText().length() <= 0 || send_text.getText().length() > 50 ) {
            send_text.setError("Please enter message between 1 and 50 letters long.");
           return false;
        }
        JSONObject cacheChatToSend;
        //TODO: Add in description adding.
        cacheChatToSend = new JSONObject(gson.toJson(new MessageRequest(usr.getUsername(),  send_text.getText().toString())));
        JsonObjectRequest requestObject = new JsonObjectRequest(Request.Method.POST, getString(R.string.access_url) + "caches/m/" + cache.getId(), cacheChatToSend,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        MessageResponse respJson = gson.fromJson(response.toString(), MessageResponse.class);
                        try {
                            getCacheChatList();
                            send_text.setText("");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ERRORRESPONSE", "Error getting responses " + error.toString());
            }
        });
        queue.add(requestObject);
        return true;
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
        refresh_caches_item.setVisible(true);
        refresh_caches_item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        refresh_caches_item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                try {
                    getCacheChatList();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return true;
            }
        });
        return true;
    }

}
