package edu.iastate.cs309.jr2.catchthecacheandroid.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import edu.iastate.cs309.jr2.catchthecacheandroid.R;
import edu.iastate.cs309.jr2.catchthecacheandroid.models.cache_models.CacheIndividual;

public class CacheListAdapter extends RecyclerView.Adapter<CacheListAdapter.CacheListViewHolder> {
    private ArrayList<CacheIndividual> mCaches;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class CacheListViewHolder extends RecyclerView.ViewHolder {
        public TextView cache_name, cache_location;
        public CacheListViewHolder(View v) {
            super(v);
            cache_name = v.findViewById(R.id.cache_name);
            cache_location = v.findViewById(R.id.cache_location);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public CacheListAdapter(ArrayList<CacheIndividual> myDataset) {
        mCaches = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public CacheListViewHolder onCreateViewHolder(ViewGroup parent,
                                                  int viewType) {
        // create a new view
        View v = (View) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cache_view_list, parent, false);
        CacheListViewHolder vh = new CacheListViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull CacheListViewHolder cacheListViewHolder, int i) {
        CacheIndividual cache = mCaches.get(i);
        TextView name = cacheListViewHolder.cache_name;
        name.setText(cache.getName());

        TextView location = cacheListViewHolder.cache_location;
        location.setText("Lat: " + cache.getLatitude() + "  Long: " + cache.getLongitude());
    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mCaches.size();
    }
}