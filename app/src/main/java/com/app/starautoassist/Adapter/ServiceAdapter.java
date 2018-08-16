package com.app.starautoassist.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.starautoassist.Activity.Fuel_Activity;
import com.app.starautoassist.Activity.HomeActivity;
import com.app.starautoassist.Activity.JumpstartActivity;
import com.app.starautoassist.Activity.ProfileActivity;
import com.app.starautoassist.Activity.Towing_Activity;
import com.app.starautoassist.Activity.TyreActivity;
import com.app.starautoassist.Activity.VechicleActivity;
import com.app.starautoassist.Helper.GetSet;
import com.app.starautoassist.Others.Constants;
import com.app.starautoassist.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashMap;

import static com.app.starautoassist.Fragment.HomeFragment.mycarslist;
import static com.app.starautoassist.Others.Constants.isLocationpermission_enabled;

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<HashMap<String,String>> serviceList;

    public ServiceAdapter(Context mContext, ArrayList<HashMap<String,String>> servicelist) {
        this.mContext = mContext;
        this.serviceList = servicelist;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_home, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        final HashMap<String, String> itemMap=serviceList.get(position);
        // holder.title.setText(itemMap.get(Constants.servicename));
        Glide
                .with(mContext)
                .load(itemMap.get(Constants.serviceimg))
                .into(holder.image);

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = itemMap.get(Constants.servicename);
                final String chrg = itemMap.get(Constants.servicecharge);
                String mobileno=Constants.pref.getString("mobileno","");
                if (mobileno==null || mobileno.equalsIgnoreCase("")) {
                    Toast.makeText(mContext, "Please fill profile", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(mContext, ProfileActivity.class);
                    mContext.startActivity(intent);
                    ((Activity) mContext).finish();
                } else {
                    if (Constants.isLocationpermission_enabled) {
                        if (Constants.pref.getBoolean("isCarAdded", false)||mycarslist.size()>0) {
                            final Dialog dialog = new Dialog(mContext);
                            LayoutInflater li = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            View view = null;
                            if (li != null) {
                                view = li.inflate(R.layout.car_choice_list, null);
                            }
                            ListView lv = (ListView) view.findViewById(R.id.dialoglist);

                            // Change MyActivity.this and myListOfItems to your own values
                            Car_choice_adapter clad = new Car_choice_adapter(mContext, mycarslist);
                            lv.setAdapter(clad);
                            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    Toast.makeText(mContext, mycarslist.get(i).get("brand"), Toast.LENGTH_SHORT).show();
                                    String car_brand=mycarslist.get(i).get("brand");
                                    String car_model=mycarslist.get(i).get("model");
                                    String flatbed=mycarslist.get(i).get("flatbed");
                                    if (name.equalsIgnoreCase("towing")) {
                                        Intent intent = new Intent(mContext, Towing_Activity.class);
                                        intent.putExtra("service_chrg", chrg);
                                        intent.putExtra("brand", car_brand);
                                        intent.putExtra("model", car_model);
                                        intent.putExtra("flatbed", flatbed);
                                        mContext.startActivity(intent);
                                        ((Activity) mContext).finish();
                                        dialog.dismiss();
                                    } else if (name.equalsIgnoreCase("out of fuel")) {
                                        Intent intent = new Intent(mContext, Fuel_Activity.class);
                                        intent.putExtra("service_chrg", chrg);
                                        intent.putExtra("brand", car_brand);
                                        intent.putExtra("model", car_model);
                                        mContext.startActivity(intent);
                                        ((Activity) mContext).finish();
                                        dialog.dismiss();
                                    } else if (itemMap.get(Constants.servicename).equalsIgnoreCase("jump start")) {
                                        Intent intent = new Intent(mContext, JumpstartActivity.class);
                                        intent.putExtra("service_chrg", chrg);
                                        intent.putExtra("brand", car_brand);
                                        intent.putExtra("model", car_model);
                                        mContext.startActivity(intent);
                                        ((Activity) mContext).finish();
                                        dialog.dismiss();
                                    } else if (name.equalsIgnoreCase("flat tyre")) {
                                        Intent intent = new Intent(mContext, TyreActivity.class);
                                        intent.putExtra("service_chrg", chrg);
                                        intent.putExtra("brand", car_brand);
                                        intent.putExtra("model", car_model);
                                        mContext.startActivity(intent);
                                        ((Activity) mContext).finish();
                                        dialog.dismiss();
                                    } else
                                        Toast.makeText(mContext, "Your selection invalid", Toast.LENGTH_SHORT).show();
                                }

                            });
                            dialog.setContentView(view);
                            dialog.show();

                        } else {
                            Toast.makeText(mContext, "Please update car details", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(mContext, VechicleActivity.class);
                            mContext.startActivity(intent);
                            ((Activity) mContext).finish();
                        }
                    }
                       else {  Toast.makeText(mContext, "Location not enabled", Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(mContext,HomeActivity.class);
                        mContext.startActivity(intent);
                        ((Activity) mContext).finish();
                    }
                }
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
