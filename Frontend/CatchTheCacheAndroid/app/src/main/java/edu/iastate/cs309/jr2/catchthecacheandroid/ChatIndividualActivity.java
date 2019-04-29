package edu.iastate.cs309.jr2.catchthecacheandroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
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

import edu.iastate.cs309.jr2.catchthecacheandroid.adapters.ChatAdapter;
import edu.iastate.cs309.jr2.catchthecacheandroid.models.WebSocketClient;
import edu.iastate.cs309.jr2.catchthecacheandroid.models.cache_models.Cache;
import edu.iastate.cs309.jr2.catchthecacheandroid.models.chat_models.Message;
import edu.iastate.cs309.jr2.catchthecacheandroid.models.chat_models.MessageListResponse;
import edu.iastate.cs309.jr2.catchthecacheandroid.models.chat_models.MessageRequest;
import edu.iastate.cs309.jr2.catchthecacheandroid.models.chat_models.MessageResponse;
import edu.iastate.cs309.jr2.catchthecacheandroid.models.user_models.User;

//import org.java_websocket.client.WebSocketClient;


public class ChatIndividualActivity extends AppCompatActivity {
    private User usr;
    //TODO create new fields
    private Cache cache;
    private RecyclerView chatRecycleView;
    private LinearLayoutManager layoutManager;
    private ArrayList<Message> messages = new ArrayList<>();
    private Gson gson;
    private RequestQueue queue;
    private ProgressBar pbar;
    private EditText send_text;
    private WebSocketClient ws;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_chat_room);
        //TODO create new xml file
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        chatRecycleView = (RecyclerView) findViewById(R.id.rvChat);

        Bundle extras = getIntent().getExtras();
        usr = (User) extras.getSerializable("UserObject");
        cache = (Cache) extras.getSerializable("CacheObject");
        pbar = findViewById(R.id.chat_retrieve_progress);
        send_text = findViewById(R.id.new_message);
        getSupportActionBar().setTitle(cache.name + " chat");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        queue = Volley.newRequestQueue(getApplicationContext());
        gson = new Gson();

        try {
            getChatList();
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

        ChatAdapter mAdapter = new ChatAdapter(messages, usr);
        chatRecycleView.setAdapter(mAdapter);

        //initialize button
        ImageButton addMsg = findViewById(R.id.send_message);
        addMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    addChatMessage();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

        try {
            ws = new WebSocketClient(new URI(getString(R.string.access_socket) + "caches/m/" + cache.getId() + "/websocket")) {
            //TODO what is the url for chats
                @Override
                public void onMessage(String message) {
                    Log.d("WEBSOCKET", "Chat Socket returned: " + message);
                    if (message.equals("refresh")) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    getChatListInvisible();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                }
            };

        }catch (Exception e){
            Log.d("WEBSOCKET", "Chat Socket Exception: " + e.getMessage());
        }
        ws.connect();
        if(ws.isOpen()) Log.d("WEBSOCKET", "Chat Socket Connected");
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

    private void getChatList() throws JSONException {
        //TODO: Get chats from server instead
        //  JSONObject requestJSON = new JSONObject(gson.toJson(new CacheListRequest()));
        // Log.d("REQUESTJSON", gson.toJson(new CacheListRequest()).toString());
        pbar.setVisibility(View.VISIBLE);
        JsonObjectRequest requestObject = new JsonObjectRequest(Request.Method.GET, "ws://10.26.44.107:8080/chats/m/1/websocket", null,
          //TODO check url again
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

    private boolean addChatMessage() throws JSONException {
        if(send_text.getText().length() <= 0 || send_text.getText().length() > 50 ) {
            send_text.setError("Please enter message between 1 and 50 letters long.");
            return false;
        }

        if(!ws.broadcast(usr.getUsername() + ":" +send_text.getText().toString())) {
            JSONObject chatToSend;
            //TODO: Add in description adding.
            chatToSend = new JSONObject(gson.toJson(new MessageRequest(usr.getUsername(), send_text.getText().toString())));
            JsonObjectRequest requestObject = new JsonObjectRequest(Request.Method.POST, getString(R.string.access_url) + "chats/m/{id}/websocket", null,
                   //TODO i dont think the last argument is supposed to be null
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            MessageResponse respJson = gson.fromJson(response.toString(), MessageResponse.class);
                            //try {
                            //getCacheChatList();
                            send_text.setText("");
                            //} catch (JSONException e) {
                            //    e.printStackTrace();
                            //}
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        //TODO change menu
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


    private void getChatListInvisible() throws JSONException {
        //TODO: Get caches from server instead
        //  JSONObject requestJSON = new JSONObject(gson.toJson(new CacheListRequest()));
        // Log.d("REQUESTJSON", gson.toJson(new CacheListRequest()).toString());
        JsonObjectRequest requestObject = new JsonObjectRequest(Request.Method.GET, getString(R.string.access_url) + "chats/m/{id}/websocket", null,
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
