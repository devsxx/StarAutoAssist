package com.app.starautoassist.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.starautoassist.Activity.ServiceDetailedHistoryActivity;
import com.app.starautoassist.R;

import java.util.ArrayList;
import java.util.HashMap;

public class ServiceHistoryAdapter extends RecyclerView.Adapter<ServiceHistoryAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<HashMap<String,String>> serviceHistoryList;

    public ServiceHistoryAdapter(Context mContext, ArrayList<HashMap<String, String>> serviceHistoryList) {
        this.mContext = mContext;
        this.serviceHistoryList = serviceHistoryList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.service_history_layout, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        final HashMap<String, String> itemMap=serviceHistoryList.get(position);

        holder.serviceid.setText(itemMap.get("serviceid"));
        holder.servicename.setText(itemMap.get("servicename"));
        holder.servicedate.setText(itemMap.get("servicedate"));
        holder.providername.setText(itemMap.get("providername"));

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(mContext, ServiceDetailedHistoryActivity.class);
                intent.putExtra("data", itemMap);

                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return serviceHistoryList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView serviceid, servicename, servicedate, providername;
        private CardView cardView;

        public MyViewHolder(View itemView) {
            super(itemView);

            serviceid = itemView.findViewById(R.id.tv_servicehistory_serviceid);
            servicename = itemView.findViewById(R.id.tv_servicehistory_servicename);
            servicedate = itemView.findViewById(R.id.tv_servicehistory_servicedate);
            providername = itemView.findViewById(R.id.tv_servicehistory_providername);
            cardView=itemView.findViewById(R.id.cv_service_history);
        }
    }
}
