package com.app.starautoassist.Activity;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.starautoassist.Helper.GetSet;
import com.app.starautoassist.Helper.ResultDelegate;
import com.app.starautoassist.Others.Constants;
import com.app.starautoassist.R;
import com.hsalf.smilerating.SmileRating;
import com.ipay.IPayIH;
import com.ipay.IPayIHPayment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Random;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.app.starautoassist.Others.Constants.resultTitle;

public class Completed_Services_Payment_Activity extends AppCompatActivity  {

    TextView pname, cname,sid, sname, toll, des,spare, extra, night, total;
    Button btnpay;
    public  final String DATA = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public Random RANDOM = new Random();
    HashMap<String,String> map;
    HashMap<String, String> hashMap;
    RelativeLayout resultLay,parentLay;
    TextView resultval,okbutton;
    ImageView resulimage;
    String ref;
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

        parentLay=findViewById(R.id.parent);
        resultLay=findViewById(R.id.resultlay);
        resultval=findViewById(R.id.resulttextvalue);
        resulimage=findViewById(R.id.resultpage);
        okbutton=findViewById(R.id.okbtn);

        pname.setText(hashMap.get("sp_name"));
        cname.setText(hashMap.get(Constants.company_name));
        sname.setText(hashMap.get(Constants.service_name));
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
                IPayIHPayment payment = null;
                try {
                    payment = new IPayIHPayment();
                    payment.setMerchantCode(Constants.MerchantCode);
                    payment.setMerchantKey(Constants.MerchantKey);
                    payment.setPaymentId("");
                    payment.setCurrency(Constants.Currency);
                    ref=randomString(10);
                    payment.setRefNo(ref);
                    payment.setAmount ("1");
                  //  payment.setAmount (hashMap.get("totalamount"));
                    payment.setProdDesc (hashMap.get("description"));
                    payment.setUserName (GetSet.getFirstname());
                    payment.setUserEmail (GetSet.getEmail());
                    payment.setUserContact (GetSet.getMobileno());
                    payment.setRemark ("Service id: "+hashMap.get("service_id"));
                    payment.setLang(Constants.Lang);
                    payment.setCountry(Constants.Country);
                    payment.setBackendPostURL(Constants.backendPostURL);
                    payment.setResponseURL(Constants.responsetURL);
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

    public  String randomString(int len) {
        StringBuilder sb = new StringBuilder(len);

        for (int i = 0; i < len; i++) {
            sb.append(DATA.charAt(RANDOM.nextInt(DATA.length())));
        }

        return sb.toString();
    }
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode != 1 || data == null) {
            return;

        }


        if (resultTitle != null) {
            if(resultTitle.equalsIgnoreCase("SUCCESS"))
            {
                new Extra_Payment_Async(Completed_Services_Payment_Activity.this).execute();
                parentLay.setVisibility(View.GONE);
                resultLay.setVisibility(View.VISIBLE);
                resulimage.setImageDrawable(getResources().getDrawable(R.drawable.payment_success));
                resultval.setText("Your Transaction Id is: "+GetSet.getTransid());
                okbutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final Dialog dialog = new Dialog(Completed_Services_Payment_Activity.this);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialog.setContentView(R.layout.rating_page_dialog);
                        final SmileRating ratingbar = dialog.findViewById(R.id.ratingval);
                        final EditText review = dialog.findViewById(R.id.reviewtxt);

                        Button confirmbtn = dialog.findViewById(R.id.confirmbtn);
                        Button cancel = dialog.findViewById(R.id.cancelbtn);
                        confirmbtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                int rate=ratingbar.getRating();
                                String rev=review.getText().toString();
                                dialog.dismiss();
                            new Send_Rating(Completed_Services_Payment_Activity.this,GetSet.getMobileno(),hashMap.get(Constants.serviceprovider_id),hashMap.get("service_id"),String.valueOf(rate),rev).execute();

                            }
                        });
                        cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                        Window window = dialog.getWindow();
                        WindowManager.LayoutParams wlp = window.getAttributes();
                        wlp.gravity = Gravity.CENTER;
                        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
                        window.setAttributes(wlp);
                        dialog.setCancelable(true);
                        dialog.setCanceledOnTouchOutside(false);
                        if (!dialog.isShowing()) {
                            dialog.show();
                        }
                    }
                });
            }else {
                parentLay.setVisibility(View.GONE);
                resultLay.setVisibility(View.VISIBLE);
                resulimage.setImageDrawable(getResources().getDrawable(R.drawable.payment_failed));
                resultval.setTextColor(getResources().getColor(R.color.white));
                resultval.setText("Failed due to: "+GetSet.getErrDes());
                okbutton.setText("OK");
                okbutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent=new Intent(Completed_Services_Payment_Activity.this,HomeActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
            }
            resultTitle = null;
        }

    }
    private class Send_Rating extends AsyncTask<String, Integer, String>{

        Context context;
        String url = Constants.BaseURL+Constants.send_rating;
        ProgressDialog progress;
        HashMap<String, String> map;
        String rating,review,clientid,sp_id,service_id;

        public Send_Rating(Context context, String clientid, String sp_id, String service_id, String rating, String review) {
            this.context = context;
            this.rating = rating;
            this.review = review;
            this.clientid=clientid;
            this.sp_id=sp_id;
            this.service_id=service_id;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = new ProgressDialog(context);
            progress.setMessage("Please wait ....");
            progress.setTitle("rating & review submission...");
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
                    .add("client_id",clientid)
                    .add("sp_id",sp_id)
                    .add("serviceid",service_id)
                    .add("rating",rating)
                    .add("review",review)
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
                        Toast.makeText(Completed_Services_Payment_Activity.this, "Thank you for your rate & review", Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(Completed_Services_Payment_Activity.this,HomeActivity.class);
                        startActivity(intent);
                        finish();
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


    public class Extra_Payment_Async extends AsyncTask<String, Integer, String> {
        private Context context;
        private String url = Constants.BaseURL + Constants.extra_payment_req;
        ProgressDialog progress;
        @Nullable
        String user_id;

        public Extra_Payment_Async(Context ctx) {
            context = ctx;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Nullable
        @Override
        protected String doInBackground(String... params) {
            String jsonData = null;
            Response response = null;
            GetSet.setMobileno(Constants.pref.getString("mobileno",""));
            OkHttpClient client = new OkHttpClient();
            RequestBody body = new FormBody.Builder()
                    .add(Constants.client_id, GetSet.getMobileno())
                    .add(Constants.amount, "1")
                    //.add(Constants.amount, hashMap.get("totalamount"))
                    .add("sid", hashMap.get("service_id"))
                    .add(Constants.transid, GetSet.getTransid())
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
            Log.v("result", "" + jsonData);
            JSONObject jonj = null;
            try {
                jonj = new JSONObject(jsonData);
                if (jonj.getString("status").equalsIgnoreCase(
                        "success")) {
                    // TODO: request code here
                    Toast.makeText(context,"Transaction saved successfully",Toast.LENGTH_SHORT).show();

                }else {
                    Toast.makeText(context,jonj.getString("message"),Toast.LENGTH_SHORT).show();

                }

            }catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}

