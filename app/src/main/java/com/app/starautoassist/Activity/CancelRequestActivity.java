package com.app.starautoassist.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.app.starautoassist.R;

public class CancelRequestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Cancel Request");
        setContentView(R.layout.activity_cancel_request);
    }
}