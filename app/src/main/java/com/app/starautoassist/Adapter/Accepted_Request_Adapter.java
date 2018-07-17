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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.starautoassist.Activity.Communication;
import com.app.starautoassist.Activity.HomeActivity;
import com.app.starautoassist.Helper.GetSet;
import com.app.starautoassist.Others.Constants;
import com.app.starautoassist.R;

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

public class Accepted_Request_Adapter extends RecyclerView.Adapter<Accepted_Request_Adapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<HashMap<String,String>> acceptedList;
    String type="";
    public Accepted_Request_Adapter(Context mContext, ArrayList<HashMap<String,String>> acceptedList) {
        this.mContext = mContext;
        this.acceptedList = acceptedList;
    }

    @NonNull
    @Override
    public Accepted_Request_Adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_accepted_request, parent, false);
        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull final Accepted_Request_Adapter.MyViewHolder holder, final int position) {
        final HashMap<String, String> itemMap = acceptedList.get(position);
        holder.spname.setText(itemMap.get(Constants.companyname));
        holder.serviceid.setText(itemMap.get(Constants.serviceid));
        holder.servicetype.setText(itemMap.get(Constants.service_name));
        holder.address.setText(itemMap.get(Constants.address));
        holder.mobileno.setText(itemMap.get(Constants.serviceprovider_id));
        if (itemMap.get(Constants.status).equalsIgnoreCase("3")) {
            holder.decline.setVisibility(View.GONE);
            holder.accept.setVisibility(View.GONE);


        } else {
            holder.decline.setVisibility(View.VISIBLE);
            holder.accept.setVisibility(View.VISIBLE);
            holder.communication.setOnClickListener(null);
        }
        holder.communication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(mContext,Communication.class);
                intent.putExtra("map",itemMap);
                mContext.startActivity(intent);
            }
        });
        holder.decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = "0";
                new Send_request_to_sp(mContext, itemMap.get(Constants.serviceid), type, itemMap.get(Constants.serviceprovider_id)).execute();
            }
        });
        holder.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = "1";
                new Send_request_to_sp(mContext, itemMap.get(Constants.serviceid), type, itemMap.get(Constants.serviceprovider_id)).execute();
            }
        });
    }

    @Override
    public int getItemCount() {

        return acceptedList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView spname,servicetype,address,serviceid,mobileno,accept,decline;
        public LinearLayout communication;
        public MyViewHolder(View itemView) {
            super(itemView);
            spname = itemView.findViewById(R.id.sname);
            servicetype = itemView.findViewById(R.id.servicetypeval);
            address = itemView.findViewById(R.id.address);
            serviceid = itemView.findViewById(R.id.sidvalue);
            mobileno = itemView.findViewById(R.id.contactno);
            accept = itemView.findViewById(R.id.accept_req_btn);
            decline = itemView.findViewById(R.id.decline_req_btn);
            communication = itemView.findViewById(R.id.communicationLayout);
        }
    }

    public class Send_request_to_sp extends AsyncTask<String, Integer, String> {
        private Context context;
        private String url = Constants.BaseURL + Constants.send_response_to_sp;
        ProgressDialog progress;
        @Nullable
        String service_id="";
        String type,sp_id;
        HashMap<String, String> map;
        public Send_request_to_sp(Context ctx, String service_id, String type,String sp_id) {
            context = ctx;
            this.service_id=service_id;
            this.type=type;
            this.sp_id=sp_id;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = new ProgressDialog(context);
            progress.setMessage("Please wait ....");
            progress.setTitle("Loading");
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.show();
        }

        @Nullable
        @Override
        protected String doInBackground(String... params) {
            String jsonData = null;
            Response response = null;
            OkHttpClient client = new OkHttpClient();
            RequestBody body = new FormBody.Builder()
                    .add(Constants.mobileno, GetSet.getMobileno())
                    .add(Constants.serviceprovider_id, sp_id)
                    .add(Constants.serviceid, service_id)
                    .add(Constants.type, type)
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
            progress.dismiss();
            String sname,time,sid,status,sprice;
            Log.v("result", "" + jsonData);
            JSONObject jonj = null;
            try {
                jonj = new JSONObject(jsonData);
                if (jonj.getString("status").equalsIgnoreCase(
                        "success")) {
                   Toast.makeText(context,jonj.getString("message"),Toast.LENGTH_SHORT).show();
                   Intent intent=new Intent(context, HomeActivity.class);
                   context.startActivity(intent);
                    ((Activity)context).finish();
                }
            }catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
