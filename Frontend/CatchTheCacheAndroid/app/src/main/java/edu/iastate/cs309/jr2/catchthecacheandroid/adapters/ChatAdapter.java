package edu.iastate.cs309.jr2.catchthecacheandroid.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import edu.iastate.cs309.jr2.catchthecacheandroid.R;
import edu.iastate.cs309.jr2.catchthecacheandroid.models.chat_models.Message;
import edu.iastate.cs309.jr2.catchthecacheandroid.models.user_models.User;

public class ChatAdapter  extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {
    private ArrayList<Message> mChats;
    private User user;


    public static class ChatViewHolder extends RecyclerView.ViewHolder {
        public TextView msg_creator, msg;
        public ChatViewHolder(View v) {
            super(v);
            msg_creator = v.findViewById(R.id.msg_creator);
            msg = v.findViewById(R.id.msg);
        }
    }

    public ChatAdapter(ArrayList<Message> myDataset, User usr) {
        mChats = myDataset;
        user = usr;
    }

    @Override
    public ChatAdapter.ChatViewHolder onCreateViewHolder(final ViewGroup parent,
                                                                   int viewType) {
        // create a new view
        View v = (View) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chat_view_list, parent, false);
        final ChatAdapter.ChatViewHolder vh = new ChatAdapter.ChatViewHolder(v);
        return vh;

    }

    @Override
    public void onBindViewHolder(@NonNull ChatAdapter.ChatViewHolder chatViewHolder, int i) {
        Message msg = mChats.get(i);
        TextView name = chatViewHolder.msg_creator;
        name.setText(msg.getSender());

        TextView text = chatViewHolder.msg;
        text.setText(msg.getText());
    }


    @Override
    public int getItemCount() {
        return mChats.size();
    }



}
