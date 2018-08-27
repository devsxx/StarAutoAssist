package com.app.starautoassist.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.starautoassist.R;

import java.util.HashMap;

public class ServiceDetailedHistoryActivity extends AppCompatActivity {

    private TextView sid,spid,email,brand,model,plateno,pickup,drop,payid,amt,companyname, sname, stype,sdate, pname;
    HashMap<String, String> hashMap;
    LinearLayout droplayout,stypelayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_detailed_history);

        companyname = findViewById(R.id.tv_servicehistory_detail_companyname);
        sid = findViewById(R.id.tv_servicehistory_detail_serviceid);
        pname = findViewById(R.id.tv_servicehistory_detail_providername);
        spid = findViewById(R.id.tv_servicehistory_detail_spid);
        email = findViewById(R.id.tv_servicehistory_detail_email);
        brand = findViewById(R.id.tv_brand);
        model = findViewById(R.id.tv_model);
        plateno = findViewById(R.id.tv_plateno);
        sname = findViewById(R.id.tv_servicehistory_detail_servicename);
        stype = findViewById(R.id.tv_servicehistory_detail_servicetype);
        stypelayout = findViewById(R.id.servcietypelayout);
        pickup = findViewById(R.id.tv_pickup);
        drop = findViewById(R.id.tv_drop);
        droplayout = findViewById(R.id.droplay);
        payid = findViewById(R.id.tv_payid);
        amt = findViewById(R.id.tv_amount);
        sdate = findViewById(R.id.tv_servicehistory_detail_servicedate);


        hashMap = (HashMap<String, String>) getIntent().getExtras().get("data");

        sid.setText(hashMap.get("serviceid"));
        companyname.setText(hashMap.get("companyname"));
        pname.setText(hashMap.get("providername"));
        spid.setText(hashMap.get("spid"));
        email.setText(hashMap.get("email"));
        brand.setText(hashMap.get("brand"));
        model.setText(hashMap.get("model"));
        plateno.setText(hashMap.get("plateno"));
        sname.setText(hashMap.get("servicename"));
        if(!hashMap.get("servicetype").equalsIgnoreCase(""))
            stype.setText(hashMap.get("servicetype"));
        else
            stypelayout.setVisibility(View.GONE);
        pickup.setText(hashMap.get("paddress"));
        if(!hashMap.get("daddress").equalsIgnoreCase(""))
            drop.setText(hashMap.get("daddress"));
        else
            droplayout.setVisibility(View.GONE);
        payid.setText(hashMap.get("payid"));
        amt.setText((hashMap.get("amount")+hashMap.get("extraamount")));
        sdate.setText(hashMap.get("servicedate"));
    }
}
