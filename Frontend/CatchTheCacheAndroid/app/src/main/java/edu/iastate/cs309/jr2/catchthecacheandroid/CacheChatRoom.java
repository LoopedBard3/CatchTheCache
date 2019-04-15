package edu.iastate.cs309.jr2.catchthecacheandroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.ColorInt;
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
import android.view.ViewGroup;
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

import java.net.URI;
import java.util.ArrayList;

import edu.iastate.cs309.jr2.catchthecacheandroid.adapters.CacheChatAdapter;
import edu.iastate.cs309.jr2.catchthecacheandroid.models.WebSocketClient;
import edu.iastate.cs309.jr2.catchthecacheandroid.models.cache_models.Cache;
import edu.iastate.cs309.jr2.catchthecacheandroid.models.chat_models.Message;
import edu.iastate.cs309.jr2.catchthecacheandroid.models.chat_models.MessageListResponse;
import edu.iastate.cs309.jr2.catchthecacheandroid.models.chat_models.MessageRequest;
import edu.iastate.cs309.jr2.catchthecacheandroid.models.chat_models.MessageResponse;
import edu.iastate.cs309.jr2.catchthecacheandroid.models.user_models.User;

//import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;


/**
 * Activity for viewing the individual chat room for the chat
 * @author Parker Bibus
 */
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
    private WebSocketClient ws;




    /**
     * Default method for starting the activity.
     * Sets up the Volley Queue, websocket, and add message button
     * @author Parker Bibus
     * @param savedInstanceState
     */
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

        try {
            ws = new WebSocketClient(new URI(getString(R.string.access_socket) + "caches/m/" + cache.getId() + "/websocket")) {

                @Override
                public void onMessage(String message) {
                    Log.d("WEBSOCKET", "Cache Chat Socket returned: " + message);
                    if (message.equals("refresh")) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        getCacheChatListInvisible();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                    }
                }
            };

        }catch (Exception e){
            Log.d("WEBSOCKET", "Cache Chat Socket Exception: " + e.getMessage());
        }
        ws.connect();
        if(ws.isOpen()) Log.d("WEBSOCKET", "Cache Chat Socket Connected");
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
     * Finish method that closes the websocket and allows for finish
     * call when not at base level of nesting.
     * @author Parker Bibus
     */
    private void finish_local(){
        if(ws != null && !ws.isClosed()){
            ws.close();
        }
        finish();
    }

    /**
     * Gets the cache list from the server using http. Also sets the progress bar to visible
     * while getting the list from the server and adds the messages to the recycler viewer data array.
     * @throws JSONException
     */
    private void getCacheChatList() throws JSONException {
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

    /**
     * Checks the message in the message box and, if it is valid,
     * it sends the message to the server.
     * @return true on successful send and false otherwise.
     * @throws JSONException
     */
    private boolean addCacheMessage() throws JSONException {
        if(send_text.getText().length() <= 0 || send_text.getText().length() > 50 ) {
            send_text.setError("Please enter message between 1 and 50 letters long.");
           return false;
        }

        if(!ws.broadcast(usr.getUsername() + ":" +send_text.getText().toString())) {
            JSONObject cacheChatToSend;
            cacheChatToSend = new JSONObject(gson.toJson(new MessageRequest(usr.getUsername(), send_text.getText().toString())));
            JsonObjectRequest requestObject = new JsonObjectRequest(Request.Method.POST, getString(R.string.access_url) + "caches/m/" + cache.getId(), cacheChatToSend,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            MessageResponse respJson = gson.fromJson(response.toString(), MessageResponse.class);
                            send_text.setText("");
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("ERRORRESPONSE", "Error getting responses " + error.toString());
                }
            });
            queue.add(requestObject);
            return true;
        }else{
            send_text.setText("");
        }
        return true;
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
     * Gets the cache list from the server using http. Also adds the messages to the
     * recycler viewer data array and notifies the viewer of the update.
     * @throws JSONException
     */
    private void getCacheChatListInvisible() throws JSONException {
        JsonObjectRequest requestObject = new JsonObjectRequest(Request.Method.GET, getString(R.string.access_url) + "caches/m/" + cache.getId(), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        MessageListResponse messageList = gson.fromJson(response.toString(), MessageListResponse.class);
                        messages.clear();
                        messages.addAll(messageList.getMessageList());
                        chatRecycleView.getAdapter().notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        queue.add(requestObject);
    }
}
