package com.app.starautoassist.Services;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.app.starautoassist.Activity.HomeActivity;
import com.app.starautoassist.Others.Constants;
import com.app.starautoassist.Others.Starautoassist_Application;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class FirebaseInstanceIDService extends FirebaseInstanceIdService {
    private static final String TAG = FirebaseInstanceIDService.class.getSimpleName();
    public Starautoassist_Application aController = null;

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        // Saving reg id to shared preferences
        storeRegIdInPref(refreshedToken);

        // sending reg id to your server
     sendRegistrationToServer(refreshedToken);

        // Notify UI that registration has completed, so the progress indicator can be hidden.
        Intent registrationComplete = new Intent(Constants.REGISTRATION_COMPLETE);
        registrationComplete.putExtra("token", refreshedToken);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }

    public void sendRegistrationToServer(final String token) {
        // sending gcm token to server
        if(aController == null)
            aController = (Starautoassist_Application) getApplicationContext();
        Constants.REGISTER_ID = token;
        Log.i(TAG, "Device registered: regId = " + token);
        // Log.d("NAME", MainActivity.name);
        aController.register(getApplicationContext());
        Log.e(TAG, "sendRegistrationToServer: " + token);
    }

    private void storeRegIdInPref(String token) {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Constants.SHARED_PREF, 0);
        Log.i(TAG, "Device registered: regId = " + token);
        Constants.REGISTER_ID=token;
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("regId", token);
        editor.commit();
    }

}
