package com.example.experimentallogin;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class BasicActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final RequestQueue queue = Volley.newRequestQueue(this);
        final String url = "http://prometheus.student.iastate.edu:8080/";
        Intent intent = getIntent();

        setContentView(R.layout.activity_basic);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        Button logoutBtn = findViewById(R.id.logoutButton);
        final TextView mTextView = findViewById(R.id.messageView);
        mTextView.setText(intent.getExtras().getString("message", "You logged in successfully!"));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Get message from the Server", Snackbar.LENGTH_LONG)
                        .setAction("Get Message", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                                        new Response.Listener<String>() {
                                            @SuppressLint("SetTextI18n")
                                            @Override
                                            public void onResponse(String response) {
                                                // Display the first 500 characters of the response string.
                                                mTextView.setText("Response is: "+ response.substring(0, response.length() - 1));
                                            }
                                        }, new Response.ErrorListener() {
                                    @SuppressLint("SetTextI18n")
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        mTextView.setText("Sorry, getting the message failed! :( ");
                                    }
                                });
                                // Add the request to the RequestQueue.
                                queue.add(stringRequest);
                            }
                        }).show();
            }
        });

        logoutBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }


}
