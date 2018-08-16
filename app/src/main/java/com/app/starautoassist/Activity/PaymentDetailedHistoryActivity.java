package com.app.starautoassist.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.app.starautoassist.R;

public class PaymentDetailedHistoryActivity extends AppCompatActivity {

    private TextView pid, sid, sname, samount, sdate, tid;
    private String payid, serid, sername, seramount, serdate, transid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_detailed_history);

        pid = findViewById(R.id.tv_payhistory_detail_payid);
        sid = findViewById(R.id.tv_payhistory_detail_serviceid);
        sname = findViewById(R.id.tv_payhistory_detail_servicename);
        samount = findViewById(R.id.tv_payhistory_detail_serviceamount);
        sdate = findViewById(R.id.tv_payhistory_detail_servicedate);
        tid = findViewById(R.id.tv_payhistory_detail_transid);

        Intent intent = getIntent();
        payid = intent.getStringExtra("payid");
        serid = intent.getStringExtra("serviceid");
        seramount = intent.getStringExtra("amount");
        sername = intent.getStringExtra("servicename");
        serdate = intent.getStringExtra("date");
        transid = intent.getStringExtra("transactionid");


        pid.setText(payid);
        sid.setText(serid);
        sname.setText(sername);
        samount.setText(seramount);
        sdate.setText(serdate);
        tid.setText(transid);
    }
}
