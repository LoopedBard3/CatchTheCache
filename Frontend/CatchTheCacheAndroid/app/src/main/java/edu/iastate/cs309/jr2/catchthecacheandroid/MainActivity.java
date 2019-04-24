package edu.iastate.cs309.jr2.catchthecacheandroid;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.MergeCursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.android.volley.RequestQueue;
import com.google.gson.Gson;
import com.lifeofcoding.cacheutlislibrary.CacheUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.function.Function;

public class MainActivity extends AppCompatActivity {
    static final int REQUEST_PERMISSION_KEY = 1;
    ArrayList<HashMap<String, String>> smsList = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> tmpList = new ArrayList<HashMap<String, String>>();
    ArrayList<String> urls = new ArrayList<>();
    private String TAG = MainActivity.class.getSimpleName();
    private ListView lv;

    private EditText message;
    private EditText username;
    private EditText chatID;

    static MainActivity inst;

    ListView listView;
    FloatingActionButton fab_new;
    ProgressBar loader;
    int i;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //initialize everything
        setContentView(R.layout.main_activity);
        CacheUtils.configureCache(this);
        listView =  findViewById(R.id.listView);
        loader =  findViewById(R.id.loader);
        fab_new =  findViewById(R.id.fab_new);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        listView.setEmptyView(loader);
        urls.add("anniede-mbp.student.iastate.edu.");
        fab_new.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ChatListActivity.class));
            }
        });

             configureBackButton();
             //TODO create back button

    }

    public void Post(String number, String message){
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost("anniede-mbp.student.iastate.edu."); //URL for server
            JSONObject jsonObject = new JSONObject(); //create new JSON object
            jsonObject.accumulate("chatID", chatID); //JSON object with ChatID
            jsonObject.accumulate("message", message); //add mesage
            StringEntity se = new StringEntity(jsonObject.toString()); //convert to string
            httpPost.setEntity(se); //post
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");
            httpclient.execute(httpPost);

        } catch (Exception e) {
        }
    }
    private String HttpPost(String myUrl) throws IOException, JSONException {
        String result = "";

        URL url = new URL(myUrl);

        // 1. create HttpURLConnection
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");

        // 2. build JSON object
        JSONObject jsonObject = buildJsonObject();

        // 3. add JSON content to POST request body
        setPostRequestContent(conn, jsonObject);

        // 4. make POST request to the given URL
        conn.connect();

        // 5. return response message
        return conn.getResponseMessage()+"";

    }
    private JSONObject buildJsonObject() throws JSONException {

        JSONObject jsonObject = new JSONObject();
        jsonObject.accumulate("user", username.getText().toString());
        jsonObject.accumulate("cacheId",  chatID.getText().toString());
        jsonObject.accumulate("ID",  message.getText().toString());

        return jsonObject;
    }
    private void setPostRequestContent(HttpURLConnection conn,
                                       JSONObject jsonObject) throws IOException {
        OutputStream os = conn.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
        writer.write(jsonObject.toString());
        Log.i(MainActivity.class.toString(), jsonObject.toString());
        writer.flush();
        writer.close();
        os.close();
    }









    class InboxViewHolder {
        ImageView inbox_thumb;
        TextView inbox_user, inbox_msg, inbox_date;
    }

    private void local_finish(){
        finish();
    }


    private void configureBackButton(){
        Button nextbutton = (Button) findViewById(R.id.back);
        nextbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                local_finish();
                //startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
        });
    }


}
