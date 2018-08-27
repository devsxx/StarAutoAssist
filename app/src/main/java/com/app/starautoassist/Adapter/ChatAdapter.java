package com.app.starautoassist.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.starautoassist.Data.Message;
import com.app.starautoassist.R;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {


    private String userid;
    private Context context;
    private int SELF = 143;
    private int OTHER = 144;
    private ArrayList<Message> messages;


    public ChatAdapter(String userid, Context context, ArrayList<Message> messages) {
        this.userid = userid;
        this.context = context;
        this.messages = messages;
    }

    @Override
    public int getItemViewType(int position) {

        Message message = messages.get(position);

        if (message.getSid().equalsIgnoreCase( userid)){
            return SELF;
        }else  return OTHER;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView;

        if (viewType == SELF) {

            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.chat_ours_layout, parent, false);
        } else {

            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.chat_others_layout, parent, false);
        }

        return new ViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Message message = messages.get(position);
        holder.textViewMessage.setText(message.getSmessage());
        holder.textViewTime.setText(message.getStime());

    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView textViewMessage, textViewTime;

        public ViewHolder(View itemView) {
            super(itemView);

            textViewMessage = itemView.findViewById(R.id.textViewMessage);
            textViewTime = itemView.findViewById(R.id.textViewTime);

        }
    }
}
