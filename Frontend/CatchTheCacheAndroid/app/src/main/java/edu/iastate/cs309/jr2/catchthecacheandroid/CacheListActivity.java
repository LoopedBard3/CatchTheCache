package edu.iastate.cs309.jr2.catchthecacheandroid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import edu.iastate.cs309.jr2.catchthecacheandroid.adapters.CacheListAdapter;
import edu.iastate.cs309.jr2.catchthecacheandroid.models.cache_models.CacheIndividual;

public class CacheListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<CacheIndividual> caches;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cache_list);
        recyclerView = (RecyclerView) findViewById(R.id.rvCacheList);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // specify an adapter (see also next example)
        mAdapter = new CacheListAdapter(caches);
        recyclerView.setAdapter(mAdapter);
    }
}
