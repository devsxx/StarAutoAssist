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
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.app.starautoassist.Helper.GetSet;
import com.app.starautoassist.Others.Constants;
import com.app.starautoassist.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Tracker_Service extends Service {

    private static final String TAG = Tracker_Service.class.getSimpleName();
    Double lat;
    Double lon;
    long elapsedtime,time;
    Double altitude;
    Boolean complete,frommock;
    String provider="";
    Float accuracy,bearing,speed;
    @Override
    public IBinder onBind(Intent intent) {return null;}

    @Override
    public void onCreate() {
        super.onCreate();
        buildNotification();
        loginToFirebase();
    }

    private void buildNotification() {
        String stop = "stop";
        registerReceiver(stopReceiver, new IntentFilter(stop));
        PendingIntent broadcastIntent = PendingIntent.getBroadcast(
                this, 0, new Intent(stop), PendingIntent.FLAG_UPDATE_CURRENT);
        // Create the persistent notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(getString(R.string.notification_text))
                .setOngoing(true)
                .setContentIntent(broadcastIntent)
                .setSmallIcon(R.drawable.logo);
        startForeground(1, builder.build());
    }

    protected BroadcastReceiver stopReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "received stop broadcast");
            // Stop the service when the notification is tapped
            unregisterReceiver(stopReceiver);
            stopSelf();
        }
    };

    private void loginToFirebase() {
        // Functionality coming next step
    }

    private void requestLocationUpdates() {
        LocationRequest request = new LocationRequest();
        request.setInterval(10000);
        request.setFastestInterval(5000);
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(this);
      //  final String path = getString(R.string.firebase_path) + "/" + getString(R.string.transport_id);
        int permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (permission == PackageManager.PERMISSION_GRANTED) {
            // Request location updates and when an update is
            // received, store the location in Firebase
            client.requestLocationUpdates(request, new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
//                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference(path);
                    Location location = locationResult.getLastLocation();
                    if (location != null) {
                        Log.d(TAG, "location update " + location);
                        accuracy=location.getAccuracy();
                        speed=location.getSpeed();
                        altitude=location.getAltitude();
                        lat=location.getLatitude();
                        lon=location.getLongitude();
                        provider=location.getProvider();
                        time=location.getTime();
                        elapsedtime=location.getElapsedRealtimeNanos();
                        new Sendlocation(getApplicationContext(),altitude,accuracy,speed,lat,lon,provider,time,elapsedtime).execute();

                    }
                }
            }, null);
        }
    }
    public class Sendlocation extends AsyncTask<String, Integer, String> {
        private Context context;
        private String username, password;
        private String url = Constants.BaseURL + Constants.login;
        Double altitude,lat,lon;
        Float accuracy,speed;
        String  provider;
        long time,elapsedtime;
        ProgressDialog progress;
        @Nullable
        String user_id;


        public Sendlocation(Context applicationContext, Double altitude, Float accuracy,
                            Float speed, Double lat, Double lon, String provider, long time, long elapsedtime) {
            this.context=applicationContext;
            this.altitude=altitude;
            this.accuracy=accuracy;
            this.speed=speed;
            this.lat=lat;
            this.lon=lon;
            this.provider=provider;
            this.time=time;
            this.elapsedtime=elapsedtime;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = new ProgressDialog(context);
            progress.setMessage("Please wait ....");
            progress.setTitle("Loading");
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.show();
        }

        @Nullable
        @Override
        protected String doInBackground(String... params) {
            String jsonData = null;
            Response response = null;
            OkHttpClient client = new OkHttpClient();
            RequestBody body = new FormBody.Builder()
                    .add(Constants.mobileno, username)
                    .add("altitude", altitude.toString())
                    .add("accuracy", accuracy.toString())
                    .add("speed", speed.toString())
                    .add("lat", lat.toString())
                    .add("lon", lon.toString())
                    .add("provider", provider.toString())
                    .add("time", String.valueOf(time))
                    .add("elapsedtime", String.valueOf(elapsedtime))
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
            progress.dismiss();
            Log.v("result", "" + jsonData);
            JSONObject jonj = null;
            try {
                jonj = new JSONObject(jsonData);
                if (jonj.getString("status").equalsIgnoreCase(
                        "true")) {
                    Log.d(TAG, "Location updated to server");
                }
            }catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

}
