package com.app.starautoassist.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.app.starautoassist.Adapter.ServiceHistoryAdapter;
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

public class ServiceHistoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ServiceHistoryAdapter adapter;
    RelativeLayout relativeLayout;
    private ArrayList<HashMap<String,String>> serviceHistoryList;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Service History");
        setContentView(R.layout.activity_service_history);

        new fetchServiceHistory(this).execute();

        recyclerView = findViewById(R.id.rv_service_history);
        relativeLayout=findViewById(R.id.nodatalayout);
        serviceHistoryList = new ArrayList<HashMap<String, String>>();
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(ServiceHistoryActivity.this,HomeActivity.class);
        startActivity(intent);
        finish();
    }

    private class fetchServiceHistory extends AsyncTask<String, Integer, String> {

        Context context;
        String url = Constants.BaseURL + Constants.service_history;
        ProgressDialog progress;
        HashMap<String,String> map;
        String serviceid, servicename, spid,email,companyname,carbrand,carmodel,plateno,paddress,daddress,payid,amount,extramount,servicetype, servicedate, providername;

        public fetchServiceHistory(Context context) {
            this.context = context;
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

        @Override
        protected String doInBackground(String... strings) {

            String jsonData = null;
            Response response = null;
            OkHttpClient client = new OkHttpClient();
            RequestBody body = new FormBody.Builder()
                    .add("client_id",Constants.pref.getString("mobileno",""))
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
            Log.v("result", "" + jsonData);
            JSONObject jonj = null;
            try {
                if (jsonData != null) {
                jonj = new JSONObject(jsonData);
                if (jonj.getString("status").equalsIgnoreCase(
                        "success")) {
                    recyclerView.setVisibility(View.VISIBLE);
                    relativeLayout.setVisibility(View.GONE);
                    String data = jonj.getString("message");
                    JSONArray array = new JSONArray(data);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jcat = array.getJSONObject(i);
                        map = new HashMap<String, String>();

                        providername = jcat.getString("firstname");
                        spid = jcat.getString("mobileno");
                        email = jcat.getString("email");
                        companyname = jcat.getString("companyname");
                        carbrand = jcat.getString("carbrand");
                        carmodel = jcat.getString("carmodel");
                        plateno = jcat.getString("plateno");
                        serviceid = jcat.getString("serviceid");
                        servicename = jcat.getString("service_name");
                        servicetype = jcat.getString("service_type");
                        paddress = jcat.getString("pickupaddress");
                        daddress = jcat.getString("dropaddress");
                        payid = jcat.getString("payid");
                        amount = jcat.getString("amount");
                        extramount = jcat.getString("extraamount");
                        servicedate = jcat.getString("date");


                        map.put("providername", providername);
                        map.put("spid", spid);
                        map.put("email", email);
                        map.put("companyname", companyname);
                        map.put("brand", carbrand);
                        map.put("model", carmodel);
                        map.put("plateno", plateno);
                        map.put("serviceid", serviceid);
                        map.put("servicename", servicename);
                        map.put("servicetype", servicetype);
                        map.put("paddress", paddress);
                        map.put("daddress", daddress);
                        map.put("payid", payid);
                        map.put("amount", amount);
                        map.put("extraamount", extramount);
                        map.put("servicedate", servicedate);

                        serviceHistoryList.add(map);
                    }

                    adapter = new ServiceHistoryAdapter(ServiceHistoryActivity.this, serviceHistoryList);
                    recyclerView.setAdapter(adapter);
                }else {
                    recyclerView.setVisibility(View.GONE);
                    relativeLayout.setVisibility(View.VISIBLE);
                }

                } else
                    Toast.makeText(context, jonj.getString("message"), Toast.LENGTH_SHORT).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
