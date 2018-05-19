package com.app.starautoassist.Activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import com.app.starautoassist.Others.Starautoassist_Application;
import com.app.starautoassist.R;


public class SplashActivity extends Activity {

    private static int SPLASH_TIME_OUT = 1000;
    private static Dialog settingsDialog;
    public static SharedPreferences pref;
    public static Editor editor;
    String[] languages, langCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        pref = getApplicationContext().getSharedPreferences("JoysalePref",
                MODE_PRIVATE);
        editor = pref.edit();
        if (Starautoassist_Application.isNetworkAvailable(SplashActivity.this)) {
        }
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

                Intent i = new Intent(SplashActivity.this,
                        IntroActivity.class);
                startActivity(i);
                finish();

                overridePendingTransition(R.anim.fade_in,
                        R.anim.fade_out);
            }
        }, SPLASH_TIME_OUT);
    }
}