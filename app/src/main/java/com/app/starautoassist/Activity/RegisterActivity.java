package com.app.starautoassist.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.MultiAutoCompleteTextView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.app.starautoassist.Helper.GetSet;
import com.app.starautoassist.Others.Constants;
import com.app.starautoassist.R;
import com.hsalf.smilerating.SmileRating;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import dmax.dialog.SpotsDialog;
import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private MaterialEditText etemail, etfirstname, etlastname, etpassword, etconfirmpassword;
    private Button btnregister;
    private RelativeLayout relativeLayout;
    Boolean verified=false;
    String mobile_no="";
    private SpotsDialog progressDialog;
    EditText etotpphone,confirmotpcode;
    Button btnotp,btnconfirm;
    HashMap<String,String> socialMap;
    LinearLayout otpLay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_register);

        etemail = findViewById(R.id.reg_et_email);
        etfirstname = findViewById(R.id.reg_et_firstname);
        etlastname = findViewById(R.id.reg_et_lastname);
        etpassword = findViewById(R.id.reg_et_password);
        etconfirmpassword = findViewById(R.id.reg_et_confirmpassword);
        btnregister = findViewById(R.id.reg_btn_register);
        otpLay=(LinearLayout)findViewById(R.id.otplayout);
        relativeLayout=(RelativeLayout) findViewById(R.id.relative_layout);
        btnregister.setOnClickListener(this);
        progressDialog = new SpotsDialog(this, R.style.Custom);

        Intent intent=getIntent();
        if(intent.hasExtra("data")) {
            socialMap = (HashMap<String, String>) getIntent().getExtras().get("gdata");
            etfirstname.setText(socialMap.get("firstName"));
            etlastname.setText(socialMap.get("lastName"));
            etemail.setText(socialMap.get("email"));

        }

        etotpphone = findViewById(R.id.et_otp_phone);
        btnotp = findViewById(R.id.btn_otp);
        confirmotpcode = findViewById(R.id.confirmotp_code);
        btnconfirm = findViewById(R.id.btn_confirm);
        btnotp.setOnClickListener(this);
        btnconfirm.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.reg_btn_register:
                String fname=etfirstname.getText().toString().trim();
                String lname=etlastname.getText().toString().trim();
                String email=etemail.getText().toString().trim();
                String password=etpassword.getText().toString().trim();
                String confirmpassword=etconfirmpassword.getText().toString().trim();
                if(fname.equals("")){
                    Toast.makeText(RegisterActivity.this, getString(R.string.please_fill_all), Toast.LENGTH_SHORT).show();
                }else if(lname.equals("")){
                    Toast.makeText(RegisterActivity.this, getString(R.string.please_fill_all), Toast.LENGTH_SHORT).show();
                }else if(email.equals("")){
                    Toast.makeText(RegisterActivity.this, getString(R.string.please_fill_all), Toast.LENGTH_SHORT).show();
                }else if(password.equals("")){
                    Toast.makeText(RegisterActivity.this, getString(R.string.please_fill_all), Toast.LENGTH_SHORT).show();
                }else if(confirmpassword.equals("")){
                    Toast.makeText(RegisterActivity.this, getString(R.string.please_fill_all), Toast.LENGTH_SHORT).show();
                }else if(!confirmpassword.equals(password)){
                    Toast.makeText(RegisterActivity.this, "Password not match", Toast.LENGTH_SHORT).show();
                }else {
                    relativeLayout.setVisibility(View.GONE);
                    otpLay.setVisibility(View.VISIBLE);
                }

                break;
            case R.id.btn_otp:
                if(!etotpphone.getText().toString().equals("")&&etotpphone.getText().toString().length()>6) {
                    mobile_no = etotpphone.getText().toString().trim();
                    // new GetOTP(getApplicationContext(), mobile_no).execute();
                    //Toast.makeText(getApplicationContext(),"Otp sent to your mobile successfully",Toast.LENGTH_SHORT).show();
                    try {
                        etotpphone.setVisibility(View.GONE);
                        btnotp.setVisibility(View.GONE);
                        confirmotpcode.setVisibility(View.VISIBLE);
                        btnconfirm.setVisibility(View.VISIBLE);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }else Toast.makeText(RegisterActivity.this,"Check your no",Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_confirm:
                String otp = confirmotpcode.getText().toString().trim();
                //   new VerifyOTP(getApplicationContext(), GetSet.getMobileno(), otp).execute();
                new VerifyOTP(RegisterActivity.this, mobile_no, "1234").execute();
                break;

        }
    }

    public class GetOTP extends AsyncTask<String, Integer, String> {
        private Context context;
        private String mobileno;
        private String url = Constants.BaseURL + Constants.getOTP;
        ProgressDialog progress;
        @Nullable
        String user_id;

        public GetOTP(Context ctx, String mobileno) {
            context = ctx;
            this.mobileno = mobileno;
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
                    .add(Constants.mobileno, mobileno)
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
                        "true")) {
                    mobile_no=mobileno;
                    //Toast.makeText(getApplicationContext(),"Otp sent to your mobile successfully",Toast.LENGTH_SHORT).show();
                    AlertDialog.Builder confirmotpbuilder = new AlertDialog.Builder(getApplicationContext());
                    LayoutInflater confirmotpinflater;
                    confirmotpinflater = getLayoutInflater();
                    View confirmotpview = confirmotpinflater.inflate(R.layout.confirm_otp_dialog, null);
                    confirmotpbuilder.setView(confirmotpview);
                    confirmotpbuilder.setTitle("Please wait for Confirmation !");
                    confirmotpbuilder.setCancelable(false);
                    final AlertDialog confirmotpdialog = confirmotpbuilder.create();
                    confirmotpdialog.show();
                    final EditText confirmotpcode = confirmotpdialog.findViewById(R.id.confirmotp_code);
                    Button btnconfirm = confirmotpdialog.findViewById(R.id.btn_confirm);

                    btnconfirm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String otp = confirmotpcode.getText().toString().trim();
                        //   new VerifyOTP(getApplicationContext(), GetSet.getMobileno(), otp).execute();
                            confirmotpdialog.dismiss();
                           new VerifyOTP(RegisterActivity.this, mobile_no, "1234").execute();

                        }
                    });

                } else {
                    Toast.makeText(RegisterActivity.this, jonj.getString("message").trim(), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    public class VerifyOTP extends AsyncTask<String, Integer, String> {
        private Context context;
        private String mobileno, otp;
        private String url = Constants.BaseURL + Constants.verifyOTP;
        @Nullable
        String user_id;

        public VerifyOTP(Context ctx, String mobileno, String otp) {
            context = ctx;
            this.mobileno = mobileno;
            this.otp = otp;
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
            OkHttpClient client = new OkHttpClient();
            RequestBody body = new FormBody.Builder()
                    .add(Constants.mobileno, mobileno)
                    .add(Constants.otp, otp)
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
                        verified=true;
                        new Register_Asyc(context).execute();
                    }else Toast.makeText(getApplicationContext(),"Otp not verified",Toast.LENGTH_SHORT).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public class Register_Asyc extends AsyncTask<String, Integer, String> {
        private Context context;
        private String url = Constants.BaseURL + Constants.registration;
        String img="";
        @Nullable
        String user_id;

        public Register_Asyc(Context ctx) {
            context = ctx;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        //    progressDialog.show();
        }

        @Nullable
        @Override
        protected String doInBackground(String... params) {
            String jsonData = null;
            if(!socialMap.get("image").equalsIgnoreCase("")||socialMap.get("image")!=null)
                img=socialMap.get("image");
            Response response = null;
            OkHttpClient client = new OkHttpClient();
            RequestBody body = new FormBody.Builder()
                    .add(Constants.mobileno, mobile_no)
                    .add(Constants.firstname, etfirstname.getText().toString().trim())
                    .add(Constants.lastname, etlastname.getText().toString().trim())
                    .add(Constants.email, etemail.getText().toString().trim())
                    .add(Constants.password, etconfirmpassword.getText().toString().trim())
                    .add(Constants.userimg,img)
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
           // progressDialog.dismiss();
            Log.v("result", "" + jsonData);
            JSONObject jonj = null;
            try {
                jonj = new JSONObject(jsonData);
                if (jonj.getString("status").equalsIgnoreCase(
                        "success")) {
                    Toast.makeText(getApplicationContext(),"Registration successfully",Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(context,LoginActivity.class);
                    startActivity(intent);
                    finish();

                }else
                {
                    Toast.makeText(getApplicationContext(),jonj.getString("message"),Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }
}
