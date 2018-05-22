package com.app.starautoassist.Activity;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.MultiAutoCompleteTextView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.app.starautoassist.R;
import com.hsalf.smilerating.SmileRating;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etemail,etfirstname, etlastname, etpassword, etconfirmpassword;
    private Button btnregister, btngoogle, btnfacebook;
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
        btngoogle = findViewById(R.id.btn_google);
        btnfacebook = findViewById(R.id.btn_facebook);
        linearLayout = findViewById(R.id.google_facebook_layout);
        relativeLayout = findViewById(R.id.or_layout);
        btnregister.setOnClickListener(this);
        btngoogle.setOnClickListener(this);
        btnfacebook.setOnClickListener(this);

        relativeLayout.setVisibility(View.VISIBLE);
        linearLayout.setVisibility(View.VISIBLE);

    }
    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.reg_btn_register:

                AlertDialog.Builder otpbuilder = new AlertDialog.Builder(this);
                LayoutInflater otpinflater = this.getLayoutInflater();
                View otpview = otpinflater.inflate(R.layout.otp_dialog, null);

                otpbuilder.setView(otpview);
                otpbuilder.setTitle("Verify Your Number :");
                otpbuilder.setCancelable(false);

                final AlertDialog otpdialog = otpbuilder.create();
                otpdialog.show();

                EditText etotpphone = otpdialog.findViewById(R.id.et_otp_phone);
                Button btnotp = otpdialog.findViewById(R.id.btn_otp);

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
                builder.setTitle("Rate & Review Us :");
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

                AlertDialog.Builder confirmotpbuilder = new AlertDialog.Builder(this);
                LayoutInflater confirmotpinflater = this.getLayoutInflater();
                View confirmotpview = confirmotpinflater.inflate(R.layout.confirm_otp_dialog, null);

                confirmotpbuilder.setView(confirmotpview);
                confirmotpbuilder.setTitle("Please wait for Confirmation !");
                confirmotpbuilder.setCancelable(false);

                final AlertDialog confirmotpdialog = confirmotpbuilder.create();
                confirmotpdialog.show();

                TextView tvconfirmotpcode = confirmotpdialog.findViewById(R.id.tv_confirmotp_code);
                Button btnconfirm = confirmotpdialog.findViewById(R.id.btn_confirm);

                btnconfirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        confirmotpdialog.dismiss();
                    }
                });


                break;
        }
    }

}
