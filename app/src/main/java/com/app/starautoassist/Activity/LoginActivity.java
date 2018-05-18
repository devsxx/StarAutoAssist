package com.app.starautoassist.Activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
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
          /*btnlogin.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {

              }
          });*/

          tvcreate.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {


              }
          });

          tvforgot.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {


              }
          });

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
        switch (v.getId()){
            case R.id.log_btn_login:
                break;
        }
    }
}
