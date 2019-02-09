package edu.iastate.cs309.jr2.catchthecacheandroid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.TextView;

import edu.iastate.cs309.jr2.catchthecacheandroid.dummy.DummyContent;
import edu.iastate.cs309.jr2.catchthecacheandroid.ui.main.MainFragment;

public class MainActivity extends AppCompatActivity implements chatFragment.OnListFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, MainFragment.newInstance())
                    .commitNow();
        }
        //super.onCreate(savedInstanceState);
        //setContentView(R.layout.main_fragment);
        configureBackButton();
        //configureRecyleButton();
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
   /* public void configureRecyleButton(){
        Button recyclebutton = (Button) findViewById(R.id.recbtn);
        recyclebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, MychatRecyclerViewAdapter.class));
            }
        });
    }
    */
}
