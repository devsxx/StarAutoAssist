package com.app.starautoassist.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Payment History");
        setContentView(R.layout.activity_payment_history);

        new fetchPaymentHistory(this).execute();

        recyclerView = findViewById(R.id.rv_payment);
        paymentHistoryList = new ArrayList<HashMap<String, String>>();
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);

    }

    private class fetchPaymentHistory extends AsyncTask<String, Integer, String> {

        Context context;
        String url = Constants.BaseURL + Constants.payment_history;
        ProgressDialog progress;
        HashMap<String,String> map;
        String payid, serviceid, amount, date, servicename, transactionid;

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
                jonj = new JSONObject(jsonData);
                if (jonj.getString("status").equalsIgnoreCase(
                        "success")) {

                    String data = jonj.getString("");
                    JSONArray array = new JSONArray(data);
                    for(int i=0;i<array.length();i++) {
                        JSONObject jcat = array.getJSONObject(i);
                        map=new HashMap<String, String>();

                        payid=jcat.getString("");
                        serviceid=jcat.getString("");
                        servicename=jcat.getString("");
                        amount=jcat.getString("");
                        date=jcat.getString("");
                        transactionid=jcat.getString("");


                        map.put("id",payid);
                        map.put("date",serviceid);
                        map.put("staff",servicename);
                        map.put("subject", amount);
                        map.put("standard", date);
                        map.put("description", transactionid);

                        paymentHistoryList.add(map);
                    }

                    adapter = new PaymentHistoryAdapter(PaymentHistoryActivity.this,paymentHistoryList);
                    recyclerView.setAdapter(adapter);

                } else
                    Toast.makeText(context, jonj.getString("message"), Toast.LENGTH_SHORT).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
