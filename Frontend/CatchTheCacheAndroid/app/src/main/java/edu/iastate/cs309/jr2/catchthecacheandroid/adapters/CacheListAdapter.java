package edu.iastate.cs309.jr2.catchthecacheandroid.adapters;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import edu.iastate.cs309.jr2.catchthecacheandroid.CacheViewActivity;
import edu.iastate.cs309.jr2.catchthecacheandroid.R;
import edu.iastate.cs309.jr2.catchthecacheandroid.models.cache_models.Cache;
import edu.iastate.cs309.jr2.catchthecacheandroid.models.user_models.User;

/**
 * The adapter for listing the list of caches
 */
public class CacheListAdapter extends RecyclerView.Adapter<CacheListAdapter.CacheListViewHolder> {
    private ArrayList<Cache> mCaches;
    private User user;

    /**
     * Class for creating the RecyclerView ViewHolder
     * @author Parker Bibus
     */
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

    /**
     * Constructor for the adapter. Loads in the list for the viewer.
     * @author Parker Bibus
     * @param myDataset ArrayList of type Cache that contains the caches to display
     * @param usr the current user
     */
    // Provide a suitable constructor (depends on the kind of dataset)
    public CacheListAdapter(ArrayList<Cache> myDataset, User usr) {
        mCaches = myDataset;
        user = usr;
    }

    /**
     * Creator for the individual cache views. Inflates the input according
     * to the cache view list layout.
     * @authro Parker Bibus
     * @param parent
     * @param viewType
     * @return the created CacheViewHolder
     */
    @Override
    public CacheListViewHolder onCreateViewHolder(final ViewGroup parent,
                                                  int viewType) {
        // create a new view
        View v = (View) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cache_view_list, parent, false);
        final CacheListViewHolder vh = new CacheListViewHolder(v);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("CLICKED", String.valueOf(vh.getAdapterPosition()));
                Intent intent = new Intent(parent.getContext(), CacheViewActivity.class);
                intent.putExtra("CacheObject", mCaches.get(vh.getAdapterPosition()));
                intent.putExtra("UserObject", user);
                parent.getContext().startActivity(intent);
            }
        });
        return vh;
    }

    /**
     * Method that binds the information into the View Holder.
     * @author Parker Bibus
     * @param CacheListViewHolder the View Holder that will be populated
     * @param i the message's array location to access the message data
     */
    @Override
    public void onBindViewHolder(@NonNull CacheListViewHolder CacheListViewHolder, int i) {
        Cache cache = mCaches.get(i);
        TextView name = CacheListViewHolder.cache_name;
        name.setText(cache.getName());

        TextView location = CacheListViewHolder.cache_location;
        String lat, lon;
        int latStop, lonStop;
        lat = Double.toString(cache.getLatitude());
        lon = Double.toString(cache.getLongitude());
        latStop = lat.indexOf(".") + 5;
        lonStop = lat.indexOf(".") + 5;
        if(latStop > lat.length()) latStop = lat.length();
        if(lonStop > lon.length()) lonStop = lon.length();
        location.setText(String.format("Lat: %s  Long: %s", lat.substring(0, latStop), lon.substring(0, lonStop)));
    }


    // Return the size of your dataset (invoked by the layout manager)
    /**
     * Gets the number of items in apart of the cache array.
     * @return the size of the array
     */
    @Override
    public int getItemCount() {
        return mCaches.size();
    }
}