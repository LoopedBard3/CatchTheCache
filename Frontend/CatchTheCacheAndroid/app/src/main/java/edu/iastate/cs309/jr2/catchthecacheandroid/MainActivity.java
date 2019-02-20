package edu.iastate.cs309.jr2.catchthecacheandroid;

import android.app.Notification;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;




import edu.iastate.cs309.jr2.catchthecacheandroid.dummy.DummyContent;
import edu.iastate.cs309.jr2.catchthecacheandroid.ui.main.MainFragment;

public class MainActivity extends AppCompatActivity implements chatFragment.OnListFragmentInteractionListener {
    private EditText mUserView;
    private EditText mCacheIdView;
    private EditText mIdView;
//work on implementing more responses
    //work on responsive buttons
    //create individual chat pages


    // json object response url
    private String urlJsonObj = "https://api.androidhive.info/volley/person_object.json";

    // json array response url
    private Button btnMakeObjectRequest, btnMakeArrayRequest;
    private RequestQueue queue;

    TextView tvIsConnected;

    // Progress dialog
    private ProgressDialog pDialog;
    TextView tvResult;
    private TextView txtResponse;
    private Gson gson;
    // temporary string to show the parsed response
    private String jsonResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, MainFragment.newInstance())
                    .commitNow();
        }
      //  tvIsConnected = (TextView) findViewById(R.id.tvIsConnected);
      //  tvResult = (TextView) findViewById(R.id.tvResult);
        //super.onCreate(savedInstanceState);
        //setContentView(R.layout.main_fragment);
        configureBackButton();
        //configureRecyleButton();
        //configureCacheToggle();

        mUserView = findViewById(R.id.user);
        mCacheIdView = findViewById(R.id.cacheId);
        mIdView = findViewById(R.id.id);

      //  checkNetworkConnection();




    }
    public boolean checkNetworkConnection() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        boolean isConnected = false;
        if (networkInfo != null && (isConnected = networkInfo.isConnected())) {
            // show "Connected" & type of network "WIFI or MOBILE"
            tvIsConnected.setText("Connected "+networkInfo.getTypeName());
            // change background color to red
           tvIsConnected.setBackgroundColor(0xFF7CCC26);


        } else {
            // show "Not Connected"
            tvIsConnected.setText("Not Connected");
            // change background color to green
           tvIsConnected.setBackgroundColor(0xFFFF0000);
        }

        return isConnected;
    }


    private class HTTPAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            // params comes from the execute() call: params[0] is the url.
            try {
                try {
                    return HttpPost(urls[0]);
                } catch (JSONException e) {
                    e.printStackTrace();
                    return "Error!";
                }
            } catch (IOException e) {
                return "Unable to retrieve web page. URL may be invalid.";
            }
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {

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


    //TODO change to fit our Stuff
    private JSONObject buildJsonObject() throws JSONException {

        JSONObject jsonObject = new JSONObject();
        jsonObject.accumulate("user", mUserView.getText().toString());
        jsonObject.accumulate("cacheId",  mCacheIdView.getText().toString());
        jsonObject.accumulate("ID",  mIdView.getText().toString());

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


    public void send(View view) {
        Toast.makeText(this, "Clicked", Toast.LENGTH_SHORT).show();
        // perform HTTP POST request
        if(checkNetworkConnection())
            new HTTPAsyncTask().execute("htttp://localhost:8080");
        else
            Toast.makeText(this, "Not Connected!", Toast.LENGTH_SHORT).show();

    }



    private void configureBackButton(){
        Button nextbutton = (Button) findViewById(R.id.back);
        nextbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
        });
    }


    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem item) {

    }
}
