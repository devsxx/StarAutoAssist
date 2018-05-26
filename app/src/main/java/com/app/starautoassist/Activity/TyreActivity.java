package com.app.starautoassist.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.app.starautoassist.R;

public class TyreActivity extends AppCompatActivity {

    private TextView tvfare, tvcharge;
    private Button btnsend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Tyre Service");
        setContentView(R.layout.activity_tyre);

        tvfare = findViewById(R.id.tyre_tv_fare);
        tvcharge = findViewById(R.id.tyre_tv_charge);
        btnsend = findViewById(R.id.tyre_btn_send);

        btnsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });

        tvfare.setText("");
        tvcharge.setText("");
    }
}
