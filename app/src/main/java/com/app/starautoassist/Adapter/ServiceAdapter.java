package com.app.starautoassist.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.starautoassist.Activity.Fuel_Activity;
import com.app.starautoassist.Activity.JumpstartActivity;
import com.app.starautoassist.Activity.Towing_Activity;
import com.app.starautoassist.Activity.TyreActivity;
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
                String name=itemMap.get(Constants.servicename);
                String chrg=itemMap.get(Constants.servicecharge);
                if(name.equalsIgnoreCase("towing")){
                    Intent intent=new Intent(mContext, Towing_Activity.class);
                    intent.putExtra("service_chrg",chrg);
                    mContext.startActivity(intent);
                    ((Activity)mContext).finish();
                }else  if(name.equalsIgnoreCase("out of fuel")){
                    Intent intent=new Intent(mContext, Fuel_Activity.class);
                    intent.putExtra("service_chrg",chrg);
                    mContext.startActivity(intent);
                    ((Activity)mContext).finish();
                }else  if(itemMap.get(Constants.servicename).equalsIgnoreCase("jump start")){
                    Intent intent=new Intent(mContext, JumpstartActivity.class);
                    intent.putExtra("service_chrg",chrg);
                    mContext.startActivity(intent);
                    ((Activity)mContext).finish();
                }else  if(name.equalsIgnoreCase("flat tyre")){
                    Intent intent=new Intent(mContext, TyreActivity.class);
                    intent.putExtra("service_chrg",chrg);
                    mContext.startActivity(intent);
                    ((Activity)mContext).finish();
                }else  Toast.makeText(mContext, "Your selection invalid" ,Toast.LENGTH_SHORT).show();
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
