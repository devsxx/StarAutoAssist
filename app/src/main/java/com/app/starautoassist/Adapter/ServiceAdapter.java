package com.app.starautoassist.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.starautoassist.Data.Service;
import com.app.starautoassist.R;
import com.bumptech.glide.Glide;

import java.util.List;

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.MyViewHolder> {

    private Context mContext;
    private List<Service> serviceList;

    public ServiceAdapter(Context mContext, List<Service> serviceList) {

        this.mContext = mContext;
        this.serviceList = serviceList;
    }

    @NonNull
    @Override
    public ServiceAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_home, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ServiceAdapter.MyViewHolder holder, final int position) {

        final Service service = serviceList.get(position);

        holder.title.setText(service.getServicename());
        Glide.with(mContext).load(service.getServiceimage()).into(holder.image);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               Toast.makeText(mContext, "You have selected\t :\t" + service.getServicename() ,Toast.LENGTH_SHORT).show();
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
        public CardView cardView;

        public MyViewHolder(View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.servicetitle);
            image = itemView.findViewById(R.id.serviceimage);
            cardView = itemView.findViewById(R.id.cv_service);

        }
    }
}
