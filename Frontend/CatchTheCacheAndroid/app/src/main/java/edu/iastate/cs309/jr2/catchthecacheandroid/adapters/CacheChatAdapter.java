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

/**
 * Adapter for the cache chat messages Recycler Viewer
 * @author Parker Bibus
 */
public class CacheChatAdapter extends RecyclerView.Adapter<CacheChatAdapter.CacheChatViewHolder> {
    private ArrayList<Message> mChats;

    private User user;


    /**
     * Class for the RecyclerView ViewHolder
     * @author Parker Bibus
     */
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

    /**
     * Constructor for the adapter. Loads in the list for the viewer.
     * @author Parker Bibus
     * @param myDataset ArrayList of type Message that contains the messages to display
     * @param usr the current user
     */
    // Provide a suitable constructor (depends on the kind of dataset)
    public CacheChatAdapter(ArrayList<Message> myDataset, User usr) {
        mChats = myDataset;
        user = usr;
    }

    /**
     * Creator for the individual messages. Inflates the input according
     * to the cache chat view list layout.
     * @authro Parker Bibus
     * @param parent
     * @param viewType
     * @return the created CacheChatViewHolder
     */
    // Create new views (invoked by the layout manager)
    @Override
    public CacheChatViewHolder onCreateViewHolder(final ViewGroup parent,
                                                  int viewType) {
        // create a new view
        View v = (View) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cache_chat_view_list, parent, false);
        final CacheChatViewHolder vh = new CacheChatViewHolder(v);
        return vh;
    }

    /**
     * Method that binds the information into the View Holder.
     * @author Parker Bibus
     * @param cacheChatViewHolder the View Holder that will be populated
     * @param i the message's array location to access the message data
     */
    @Override
    public void onBindViewHolder(@NonNull CacheChatViewHolder cacheChatViewHolder, int i) {
        Message msg = mChats.get(i);
        TextView name = cacheChatViewHolder.msg_creator;
        name.setText(msg.getSender());

        TextView text = cacheChatViewHolder.msg;
        text.setText(msg.getText());
    }


    /**
     * Gets the number of items in apart of the chat array.
     * @return the size of the array
     */
    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mChats.size();
    }
}