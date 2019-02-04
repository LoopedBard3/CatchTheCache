package edu.iastate.cs309.jr2.catchthecacheandroid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import edu.iastate.cs309.jr2.catchthecacheandroid.ui.main.MainFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, MainFragment.newInstance())
                    .commitNow();
        }
    }
}
