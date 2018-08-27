package com.app.starautoassist.Activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.app.starautoassist.Others.Starautoassist_Application;
import com.app.starautoassist.R;



public class SplashActivity extends Activity {
    private static int SPLASH_TIME_OUT = 3000;
    public static SharedPreferences pref;
    public static Editor editor;
    ImageView logo;
    Animation zoomin, zoomout, logoMoveAnimation, tran, fadein, fadeout,rotate, fadeinout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        pref = getApplicationContext().getSharedPreferences("JoysalePref",MODE_PRIVATE);
        logo = findViewById(R.id.splashlogo);
        editor = pref.edit();
        if (Starautoassist_Application.isNetworkAvailable(SplashActivity.this)) {
        animate();
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                Intent i = new Intent(SplashActivity.this,
                        IntroActivity.class);
                startActivity(i);
                finish();

                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
            }
        }, SPLASH_TIME_OUT);
    }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Starautoassist_Application.freeMemory();
    }
    public void animate()
    {

        zoomin = AnimationUtils.loadAnimation(this, R.anim.zoom_in);
        zoomout = AnimationUtils.loadAnimation(this, R.anim.zoom_out);
        tran = AnimationUtils.loadAnimation(this, R.anim.translate);
        logoMoveAnimation = AnimationUtils.loadAnimation(this, R.anim.scale_and_translate);
        fadein = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        fadeout = AnimationUtils.loadAnimation(this, R.anim.fade_out);
        fadeinout=AnimationUtils.loadAnimation(this,R.anim.fadeinandout);
        rotate = AnimationUtils.loadAnimation(this, R.anim.rotate_forward);
        AnimationSet s = new AnimationSet(false);
        s.addAnimation(zoomin);
        s.addAnimation(zoomout);
        // s.addAnimation(rotate);
        logo.startAnimation(s);
    }


}
