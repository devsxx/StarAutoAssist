package com.app.starautoassist.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.app.starautoassist.Helper.GetSet;
import com.app.starautoassist.Others.Constants;
import com.app.starautoassist.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mobileno, etpass;
    private Button btnlogin;
    private TextView tvforgot, tvcreate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        setContentView(R.layout.activity_login);

        changeStatusBarColor();
        mobileno = findViewById(R.id.log_et_mobileno);
        etpass = findViewById(R.id.log_et_pass);
        tvcreate = findViewById(R.id.log_tv_create);
        tvforgot = findViewById(R.id.log_tv_forgot);
        btnlogin = findViewById(R.id.log_btn_login);

        btnlogin.setOnClickListener(this);
        tvcreate.setOnClickListener(this);
        tvforgot.setOnClickListener(this);

    }

    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.log_btn_login:
                String phone = mobileno.getText().toString();
                String pass = etpass.getText().toString();
               /* new Login_Async(this,email,pass).execute();*/
            Intent intent=new Intent(this,HomeActivity.class);
            startActivity(intent);
                break;
            case R.id.log_tv_create:
                Intent registerIntent = new Intent(this, RegisterActivity.class);
                startActivity(registerIntent);
                break;
        }

    }

    public class Login_Async extends AsyncTask<String, Integer, String> {
        private Context context;
        private String username, password;
        private String url = Constants.BaseURL + Constants.login;
        ProgressDialog progress;
        @Nullable
        String user_id;

        public Login_Async(Context ctx, String username, String password) {
            context = ctx;
            this.username = username;
            this.password = password;
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
                    .add(Constants.mobileno, username)
                    .add(Constants.password, password)
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
                    GetSet.setIsLogged(true);
                    GetSet.setEmail(jonj.getString("email"));
                    GetSet.setPassword(etpass.getText().toString());
                    GetSet.setUser_type(jonj.getString("type"));
                    GetSet.setFirstname(jonj.getString("firstname"));
                    GetSet.setLastname(jonj.getString("lastname"));
                    GetSet.setCompanyname(jonj.getString("companyname"));
                    GetSet.setStreet(jonj.getString("street"));
                    GetSet.setArea(jonj.getString("area"));
                    GetSet.setLat(jonj.getString("lat"));
                    GetSet.setLon(jonj.getString("long"));
                    GetSet.setServices(jonj.getString("servicecategory"));
                    GetSet.setMobileno(jonj.getString("mobileno"));
                    GetSet.setImageUrl(jonj.getString("userimage"));


                    Constants.editor.putBoolean("isLogged", true);
                    Constants.editor.putString("email", GetSet.getEmail());
                    Constants.editor.putString("type", GetSet.getUser_type());
                    Constants.editor.putString("fname", GetSet.getFirstname());
                    Constants.editor.putString("lname", GetSet.getLastname());
                    Constants.editor.putString("mobileno", GetSet.getMobileno());
                    Constants.editor.putString("password", GetSet.getPassword());
                    Constants.editor.putString("companyname", GetSet.getCompanyname());
                    Constants.editor.putString("street", GetSet.getStreet());
                    Constants.editor.putString("area", GetSet.getArea());
                    Constants.editor.putString("lat", GetSet.getLat());
                    Constants.editor.putString("lon", GetSet.getLon());
                    Constants.editor.putString("userimage", GetSet.getImageUrl());
                    Constants.editor.putString("services", GetSet.getServices());
                    Constants.editor.commit();
                  //  Registernotifi();
                    finish();
//                        Intent i = new Intent(LoginActivity.this, FragmentMainActivity.class);
//                        startActivity(i);
                }
            }catch (JSONException e) {
                e.printStackTrace();
            }

        }
        }

    }





