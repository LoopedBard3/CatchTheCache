package edu.iastate.cs309.jr2.catchthecacheandroid;

import android.app.Activity;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by acg
 */

public class NewSmsActivity extends AppCompatActivity{

    EditText address, message, username;
    Button send_btn;

    /**
     * start up method
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_new);

        address = (EditText) findViewById(R.id.address);
        username = (EditText) findViewById(R.id.username);
        message = (EditText) findViewById(R.id.message);
        send_btn = (Button) findViewById(R.id.send_btn);


        send_btn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                String str_addtes = address.getText().toString();
                String str_message = message.getText().toString();
                String str_username = message.getText().toString();


                if (str_addtes.length() > 0 && str_message.length() > 0 && str_username.length() >0) {

                    if(Function.sendSMS(str_addtes, str_message))
                    {
                        Toast.makeText(getApplicationContext(), "Message sent", Toast.LENGTH_SHORT).show();
                        finish();
                        //TODO JSON
                    }
                }
            }

        });
    }
}
