package edu.iastate.cs309.jr2.catchthecacheandroid.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import edu.iastate.cs309.jr2.catchthecacheandroid.R;
import edu.iastate.cs309.jr2.catchthecacheandroid.models.cache_models.Cache;
import edu.iastate.cs309.jr2.catchthecacheandroid.models.chat_models.Message;
import edu.iastate.cs309.jr2.catchthecacheandroid.models.user_models.User;

public class CacheChatAdapter extends RecyclerView.Adapter<CacheChatAdapter.CacheChatViewHolder> {
    private ArrayList<Message> mChats;

    private User user;


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class CacheChatViewHolder extends RecyclerView.ViewHolder {
        public TextView msg_creator, msg;
        public CacheChatViewHolder(View v) {
            super(v);
            msg_creator = v.findViewById(R.id.msg_creator);
            msg = v.findViewById(R.id.msg);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public CacheChatAdapter(ArrayList<Message> myDataset, User usr) {
        mChats = myDataset;
        user = usr;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public CacheChatViewHolder onCreateViewHolder(final ViewGroup parent,
                                                  int viewType) {
        // create a new view
        View v = (View) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cache_chat_view_list, parent, false);
        final CacheChatViewHolder vh = new CacheChatViewHolder(v);
        //TODO make a cache_chat_view_list
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull CacheChatViewHolder cacheChatViewHolder, int i) {
        Message msg = mChats.get(i);
        TextView name = cacheChatViewHolder.msg_creator;
        name.setText((CharSequence) msg.getSender());//TODO help i might have broken this?

        TextView text = cacheChatViewHolder.msg;
        text.setText(msg.getText());
    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mChats.size();
    }
}