package com.app.starautoassist.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.starautoassist.Others.Constants;
import com.app.starautoassist.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashMap;

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<HashMap<String,String>> serviceList;

    public ServiceAdapter(Context mContext, ArrayList<HashMap<String,String>> servicelist) {

        this.mContext = mContext;
        this.serviceList = servicelist;
    }

    @NonNull
    @Override
    public ServiceAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_home, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ServiceAdapter.MyViewHolder holder, final int position) {
        final HashMap<String, String> itemMap=serviceList.get(position);
         holder.title.setText(itemMap.get(Constants.servicename));
        Glide   .with(mContext)
                .load(itemMap.get(Constants.serviceimg))
                .into(holder.image);

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               Toast.makeText(mContext, "You have selected\t :\t" + "service" ,Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {

        return serviceList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView title;
        public ImageView image;
        public LinearLayout linearLayout;

        public MyViewHolder(View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.servicetitle);
            image = itemView.findViewById(R.id.serviceimage);
            linearLayout = itemView.findViewById(R.id.lv_service);

        }
    }
}
