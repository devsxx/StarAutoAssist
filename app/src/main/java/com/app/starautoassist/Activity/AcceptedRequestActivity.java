package com.app.starautoassist.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.app.starautoassist.Adapter.Accepted_Request_Adapter;
import com.app.starautoassist.Adapter.Sent_Request_Adapter;
import com.app.starautoassist.Helper.GetSet;
import com.app.starautoassist.Others.Constants;
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

public class AcceptedRequestActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    RelativeLayout relativeLayout;
    private Accepted_Request_Adapter acceptedAdapter;
    public static ArrayList<HashMap<String, String>> accepted_list = new ArrayList<HashMap<String, String>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Accepted Request");
        setContentView(R.layout.activity_accepted_request);
        recyclerView= findViewById(R.id.req_recyclerview);
        relativeLayout=findViewById(R.id.nodatalayout);
        new Get_Accepted_List_Async(AcceptedRequestActivity.this).execute();
    }
    public class Get_Accepted_List_Async extends AsyncTask<String, Integer, String> {
        private Context context;
        private String url = Constants.BaseURL + Constants.get_accepted_list;
        ProgressDialog progress;
        @Nullable
        String user_id;
        HashMap<String, String> map;
        public Get_Accepted_List_Async(Context ctx) {
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
                    .add(Constants.mobileno, Constants.pref.getString("mobileno", ""))
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
            String spname,companyname,sname,sid,status,pickuplocation,droplocation,clientid,spcontact,address,service_description,providerimage;
            Log.v("result", "" + jsonData);
            JSONObject jonj = null;
            try {
                jonj = new JSONObject(jsonData);
                if (jonj.getString("status").equalsIgnoreCase(
                        "success")) {
                    recyclerView.setVisibility(View.VISIBLE);
                    relativeLayout.setVisibility(View.GONE);
                    String data=jonj.getString("data");
                    JSONArray array=new JSONArray(data);
                    accepted_list.clear();
                    for(int i=0;i<array.length();i++){
                        map = new HashMap<String, String>();
                        JSONObject object=array.getJSONObject(i);
                        sid=object.getString(Constants.serviceid);
                        sname=object.getString(Constants.service_name);
                        spcontact=object.getString(Constants.serviceprovider_id);
                        spname=object.getString(Constants.firstname);
                        companyname=object.getString(Constants.companyname);
                        address=object.getString(Constants.address);
                        providerimage=object.getString(Constants.providerimage);
                        service_description=object.getString(Constants.service_description);
                        status=object.getString(Constants.status);
                        clientid=object.getString(Constants.client_id);
                        pickuplocation=object.getString(Constants.pickup_location);
                        droplocation=object.getString(Constants.drop_location);
                       // rating=object.getString(Constants.rating);
                       // reviews=object.getString(Constants.review);


                        map.put(Constants.serviceid,sid);
                        map.put(Constants.service_name,sname);
                        map.put(Constants.firstname,spname);
                        map.put(Constants.serviceprovider_id,spcontact);
                        map.put(Constants.companyname,companyname);
                        map.put(Constants.address,address);
                        map.put(Constants.providerimage,providerimage);
                        map.put(Constants.service_description,service_description);
                        map.put(Constants.client_id,clientid);
                        map.put(Constants.pickup_location,pickuplocation);
                        map.put(Constants.drop_location,droplocation);
                        map.put(Constants.status,status);
                        accepted_list.add(map);
                    }
                    acceptedAdapter = new Accepted_Request_Adapter(AcceptedRequestActivity.this, accepted_list);
                    layoutManager = new LinearLayoutManager(AcceptedRequestActivity.this, LinearLayoutManager.VERTICAL, false);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(acceptedAdapter);
                } else {
                    recyclerView.setVisibility(View.GONE);
                    relativeLayout.setVisibility(View.VISIBLE);
                }

            }catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }


}
