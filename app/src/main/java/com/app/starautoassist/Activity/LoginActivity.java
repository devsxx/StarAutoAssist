package com.app.starautoassist.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.app.starautoassist.Others.Constants;
import com.app.starautoassist.R;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText etemail, etpass;
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
        etemail = findViewById(R.id.log_et_email);
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
                String email = etemail.getText().toString();
                String pass = etpass.getText().toString();

                break;
            case R.id.log_tv_create:
                break;
            case R.id.log_tv_forgot:
                break;
        }
    }
}
    /*public class LoginAsync extends AsyncTask<String, Integer, String> {

        private Context context;
        private Productinterface_and_cartcount listener;
        ProgressDialog progress;
        SessionManager session;
        Constants config = new Constants();
        @Nullable
        String user_id;
        final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        private String url = config.HOSTURL + config.Get_Product;

        public ProductAsync(Context ctx, Productinterface_and_cartcount listener) {
            context = ctx;
            this.listener = listener;
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
            session = new SessionManager(context);
            shared = context.getSharedPreferences("EPOS", 0);
            user_id = (shared.getString("key_userid", "1"));
            OkHttpClient client = new OkHttpClient();
            RequestBody body = new FormBody.Builder()
                    .add(config.CUSTOMERID, user_id)
                    .add(config.CAT_ID, Config.sub_categoryid)
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
            listener.productinterface(jsonData, user_id);
        }
}*/
