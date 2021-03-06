package com.app.starautoassist.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.starautoassist.Activity.Completed_Services_Payment_Activity;
import com.app.starautoassist.Others.Constants;
import com.app.starautoassist.R;

import java.util.ArrayList;
import java.util.HashMap;

public class Completed_Service_Adapter extends RecyclerView.Adapter<Completed_Service_Adapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<HashMap<String,String>> completed_list;

    public Completed_Service_Adapter(Context mContext, ArrayList<HashMap<String, String>> completed_list) {
        this.mContext = mContext;
        this.completed_list = completed_list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_completed_service, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        final HashMap<String, String> itemMap = completed_list.get(position);

        holder.sid.setText(itemMap.get("service_id"));
        holder.sname.setText(itemMap.get(Constants.service_name));
        holder.samount.setText(itemMap.get("totalamount"));
        if(itemMap.get("status").equals("0")) {
            holder.status.setText("Pay & close service");
            holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(mContext, Completed_Services_Payment_Activity.class);
                    intent.putExtra("data", itemMap);
                    mContext.startActivity(intent);
                }
            });
        }
        else
            holder.status.setText("Payment Done");


    }

    @Override
    public int getItemCount() {
        return completed_list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView sid, sname,status, samount;
        private LinearLayout  linearLayout;

        public MyViewHolder(View itemView) {
            super(itemView);

            sid = itemView.findViewById(R.id.serviceidval);
            sname = itemView.findViewById(R.id.servicenameval);
            samount = itemView.findViewById(R.id.serviceamt);
            status = itemView.findViewById(R.id.statusval);
            linearLayout = itemView.findViewById(R.id.completed_layout);
        }
    }
}
