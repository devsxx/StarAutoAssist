package com.app.starautoassist.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.app.starautoassist.R;

import static android.view.View.VISIBLE;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etemail,etfirstname, etlastname, etpassword, etconfirmpassword, etphone;
    private Button btnuser, btnprovider, btnregister, btngoogle, btnfacebook;
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
        etphone = findViewById(R.id.reg_et_phone);
        etpassword = findViewById(R.id.reg_et_password);
        etconfirmpassword = findViewById(R.id.reg_et_confirmpassword);
        btnuser = findViewById(R.id.reg_btn_user);
        btnprovider = findViewById(R.id.reg_btn_provider);
        btnregister = findViewById(R.id.reg_btn_register);
        btngoogle = findViewById(R.id.btn_google);
        btnfacebook = findViewById(R.id.btn_facebook);
        linearLayout = findViewById(R.id.google_facebook_layout);
        relativeLayout = findViewById(R.id.or_layout);
        btnuser.setOnClickListener(this);
        btnprovider.setOnClickListener(this);
        btnregister.setOnClickListener(this);
        btngoogle.setOnClickListener(this);
        btnfacebook.setOnClickListener(this);

        btnuser.setSelected(true);
        relativeLayout.setVisibility(View.VISIBLE);
        linearLayout.setVisibility(View.VISIBLE);

    }
    @Override
    public void onClick(View v) {

        Button button = (Button) v;
        btnuser.setSelected(false);
        btnuser.setPressed(false);
        btnprovider.setSelected(false);
        btnprovider.setPressed(false);
        button.setSelected(true);
        button.setPressed(true);

        switch (v.getId()){
            case R.id.reg_btn_user:
                relativeLayout.setVisibility(View.VISIBLE);
                linearLayout.setVisibility(View.VISIBLE);
                break;
            case R.id.reg_btn_provider:
                relativeLayout.setVisibility(View.GONE);
                linearLayout.setVisibility(View.GONE);
                break;
            case R.id.reg_btn_register:
                break;
            case R.id.btn_google:
                break;
            case R.id.btn_facebook:
                break;
        }
    }

}
