package com.app.starautoassist.Services;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.app.starautoassist.Others.Constants;
import com.app.starautoassist.Others.Starautoassist_Application;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class FirebaseInstanceIDService extends FirebaseInstanceIdService {
    private static final String TAG = FirebaseInstanceIDService.class.getSimpleName();
    public Starautoassist_Application aController = null;


    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        // Saving reg id to shared preferences
        storeRegIdInPref(refreshedToken);
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Constants.SHARED_PREF, 0);
        Constants.pref = getApplicationContext().getSharedPreferences("StarAutoAssist",MODE_PRIVATE);
        // sending reg id to your server
        if(!pref.getString("regId","").equalsIgnoreCase(refreshedToken)) {
         if(!Constants.pref.getString("mobileno","").equalsIgnoreCase(""))
            sendRegistrationToServer(refreshedToken);
        }

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
        Log.i(TAG, "Device registered: regId = " + token);
        Constants.REGISTER_ID=token;
        SharedPreferences pref = getApplication().getSharedPreferences(Constants.SHARED_PREF, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("regId", token);
        editor.apply();
        editor.commit();
    }

}
