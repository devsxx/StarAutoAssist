package com.app.starautoassist.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;
import com.app.starautoassist.R;

public class Aboutus extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aboutus);

        TextView content=(TextView)findViewById(R.id.aboutcontent);
        TextView address=(TextView)findViewById(R.id.add);
        content.setText(Html.fromHtml(getString(R.string.aboutus)));
        address.setText(Html.fromHtml(getString(R.string.address)));

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(Aboutus.this,HomeActivity.class);
        startActivity(intent);
        finish();
    }
}
