package com.app.starautoassist.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.app.starautoassist.R;
import com.google.android.gms.maps.MapView;

public class NotificationServiceActivity extends AppCompatActivity implements View.OnClickListener {

    private MapView mapView;
    private TextView tvaddress;
    private Button btncall, btnchat, btnclose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Notification Service");
        setContentView(R.layout.activity_notification_service);

        mapView = findViewById(R.id.notify_mapview);
        tvaddress = findViewById(R.id.tv_notify_address);
        btncall = findViewById(R.id.btn_notify_call);
        btnchat = findViewById(R.id.btn_notify_chat);
        btnclose = findViewById(R.id.btn_notify_close);

        btncall.setOnClickListener(this);
        btnchat.setOnClickListener(this);
        btnclose.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.btn_notify_call:
                break;
            case R.id.btn_notify_chat:
                break;
            case R.id.btn_notify_close:
                break;
        }
    }
}
