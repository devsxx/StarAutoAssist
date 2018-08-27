package com.app.starautoassist.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;
import com.app.starautoassist.R;

public class Aboutus extends AppCompatActivity {

    private TextView privacy, cancel, terms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("About Us");
        setContentView(R.layout.activity_aboutus);

        TextView content=(TextView)findViewById(R.id.aboutcontent);
        content.setText(Html.fromHtml(getString(R.string.aboutus)));

        privacy = findViewById(R.id.privacy_tv);
        cancel = findViewById(R.id.cancel_tv);
        terms = findViewById(R.id.termscond_tv);

        privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(Aboutus.this, PrivacyPolicy.class));
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(Aboutus.this, CancelPolicy.class));
            }
        });

        terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(Aboutus.this, Terms.class));
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(Aboutus.this,HomeActivity.class);
        startActivity(intent);
        finish();
    }
}
