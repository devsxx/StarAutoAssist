package com.app.starautoassist.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.app.starautoassist.R;

import java.util.HashMap;

public class PaymentDetailedHistoryActivity extends AppCompatActivity {

    private TextView pid, sid, sname,description, eamount,samount,tamount, sdate, tid,tid2;
    HashMap<String, String> hashMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_detailed_history);

        pid = findViewById(R.id.tv_payhistory_detail_payid);
        sid = findViewById(R.id.tv_payhistory_detail_serviceid);
        sname = findViewById(R.id.tv_payhistory_detail_servicename);
        description = findViewById(R.id.tv_payhistory_detail_des);
        samount = findViewById(R.id.tv_payhistory_detail_serviceamount);
        eamount = findViewById(R.id.tv_payhistory_detail_extraamount);
        tamount = findViewById(R.id.tv_payhistory_detail_total);
        sdate = findViewById(R.id.tv_payhistory_detail_servicedate);
        tid = findViewById(R.id.tv_payhistory_detail_transid);
        tid2 = findViewById(R.id.tv_payhistory_detail_transid2);

        hashMap = (HashMap<String, String>) getIntent().getExtras().get("data");



        pid.setText(hashMap.get("payid"));
        sid.setText(hashMap.get("serviceid"));
        sname.setText(hashMap.get("servicename"));
        description.setText(hashMap.get("description"));
        samount.setText(hashMap.get("amount"));
        eamount.setText(hashMap.get("extraamount"));
        tamount.setText((hashMap.get("amount")+hashMap.get("extraamount")));
        sdate.setText(hashMap.get("date"));
        tid.setText(hashMap.get("transid"));
        tid2.setText(hashMap.get("transid2"));
    }
}
