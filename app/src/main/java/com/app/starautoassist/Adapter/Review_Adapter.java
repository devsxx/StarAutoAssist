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
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.app.starautoassist.Activity.HomeActivity;
import com.app.starautoassist.Helper.GetSet;
import com.app.starautoassist.Others.Constants;
import com.app.starautoassist.R;
import com.bumptech.glide.Glide;
import com.mikhaellopez.circularimageview.CircularImageView;

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

public class Review_Adapter extends RecyclerView.Adapter<Review_Adapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<HashMap<String,String>> reviewlist;
    public Review_Adapter(Context mContext, ArrayList<HashMap<String,String>> reviewlist) {

        this.mContext = mContext;
        this.reviewlist = reviewlist;
    }

    @NonNull
    @Override
    public Review_Adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_reviewlist, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final Review_Adapter.MyViewHolder holder, final int position) {
        final HashMap<String, String> itemMap=reviewlist.get(position);
         holder.name.setText(itemMap.get(Constants.firstname));
         holder.review.setText(itemMap.get(Constants.review));
         holder.ratingBar.setRating(Float.parseFloat(itemMap.get(Constants.rating)));
        Glide .with(mContext)
                .load(itemMap.get(Constants.userimage))
                .into(holder.image);



    }

    @Override
    public int getItemCount() {

        return reviewlist.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView name,review;
        RatingBar ratingBar;
        CircularImageView image;

        public MyViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.clientname);
            review = itemView.findViewById(R.id.reviewvalues);
            ratingBar = itemView.findViewById(R.id.rating);
            image = itemView.findViewById(R.id.clientimage);
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
