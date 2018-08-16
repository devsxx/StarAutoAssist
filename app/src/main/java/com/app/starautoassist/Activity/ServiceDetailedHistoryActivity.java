package com.app.starautoassist.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.app.starautoassist.R;

public class ServiceDetailedHistoryActivity extends AppCompatActivity {

    private TextView sid, sname, stype,sdate, pname;
    private String id, sername, type, date, proname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_detailed_history);

        sid = findViewById(R.id.tv_servicehistory_detail_serviceid);
        sname = findViewById(R.id.tv_servicehistory_detail_servicename);
        stype = findViewById(R.id.tv_servicehistory_detail_servicetype);
        sdate = findViewById(R.id.tv_servicehistory_detail_servicedate);
        pname = findViewById(R.id.tv_servicehistory_detail_providername);

        Intent intent = getIntent();
        id = intent.getStringExtra("serviceid");
        sername = intent.getStringExtra("servicename");
        type = intent.getStringExtra("servicetype");
        date = intent.getStringExtra("servicedate");
        proname = intent.getStringExtra("providername");

        sid.setText(id);
        sname.setText(sername);
        stype.setText(type);
        sdate.setText(date);
        pname.setText(proname);
    }
}
