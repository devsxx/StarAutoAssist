package com.app.starautoassist.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.app.starautoassist.Adapter.Sent_Request_Adapter;
import com.app.starautoassist.Adapter.ServiceAdapter;
import com.app.starautoassist.Fragment.HomeFragment;
import com.app.starautoassist.Helper.GetSet;
import com.app.starautoassist.Others.Constants;
import com.app.starautoassist.Others.Starautoassist_Application;
import com.app.starautoassist.R;

import org.json.JSONArray;
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

public class SentRequestActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    private Sent_Request_Adapter serviceAdapter;
    public static ArrayList<HashMap<String, String>> sent_list = new ArrayList<HashMap<String, String>>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Sent Request");
        setContentView(R.layout.activity_sent_request);

        recyclerView= findViewById(R.id.req_recyclerview);

        new Get_Sent_Services_Async(SentRequestActivity.this).execute();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(SentRequestActivity.this,HomeActivity.class);
        startActivity(intent);
        finish();
    }

    public class Get_Sent_Services_Async extends AsyncTask<String, Integer, String> {
        private Context context;
        private String url = Constants.BaseURL + Constants.get_req_status;
        ProgressDialog progress;
        @Nullable
        String user_id;
        HashMap<String, String> map;
        public Get_Sent_Services_Async(Context ctx) {
            context = ctx;
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
                    String data=jonj.getString("data");
                    JSONArray array=new JSONArray(data);
                    sent_list.clear();
                    for(int i=0;i<array.length();i++){
                        map = new HashMap<String, String>();
                        JSONObject object=array.getJSONObject(i);
                        sid=object.getString(Constants.serviceid);
                        sname=object.getString(Constants.service_name);
                        status=object.getString(Constants.status);
                        time=object.getString(Constants.timestamp);


                        map.put(Constants.serviceid,sid);
                        map.put(Constants.service_name,sname);
                        map.put(Constants.timestamp,time);
                        map.put(Constants.status,status);
                        sent_list.add(map);
                    }
                    serviceAdapter = new Sent_Request_Adapter(SentRequestActivity.this, sent_list);
                    layoutManager = new LinearLayoutManager(SentRequestActivity.this, LinearLayoutManager.VERTICAL, false);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(serviceAdapter);
                }
            }catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Starautoassist_Application.freeMemory();
    }
}
