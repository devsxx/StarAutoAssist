package com.app.starautoassist.Adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.starautoassist.Activity.HomeActivity;
import com.app.starautoassist.Helper.GetSet;
import com.app.starautoassist.Others.Constants;
import com.app.starautoassist.R;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.app.starautoassist.Activity.VechicleActivity.mycars;

public class MyCars_Adapter extends RecyclerView.Adapter<MyCars_Adapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<HashMap<String,String>> carList;
    int pos;

    public MyCars_Adapter(Context mContext, ArrayList<HashMap<String,String>> acceptedList) {
        this.mContext = mContext;
        this.carList = acceptedList;

    }

    @NonNull
    @Override
    public MyCars_Adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_mycars, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyCars_Adapter.MyViewHolder holder, final int position) {
        final HashMap<String, String> itemMap=carList.get(position);
         holder.brand.setText(itemMap.get(Constants.brand));
         holder.model.setText(itemMap.get(Constants.model));
         holder.plateno.setText(itemMap.get(Constants.plateno));
        Glide   .with(mContext)
                .load(itemMap.get(Constants.logo))
                .into(holder.brandlogo);
         holder.del.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 pos=position;
                 if(mycars.size()>1) {
                     new Delete_Mycar(mContext, itemMap.get(Constants.cno)).execute();
                 }else Toast.makeText(mContext,"Cannot delete all cars",Toast.LENGTH_SHORT).show();
             }
         });

    }

    @Override
    public int getItemCount() {

        return carList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView brand,model,plateno;
        public ImageView brandlogo;
        public LinearLayout del;

        public MyViewHolder(View itemView) {
            super(itemView);
            brand = itemView.findViewById(R.id.brand);
            model = itemView.findViewById(R.id.model);
            plateno = itemView.findViewById(R.id.pno);
            brandlogo = itemView.findViewById(R.id.brandlogo);
            del = itemView.findViewById(R.id.dellayout);
        }
    }

    public class Delete_Mycar extends AsyncTask<String, Integer, String> {
        Context context;
        String carid;
        private String url = Constants.BaseURL + Constants.delcar;

        private Delete_Mycar(Context ctx,String carid) {
            context = ctx;
            this.carid=carid;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Nullable
        @Override
        protected String doInBackground(String... params) {
            String jsonData = null;
            Response response = null;
            OkHttpClient client = new OkHttpClient();
            RequestBody body = new FormBody.Builder()
                    .add(Constants.cno, carid)
                    .build();
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();
            Call call = client.newCall(request);

            try {
                response = call.execute();

                if (response.isSuccessful()) {
                    jsonData = response.body().string();
                } else {
                    jsonData = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return jsonData;
        }

        @Override
        protected void onPostExecute(String jsonData) {
            super.onPostExecute(jsonData);
            Log.v("result", "" + jsonData);
            JSONObject jonj = null;
            try {
                jonj = new JSONObject(jsonData);
                if (jonj.getString("status").equalsIgnoreCase(
                        "success")) {
                    Toast.makeText(mContext, "Car delelted from your list successfully", Toast.LENGTH_SHORT).show();
                    mycars.remove(pos);
                    notifyDataSetChanged();
                } else
                    Toast.makeText(mContext, jonj.getString("message"), Toast.LENGTH_SHORT).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
