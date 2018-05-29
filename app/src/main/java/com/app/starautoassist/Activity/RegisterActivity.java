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
    private LinearLayout linearLayout;


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
        linearLayout = findViewById(R.id.google_facebook_layout);
        relativeLayout = findViewById(R.id.or_layout);
        btnregister.setOnClickListener(this);
        relativeLayout.setVisibility(View.VISIBLE);
        linearLayout.setVisibility(View.VISIBLE);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.reg_btn_register:

                AlertDialog.Builder otpbuilder = new AlertDialog.Builder(this);
                LayoutInflater otpinflater = this.getLayoutInflater();
                View otpview = otpinflater.inflate(R.layout.otp_dialog, null);

                otpbuilder.setView(otpview);
                otpbuilder.setTitle("Verify Your Number :");
                otpbuilder.setCancelable(false);

                final AlertDialog otpdialog = otpbuilder.create();
                otpdialog.show();

                final EditText etotpphone = otpdialog.findViewById(R.id.et_otp_phone);
                Button btnotp = otpdialog.findViewById(R.id.btn_otp);

                btnotp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String mobileno = etotpphone.getText().toString().trim();
                        new GetOTP(getApplicationContext(), mobileno).execute();
                        otpdialog.dismiss();
                    }
                });

                break;

        }
    }

    public class GetOTP extends AsyncTask<String, Integer, String> {
        private Context context;
        private String mobileno;
        private String url = Constants.BaseURL + Constants.getotp;
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
                            new VerifyOTP(getApplicationContext(), GetSet.getMobileno(), otp).execute();
                            confirmotpdialog.dismiss();
                        }
                    });

                } else {
                    Toast.makeText(getApplicationContext(), jonj.getString("message").trim(), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    public class VerifyOTP extends AsyncTask<String, Integer, String> {
        private Context context;
        private String mobileno, otp;
        private String url = Constants.BaseURL + Constants.getotp;
        ProgressDialog progress;
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
            progress.dismiss();
            Log.v("result", "" + jsonData);
            JSONObject jonj = null;
            try {
                jonj = new JSONObject(jsonData);
                if (jonj.getString("status").equalsIgnoreCase(
                        "true")) {
                    Toast.makeText(getApplicationContext(),"Otp verified successfully",Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(getApplicationContext(),HomeActivity.class);
                    startActivity(intent);
                    finish();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }
}
