package com.app.starautoassist.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.app.starautoassist.Adapter.Completed_Service_Adapter;
import com.app.starautoassist.Others.Constants;
import com.app.starautoassist.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.logging.StreamHandler;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Completed_Services_Payment_Activity extends AppCompatActivity {

    private TextView pname, cname, sname, toll, spare, extra, night, total;
    private Button btnpay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completed__services__payment_);

        pname = findViewById(R.id.complete_tv_pname);
        cname = findViewById(R.id.complete_tv_cname);
        sname = findViewById(R.id.complete_tv_sername);
        toll = findViewById(R.id.complete_tv_toll);
        spare = findViewById(R.id.complete_tv_spare);
        extra = findViewById(R.id.complete_tv_extra);
        night = findViewById(R.id.complete_tv_night);
        total = findViewById(R.id.complete_tv_total);
        btnpay = findViewById(R.id.complete_btn_pay);

        Intent intent = getIntent();
        final String sid = intent.getStringExtra("sid");

        btnpay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new servicePayment(Completed_Services_Payment_Activity.this, sid).execute();
            }
        });
    }

    private class servicePayment extends AsyncTask<String, Integer, String>{

        Context context;
        String url = Constants.BaseURL+Constants.servicepayment;
        ProgressDialog progress;
        HashMap<String, String> map;
        String sid,sername, proname, compname, tollprice, spareprice, extraprice, nightprice, totalprice;

        public servicePayment(Context context, String sid) {
            this.context = context;
            this.sid = sid;
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
                    .add("sid", sid)
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
                        String data = jonj.getString("data");
                        JSONArray array = new JSONArray(data);
                        JSONObject object = array.getJSONObject(0);

                            sername = object.getString("");
                            proname = object.getString("");
                            compname = object.getString("");
                            tollprice = object.getString("");
                            spareprice = object.getString("");
                            extraprice = object.getString("");
                            nightprice = object.getString("");
                            totalprice = object.getString("");

                            sname.setText(sername);
                            pname.setText(proname);
                            cname.setText(compname);
                            toll.setText(tollprice);
                            spare.setText(spareprice);
                            extra.setText(extraprice);
                            night.setText(nightprice);
                            total.setText(totalprice);

                        }

                }
                else {
                    Toast.makeText(Completed_Services_Payment_Activity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}

