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

import com.app.starautoassist.Adapter.PaymentHistoryAdapter;
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

public class PaymentHistoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PaymentHistoryAdapter adapter;
    private ArrayList<HashMap<String,String>> paymentHistoryList;
    private RecyclerView.LayoutManager mLayoutManager;
    RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Payment History");
        setContentView(R.layout.activity_payment_history);

        new fetchPaymentHistory(this).execute();

        recyclerView = findViewById(R.id.rv_payment);
        relativeLayout=findViewById(R.id.nodatalayout);
        paymentHistoryList = new ArrayList<HashMap<String, String>>();
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(PaymentHistoryActivity.this,HomeActivity.class);
        startActivity(intent);
        finish();
    }

    private class fetchPaymentHistory extends AsyncTask<String, Integer, String> {

        Context context;
        String url = Constants.BaseURL + Constants.payment_history;
        ProgressDialog progress;
        HashMap<String,String> map;
        String payid, serviceid,description, examount,amount, date, servicename, transactionid2,transactionid;

        public fetchPaymentHistory(Context context) {
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

                        payid = jcat.getString("payid");
                        serviceid = jcat.getString("serviceid");
                        description = jcat.getString("des");
                        servicename = jcat.getString("service_name");
                        amount = jcat.getString("amount");
                        examount = jcat.getString("extraamount");
                        date = jcat.getString("date");
                        transactionid = jcat.getString("transid");
                        transactionid2 = jcat.getString("transid2");


                        map.put("payid", payid);
                        map.put("serviceid", serviceid);
                        map.put("description", description);
                        map.put("servicename", servicename);
                        map.put("amount", amount);
                        map.put("extraamount", examount);
                        map.put("date", date);
                        map.put("transid", transactionid);
                        map.put("transid2", transactionid2);
                        paymentHistoryList.add(map);
                    }

                    adapter = new PaymentHistoryAdapter(PaymentHistoryActivity.this, paymentHistoryList);
                    recyclerView.setAdapter(adapter);
                }else{
                    recyclerView.setVisibility(View.VISIBLE);
                    relativeLayout.setVisibility(View.GONE);
                }

                } else
                    Toast.makeText(context, jonj.getString("message"), Toast.LENGTH_SHORT).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
