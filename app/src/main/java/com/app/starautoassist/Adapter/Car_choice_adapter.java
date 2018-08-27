package com.app.starautoassist.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.starautoassist.Others.Constants;
import com.app.starautoassist.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashMap;

public class Car_choice_adapter extends BaseAdapter {

    Context context;
    private LayoutInflater layoutInflater;
    ArrayList<HashMap<String,String>> carlist;
    public Car_choice_adapter(Context context, ArrayList<HashMap<String,String>> carlist) {
        this.context=context;
        this.carlist=carlist;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return carlist.size();
    }

    @Override
    public Object getItem(int i) {
        return carlist.get(i);
    }

    @Override
    public long getItemId(int i) {
        return (i);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder holder;
        HashMap<String,String> map=carlist.get(position);
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.adapter_car_choice, null);
            holder = new ViewHolder();
            holder.brand = convertView.findViewById(R.id.brand);
            holder.model = convertView.findViewById(R.id.model);
            holder.plateno = convertView.findViewById(R.id.pno);
            holder.brandlogo = convertView.findViewById(R.id.brandlogo);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.brand.setText(map.get(Constants.brand));
        holder.model.setText(map.get(Constants.model));
        holder.plateno.setText(map.get(Constants.plateno));
        Glide.with(context)
                .load(map.get(Constants.logo))
                .into(holder.brandlogo);

        return convertView;
    }

    static class ViewHolder {
        public TextView brand,model,plateno;
        public ImageView brandlogo;

    }

}
