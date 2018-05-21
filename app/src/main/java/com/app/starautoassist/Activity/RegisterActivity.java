package com.app.starautoassist.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.MultiAutoCompleteTextView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.starautoassist.R;
import com.hsalf.smilerating.BaseRating;
import com.hsalf.smilerating.SmileRating;

import static android.view.View.VISIBLE;
import static android.view.View.inflate;

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

                if (btnuser.isSelected()){

                    String firstname = etfirstname.getText().toString();
                    String lastname = etlastname.getText().toString();
                    String email = etemail.getText().toString();
                     String phone = etphone.getText().toString();
                    String account = btnuser.getText().toString();

                }else if (btnprovider.isSelected()){

                    String firstname = etfirstname.getText().toString();
                    String lastname = etlastname.getText().toString();
                    String email = etemail.getText().toString();
                     String phone = etphone.getText().toString();
                    String account = btnprovider.getText().toString();

                }

                AlertDialog.Builder otpbuilder = new AlertDialog.Builder(this);
                LayoutInflater otpinflater = this.getLayoutInflater();
                View otpview = otpinflater.inflate(R.layout.otp_dialog, null);

                otpbuilder.setView(otpview);
                otpbuilder.setTitle("Confirm your Phone Number");
                otpbuilder.setCancelable(false);

                final AlertDialog otpdialog = otpbuilder.create();
                otpdialog.show();

                final EditText etotpphone =otpdialog.findViewById(R.id.et_otp_phone);
                Button btnotp = otpdialog.findViewById(R.id.btn_otp);

                etotpphone.setText(etphone.getText().toString());

                btnotp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        otpdialog.dismiss();
                    }
                });

                break;
            case R.id.btn_google:

                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                LayoutInflater layoutInflater = this.getLayoutInflater();
                View view = layoutInflater.inflate(R.layout.rating_alert_dialog, null);

                builder.setView(view);
                builder.setTitle("Rate & Review Us");
                builder.setCancelable(false);

                final AlertDialog dialog = builder.create();
                dialog.show();

                final MultiAutoCompleteTextView review = dialog.findViewById(R.id.review);
                final SmileRating smileRating = dialog.findViewById(R.id.smile_rating);
                Button btnsubmit = dialog.findViewById(R.id.btn_submit_rating);

                btnsubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialog.dismiss();
                    }
                });

                smileRating.setOnRatingSelectedListener(new SmileRating.OnRatingSelectedListener() {
                    @Override
                    public void onRatingSelected(int level, boolean reselected) {

                        review.setText(level + "" );
                    }
                });


                break;
            case R.id.btn_facebook:
                break;
        }
    }

}
