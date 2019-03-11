package edu.iastate.cs309.jr2.catchthecacheandroid;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;

import edu.iastate.cs309.jr2.catchthecacheandroid.models.cache_models.Cache;
import edu.iastate.cs309.jr2.catchthecacheandroid.models.user_models.User;

public class CacheViewActivity extends AppCompatActivity {
    private User usr;
    private Cache cache;
    private TextView desc;
    private Button btn;

    private Button btn2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cache_view);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle extras = getIntent().getExtras();
        usr = (User) extras.getSerializable("UserObject");
        cache = (Cache) extras.getSerializable("CacheObject");

        desc = findViewById(R.id.CacheDescription);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            desc.setTextAppearance(R.style.TextAppearance_AppCompat_Large);
        }
        if(TextUtils.isEmpty(cache.description)) desc.setText(getString(R.string.cache_description_testing, cache.getLatitude(), cache.getLongitude()));
        else desc.setText(cache.getDescription());

        btn = findViewById(R.id.start_caching_button);
        btn.setText(getString(R.string.btn_start_caching, cache.getName()));
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "I am currently a placeholder!!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        btn2 = findViewById(R.id.enter_chat_room);
        btn2.setText("View the chat room!");
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CacheChatRoom.class);
                intent.putExtra("UserObject", usr);
                intent.putExtra("CacheObject", cache);
                startActivity(intent);
            }
        });
        getSupportActionBar().setTitle(cache.name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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


}
