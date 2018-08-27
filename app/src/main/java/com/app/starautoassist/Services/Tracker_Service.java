package com.app.starautoassist.Services;

import android.Manifest;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.app.starautoassist.Helper.GetSet;
import com.app.starautoassist.Others.Constants;
import com.app.starautoassist.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Tracker_Service extends Service {

    private static final String TAG = Tracker_Service.class.getSimpleName();
    private static final long INTERVAL = 1000 * 60 * 1; //1 minute
    private static final long FASTEST_INTERVAL = 1000 * 60 * 1; // 1 minute
    public static final String ACTION = "com.app.starautoassistservice.Services.Tracker_Service";
    @Override
    public void onCreate() {
        super.onCreate();
        Constants.pref = getApplicationContext().getSharedPreferences("StarAutoAssist", MODE_PRIVATE);
        new Get_location(this).execute();
    }


    @Override
    public IBinder onBind(final Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(final Intent intent, final int flags,
                              final int startId) {

        return Service.START_NOT_STICKY;
    }

    public static class Get_location extends AsyncTask<String, Integer, String> {
        private Context context;
        private String url = Constants.BaseURL + Constants.get_location;
        String lat,lon,ispicked;
        HashMap<String, String> map;


        public Get_location(Context applicationContext) {
            this.context=applicationContext;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Nullable
        @Override
        protected String doInBackground(String... params) {
            String jsonData = null;
            Response response = null;
            OkHttpClient client = new OkHttpClient();
            RequestBody body = new FormBody.Builder()
                    .add(Constants.mobileno, Constants.pref.getString("mobileno",""))
                    .build();
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();
            Call call = client.newCall(request);

            try {
                response = call.execute();

                if (response.isSuccessful()) {
                    jsonData = response.body().string();
                } else {
                    jsonData = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return jsonData;
        }

        @Override
        protected void onPostExecute(String jsonData) {
            super.onPostExecute(jsonData);
            Log.v("result", "" + jsonData);
            JSONObject jonj = null;
            try {
                jonj = new JSONObject(jsonData);
                if (jonj.getString("status").equalsIgnoreCase(
                        "success")) {
                    String data = jonj.getString("message");
                    JSONArray array = new JSONArray(data);
                    for (int i = 0; i < array.length(); i++) {
                        map = new HashMap<String, String>();
                        JSONObject object = array.getJSONObject(i);
                        lat = object.getString(Constants.lat);
                        lon = object.getString(Constants.lon);
                        ispicked = object.getString("ispickedup");
                        map.put(Constants.lat, lat);
                        map.put(Constants.lon, lon);
                        map.put("ispickedup", ispicked);
                    }
                    Intent localBroadcastIntent = new Intent(ACTION);
                    localBroadcastIntent.putExtra("Location_Status", "Received");
                    localBroadcastIntent.putExtra("data", map);
                    LocalBroadcastManager.getInstance(context).sendBroadcast(localBroadcastIntent);
                }
            }catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }




}
