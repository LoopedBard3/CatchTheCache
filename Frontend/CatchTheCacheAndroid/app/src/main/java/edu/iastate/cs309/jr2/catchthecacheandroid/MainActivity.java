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

/**
 * main class for sending chats
 */
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
    LoadSms loadsmsTask;
    InboxAdapter adapter, tmpadapter;
    ListView listView;
    FloatingActionButton fab_new;
    ProgressBar loader;
    int i;


    /**
     * method called when first starting this class
     * @param savedInstanceState
     */
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
                startActivity(new Intent(MainActivity.this, NewSmsActivity.class));
            }
        });

             configureBackButton();
             //TODO create back button

    }

    /**
     * method to initialize values within the chatting classes
     */
    public void init() {
        smsList.clear();
        try {
            tmpList = (ArrayList<HashMap<String, String>>) edu.iastate.cs309.jr2.catchthecacheandroid.Function.readCachedFile(MainActivity.this, "smsapp");
            tmpadapter = new InboxAdapter(MainActivity.this, tmpList);
            listView.setAdapter(tmpadapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view,
                                        final int position, long id) {
                    loadsmsTask.cancel(true);
                    Intent intent = new Intent(MainActivity.this, Chat.class);
                    intent.putExtra("name", tmpList.get(+position).get(edu.iastate.cs309.jr2.catchthecacheandroid.Function.KEY_NAME));
                    intent.putExtra("address", tmpList.get(+position).get(edu.iastate.cs309.jr2.catchthecacheandroid.Function.KEY_PHONE));
                    intent.putExtra("thread_id", tmpList.get(+position).get(edu.iastate.cs309.jr2.catchthecacheandroid.Function.KEY_THREAD_ID));
                    startActivity(intent);
                }
            });
        } catch (Exception e) {
        }
    }


    /**
     * Mini class used in sending chats through sms
      */
    class LoadSms extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            smsList.clear();
        }

        /**
         * a good ole do in the background method, we are going to be deleting these classes
         * @param args
         * @return
         */
        protected String doInBackground(String... args) {
            String xml = "";


            try {
                Uri uriInbox = Uri.parse("content://sms/inbox");

                Cursor inbox = getContentResolver().query(uriInbox, null, "address IS NOT NULL) GROUP BY (thread_id", null, null); // 2nd null = "address IS NOT NULL) GROUP BY (address"
                Uri uriSent = Uri.parse("content://sms/sent");
                Cursor sent = getContentResolver().query(uriSent, null, "address IS NOT NULL) GROUP BY (thread_id", null, null); // 2nd null = "address IS NOT NULL) GROUP BY (address"
                Cursor c = new MergeCursor(new Cursor[]{inbox, sent}); // Attaching inbox and sent sms


                if (c.moveToFirst()) {
                    for (int i = 0; i < c.getCount(); i++) {
                        String name = "name";
                        String phone = "";
                        String _id = c.getString(c.getColumnIndexOrThrow("_id"));
                        String thread_id = c.getString(c.getColumnIndexOrThrow("thread_id"));
                        String msg = c.getString(c.getColumnIndexOrThrow("body"));
                        String type = c.getString(c.getColumnIndexOrThrow("type"));
                        String timestamp = c.getString(c.getColumnIndexOrThrow("date"));
                        phone = c.getString(c.getColumnIndexOrThrow("address"));


                        name = CacheUtils.readFile(thread_id);
                        if (name == null) {
                            name = edu.iastate.cs309.jr2.catchthecacheandroid.Function.getContactbyPhoneNumber(getApplicationContext(), c.getString(c.getColumnIndexOrThrow("address")));
                            CacheUtils.writeFile(thread_id, name);
                        }


                        smsList.add(edu.iastate.cs309.jr2.catchthecacheandroid.Function.mappingInbox(_id, thread_id, name, phone, msg, type, timestamp, edu.iastate.cs309.jr2.catchthecacheandroid.Function.converToTime(timestamp)));
                        c.moveToNext();
                    }
                }
                c.close();

            } catch (IllegalArgumentException e) {
                // TODO Auto-generated catch block
                return "Unable to retrieve web page. URL may be invalid.";
            }

            Collections.sort(smsList, new MapComparator(edu.iastate.cs309.jr2.catchthecacheandroid.Function.KEY_TIMESTAMP, "dsc")); // Arranging sms by timestamp decending
            ArrayList<HashMap<String, String>> purified = edu.iastate.cs309.jr2.catchthecacheandroid.Function.removeDuplicates(smsList); // Removing duplicates from inbox & sent
            smsList.clear();
            smsList.addAll(purified);

            // Updating cache data
            try {
                edu.iastate.cs309.jr2.catchthecacheandroid.Function.createCachedFile(MainActivity.this, "smsapp", smsList);
            } catch (Exception e) {
            }
            // Updating cache data

            return xml;
        }

        @Override
        protected void onPostExecute(String xml) {

            if (!tmpList.equals(smsList)) {
                adapter = new InboxAdapter(MainActivity.this, smsList);
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View view,
                                            final int position, long id) {
                        Intent intent = new Intent(MainActivity.this, Chat.class);
                        intent.putExtra("name", smsList.get(+position).get(edu.iastate.cs309.jr2.catchthecacheandroid.Function.KEY_NAME));
                        intent.putExtra("address", tmpList.get(+position).get(edu.iastate.cs309.jr2.catchthecacheandroid.Function.KEY_PHONE));
                        intent.putExtra("thread_id", smsList.get(+position).get(edu.iastate.cs309.jr2.catchthecacheandroid.Function.KEY_THREAD_ID));
                        startActivity(intent);
                    }
                });
            }
        }
    }

    /**
     * when sending a new chat for the first time permissions will be requested first
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_PERMISSION_KEY: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    init();
                    loadsmsTask = new LoadSms();
                    loadsmsTask.execute();
                } else {
                    Toast.makeText(MainActivity.this, "You must accept permissions.", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    /**
     * method to check permissions on start up
     */
    @Override
    protected void onResume() {
        super.onResume();

        String[] PERMISSIONS = {Manifest.permission.READ_SMS, Manifest.permission.SEND_SMS, Manifest.permission.RECEIVE_SMS,
                Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS};
        if (!edu.iastate.cs309.jr2.catchthecacheandroid.Function.hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, REQUEST_PERMISSION_KEY);
        } else {

            init();
            loadsmsTask = new LoadSms();
            loadsmsTask.execute();
        }
    }

    /**
     * method called to init
     */
    @Override
    public void onStart() {
        super.onStart();
    }

    /**
     * adapter class to format the inbox
     */
    class InboxAdapter extends BaseAdapter {
        private Activity activity;
        private ArrayList<HashMap< String, String >> data;
        public InboxAdapter(Activity a, ArrayList < HashMap < String, String >> d) {
            activity = a;
            data = d;
        }



        public int getCount() {
            return data.size();
        }
        public Object getItem(int position) {
            return position;
        }
        public long getItemId(int position) {
            return position;
        }

        /**
         * method used to organize inbox
         * @param position
         * @param convertView
         * @param parent
         * @return nicely formatted inbox
         */
        public View getView(int position, View convertView, ViewGroup parent) {
            InboxViewHolder holder = null;
            if (convertView == null) {
                holder = new InboxViewHolder();
                convertView = LayoutInflater.from(activity).inflate(
                        R.layout.conversation_list_item, parent, false);

                holder.inbox_thumb = (ImageView) convertView.findViewById(R.id.inbox_thumb);
                holder.inbox_user = (TextView) convertView.findViewById(R.id.inbox_user);
                holder.inbox_msg = (TextView) convertView.findViewById(R.id.inbox_msg);
                holder.inbox_date = (TextView) convertView.findViewById(R.id.inbox_date);

                convertView.setTag(holder);
            } else {
                holder = (InboxViewHolder) convertView.getTag();
            }
            holder.inbox_thumb.setId(position);
            holder.inbox_user.setId(position);
            holder.inbox_msg.setId(position);
            holder.inbox_date.setId(position);

            HashMap < String, String > song = new HashMap < String, String > ();
            song = data.get(position);
            try {
                holder.inbox_user.setText(song.get(edu.iastate.cs309.jr2.catchthecacheandroid.Function.KEY_NAME));
                holder.inbox_msg.setText(song.get(edu.iastate.cs309.jr2.catchthecacheandroid.Function.KEY_MSG));
                holder.inbox_date.setText(song.get(edu.iastate.cs309.jr2.catchthecacheandroid.Function.KEY_TIME));

                String firstLetter = String.valueOf(song.get(edu.iastate.cs309.jr2.catchthecacheandroid.Function.KEY_NAME).charAt(0));
                ColorGenerator generator = ColorGenerator.MATERIAL;
                int color = generator.getColor(getItem(position));
                TextDrawable drawable = TextDrawable.builder()
                        .buildRound(firstLetter, color);
                holder.inbox_thumb.setImageDrawable(drawable);
            } catch (Exception e) {}
            return convertView;
        }
    }

    /**
     * mini inbox organizing class
     */
    class InboxViewHolder {
        ImageView inbox_thumb;
        TextView inbox_user, inbox_msg, inbox_date;
    }

    private void local_finish(){
        finish();
    }

    /**
     * method used to configure button back to login, used for testing
     */
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
