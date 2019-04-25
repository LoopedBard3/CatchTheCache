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
import edu.iastate.cs309.jr2.catchthecacheandroid.models.chat_models.ChatCreateRequest;
import edu.iastate.cs309.jr2.catchthecacheandroid.models.chat_models.ChatCreateResponse;
import edu.iastate.cs309.jr2.catchthecacheandroid.models.user_models.User;

/**
 * this is how a user will create a new chat
 */
public class ChatNewActivity extends AppCompatActivity
{
    private RequestQueue queue;
    private EditText username;
    private EditText message;
    private Button addBtn;
    private Gson gson;
    private User usr;
    int chat_Id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        queue = Volley.newRequestQueue(getApplicationContext());
        gson = new Gson();
        setContentView(R.layout.activity_chat_add);
        //TODO create new XML File
       // usr = (User) extras.getSerializable("UserObject");
        username = findViewById(R.id.username);
        message = findViewById(R.id.new_message);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
       // getSupportActionBar().setTitle("Add Chat");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        chat_Id = 1;

        addBtn = findViewById(R.id.new_chat_button);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    addChat();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }
    //TODO change to chat stuff
    public boolean chatValid (EditText username, EditText message){
        return username.length() != 0 && message.length() != 0;
    }

    public void addChat() throws JSONException {
        if (chatValid(username, message)) { //I put the null checkers and stuff up in a different method. I dont think  broke anything


            JSONObject chatToSend;
           chatToSend = new JSONObject(gson.toJson(new ChatCreateRequest( username.getText().toString(), chat_Id)));
            JsonObjectRequest requestObject = new JsonObjectRequest(Request.Method.POST, getString(R.string.access_url) + "caches", chatToSend,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            ChatCreateResponse respJson = gson.fromJson(response.toString(), ChatCreateResponse.class);
                            if (respJson.getSuccess()) {
                                finish_local();
                            } else {
                                message.setError("Unable to send message.");
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
        Intent intent = new Intent();
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}
