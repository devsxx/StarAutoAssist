package com.app.starautoassist.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.app.starautoassist.Helper.GetSet;
import com.app.starautoassist.Helper.ResultDelegate;
import com.app.starautoassist.Others.Constants;
import com.app.starautoassist.R;
import com.ipay.IPayIH;
import com.ipay.IPayIHPayment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Completed_Services_Payment_Activity extends AppCompatActivity  {

    TextView pname, cname,sid, sname, toll, des,spare, extra, night, total;
    Button btnpay;
    HashMap<String,String> map;
    HashMap<String, String> hashMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completed__services__payment_);

        hashMap = (HashMap<String, String>) getIntent().getExtras().get("data");
        pname = findViewById(R.id.providerval);
        cname = findViewById(R.id.companyval);
        sname = findViewById(R.id.servicenameval);
        sid = findViewById(R.id.serviceidval);
        toll = findViewById(R.id.tollval);
        spare = findViewById(R.id.spareval);
        extra = findViewById(R.id.extraval);
        night = findViewById(R.id.nightval);
        total = findViewById(R.id.totalval);
        des = findViewById(R.id.desval);
        btnpay = findViewById(R.id.complete_btn_pay);

        pname.setText(hashMap.get("sp_name"));
        cname.setText(hashMap.get(Constants.company_name));
        sname.setText(hashMap.get("sp_name"));
        sid.setText(hashMap.get("service_id"));
        toll.setText("RM " + hashMap.get("tollamount"));
        spare.setText("RM " + hashMap.get("spareamount"));
        extra.setText("RM " + hashMap.get("extraamount"));
        night.setText("RM " + hashMap.get("nightamount"));
        total.setText("RM " + hashMap.get("totalamount"));
        des.setText(hashMap.get("description"));

        btnpay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int randomPIN = (int)(Math.random()*90000000)+10000000;
                IPayIHPayment payment = null;
                try {
                    payment = new IPayIHPayment();
                    payment.setMerchantKey (getString(R.string.merchant_key));
                    payment.setMerchantCode (getString(R.string.merchant_code));
                    payment.setPaymentId ("2");
                    payment.setCurrency ("MYR");
                    payment.setRefNo (String.valueOf(randomPIN));
                    payment.setAmount (hashMap.get("totalamount"));
                    payment.setProdDesc (hashMap.get("description"));
                    payment.setUserName (GetSet.getFirstname());
                    payment.setUserEmail (GetSet.getEmail());
                    payment.setUserContact (GetSet.getMobileno());
                    payment.setRemark ("Service id: "+hashMap.get("service_id"));
                    payment.setLang ("ISO-8859-1");
                    payment.setCountry ("MY");
                    payment.setBackendPostURL (Constants.backendurl_callback);
                    Intent checkoutIntent = IPayIH.getInstance().checkout(payment, Completed_Services_Payment_Activity.this, new
                            ResultDelegate(), IPayIH.PAY_METHOD_CREDIT_CARD);
                    startActivityForResult(checkoutIntent, 1);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }



                //new servicePayment(Completed_Services_Payment_Activity.this, sid).execute();
            }
        });
    }

    private class servicePayment extends AsyncTask<String, Integer, String>{

        Context context;
        String url = Constants.BaseURL+Constants.receive_bill;
        ProgressDialog progress;
        HashMap<String, String> map;
        String sid,sername, proname,id,serviceid,sp_id,des,status, compname, tollprice, spareprice, extraprice, nightprice, totalprice;

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
            Constants.pref=getApplicationContext().getSharedPreferences("StarAutoAssist",MODE_PRIVATE);
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

