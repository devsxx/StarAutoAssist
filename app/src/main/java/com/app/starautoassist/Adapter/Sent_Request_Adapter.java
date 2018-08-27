package com.app.starautoassist.Adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.app.starautoassist.Activity.AcceptedRequestActivity;
import com.app.starautoassist.Activity.HomeActivity;
import com.app.starautoassist.Helper.GetSet;
import com.app.starautoassist.Helper.TimeAgo;
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

public class Sent_Request_Adapter extends RecyclerView.Adapter<Sent_Request_Adapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<HashMap<String,String>> serviceList;

    public Sent_Request_Adapter(Context mContext, ArrayList<HashMap<String,String>> servicelist) {

        this.mContext = mContext;
        this.serviceList = servicelist;
    }

    @NonNull
    @Override
    public Sent_Request_Adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_sent_request, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final Sent_Request_Adapter.MyViewHolder holder, final int position) {
        final HashMap<String, String> itemMap=serviceList.get(position);
         holder.servicename.setText(itemMap.get(Constants.service_name));
        long timestamp = 0;
        String time = itemMap.get(Constants.timestamp);
        if(time != null){
            timestamp = Long.parseLong(time) * 1000;
        }
        TimeAgo timeAgo = new TimeAgo(mContext);
        holder.postedtime.setText(timeAgo.timeAgo(timestamp));
        if(itemMap.get("status").equalsIgnoreCase("3")){
            holder.cancelbtn.setVisibility(View.VISIBLE);
            holder.cancelbtn.setText(R.string.track_now);
            holder.cancelbtn.setTextColor(Color.parseColor("#ffffff"));
            holder.cancelbtn.setBackgroundColor(Color.parseColor("#f97900"));
        }else if(itemMap.get("status").equalsIgnoreCase("1")){
            holder.cancelbtn.setVisibility(View.VISIBLE);
            holder.cancelbtn.setText(R.string.send_response);
            holder.cancelbtn.setTextColor(Color.parseColor("#ffffff"));
            holder.cancelbtn.setBackgroundColor(Color.parseColor("#FFFF4F3B"));
        }else
            holder.cancelbtn.setVisibility(View.VISIBLE);
            if(itemMap.get(Constants.status).equalsIgnoreCase("0")) {
            holder.status.setText("Request Pending");
            holder.status.setTextColor(Color.parseColor("#ffffff"));
            holder.status.setBackgroundColor(Color.parseColor("#FFFF4F3B"));
        }else if(itemMap.get(Constants.status).equalsIgnoreCase("1")){
            holder.status.setText("Service Provider Accepted");
            holder.status.setTextColor(Color.parseColor("#ffffff"));
            holder.status.setBackgroundColor(Color.parseColor("#FF80B46D"));
        }else if(itemMap.get(Constants.status).equalsIgnoreCase("3")){
            holder.status.setText("Start Tracking");
            holder.status.setTextColor(Color.parseColor("#ffffff"));
            holder.status.setBackgroundColor(Color.parseColor("#FF80B46D"));
        }
        Log.v("time", "time="+timeAgo.timeAgo(timestamp));

        holder.cancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(itemMap.get(Constants.status).equalsIgnoreCase("3")||itemMap.get(Constants.status).equalsIgnoreCase("1")) {
                Intent intent=new Intent(mContext,AcceptedRequestActivity.class);
                mContext.startActivity(intent);
                ((Activity)mContext).finish();
                }else{
                    new Cancel_req(mContext, itemMap.get(Constants.serviceid)).execute();
                }
            }
        });

    }

    @Override
    public int getItemCount() {

        return serviceList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView servicename,postedtime,cancelbtn,status;

        public MyViewHolder(View itemView) {
            super(itemView);
            servicename = itemView.findViewById(R.id.sname);
            postedtime = itemView.findViewById(R.id.postedtimeval);
            status = itemView.findViewById(R.id.statusval);
            cancelbtn = itemView.findViewById(R.id.cancel_req_btn);

        }
    }

    public class Cancel_req extends AsyncTask<String, Integer, String> {
        private Context context;
        private String url = Constants.BaseURL + Constants.cancel_req;
        ProgressDialog progress;
        @Nullable
        String service_id="";
        HashMap<String, String> map;
        public Cancel_req(Context ctx,String service_id) {
            context = ctx;
            this.service_id=service_id;
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
                    .add(Constants.serviceid, service_id)
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
                   Toast.makeText(context,"Your cancel request accepted",Toast.LENGTH_SHORT).show();
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
