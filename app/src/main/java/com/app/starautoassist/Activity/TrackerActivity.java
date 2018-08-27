package com.app.starautoassist.Activity;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.app.starautoassist.Helper.DataParser;
import com.app.starautoassist.Others.Constants;
import com.app.starautoassist.Others.Starautoassist_Application;
import com.app.starautoassist.R;
import com.app.starautoassist.Services.Tracker_Service;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class TrackerActivity extends AppCompatActivity implements LocationListener, GoogleMap.OnMapClickListener, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private static final String LOCATION_UPDATE =
            "com.app.starautoassist.Services.Tracker_Service";
    SupportMapFragment mapFragment;
    String TAG = "TrackerActivity";
    private LocationManager locationManager;
    public static HashMap<String, String> hashMap;
    TextView durationtxt, distancetxt;
    ArrayList<LatLng> MarkerPoints;
    private static final long INTERVAL = 1000 * 60 * 1; //1 minute
    static GoogleMap gMap;
    Boolean status = false;
    int count;
    public  LatLng mylocation,droplocation;
    LocationRequest mLocationRequest;
    public static String dist, duration;
    static Marker mCurrLocationMarker,myMarker;
    Location location;
    public static Handler handler;
    public static Thread thread1;
    private RouteBroadCastReceiver routeReceiver;
    GoogleApiClient mGoogleApiClient;
    private static final int PERMISSIONS_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking);
        Constants.pref = this.getSharedPreferences("StarAutoAssist", MODE_PRIVATE);
        durationtxt = findViewById(R.id.durationval);
        distancetxt = findViewById(R.id.distanceval);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(TrackerActivity.this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        hashMap = (HashMap<String, String>) getIntent().getExtras().get("map");
        String[] pickuplocation = hashMap.get(Constants.pickup_location).split(",");
        mylocation = new LatLng(Double.parseDouble(pickuplocation[0]), Double.parseDouble(pickuplocation[1]));

        if(!hashMap.get(Constants.drop_location).equalsIgnoreCase("")) {
            String[] droplocation1 = hashMap.get(Constants.drop_location).split(",");
            droplocation=new LatLng(Double.parseDouble(droplocation1[0]), Double.parseDouble(droplocation1[1]));
        }

        // Check GPS is enabled
        LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(this, "Please enable location services", Toast.LENGTH_SHORT).show();
            finish();
        }


        // Check location permission is granted - if it is, start

        // the service, otherwise request the permission
        int permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (permission == PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
              //  startForegroundService(new Intent(this, Tracker_Service.class));

            } else {
              //  startService(new Intent(this, Tracker_Service.class));
            }
            //setRecurringAlarm(this);
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST);
        }
    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Starautoassist_Application.freeMemory();
    }

    private void startTrackerService() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(new Intent(this, Tracker_Service.class));

        } else {
            startService(new Intent(this, Tracker_Service.class));
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        // For Internet checking
        Starautoassist_Application.registerReceiver(TrackerActivity.this);
        if (routeReceiver == null) {
            routeReceiver = new RouteBroadCastReceiver();
        }
        final Handler handler =new Handler();
        final Runnable r = new Runnable() {
            public void run() {
                handler.postDelayed(this, INTERVAL);
                new Get_location(TrackerActivity.this).execute();
            }
        };
        handler.postDelayed(r, 0000);
        status = true;
        IntentFilter filter = new IntentFilter(Tracker_Service.ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(routeReceiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        status = false;
        // For Internet disconnect checking
        Starautoassist_Application.unregisterReceiver(TrackerActivity.this);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(routeReceiver);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[]
            grantResults) {
        if (requestCode == PERMISSIONS_REQUEST && grantResults.length == 1
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // Start the service when the permission is granted
            startTrackerService();
        } else {
            finish();
        }
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        if (ActivityCompat.checkSelfPermission(TrackerActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(TrackerActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        gMap = googleMap;
        gMap.setOnMapClickListener(TrackerActivity.this);
        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                gMap.setMyLocationEnabled(true);
                gMap.setTrafficEnabled(true);
                gMap.setIndoorEnabled(true);
                gMap.setBuildingsEnabled(true);
                gMap.getUiSettings().setCompassEnabled(false);
            }
        } else {
            buildGoogleApiClient();
            gMap.setMyLocationEnabled(true);
            gMap.setTrafficEnabled(true);
            gMap.setIndoorEnabled(true);
            gMap.setBuildingsEnabled(true);
            gMap.getUiSettings().setCompassEnabled(false);
        }
       /* GPSTracker gpsTracker = new GPSTracker(TrackerActivity.this);
        LatLng latLng = new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude());*/
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(mylocation);
        markerOptions.title("Your Requested Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        markerOptions.position(droplocation);
        markerOptions.title("Your Drop Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        mCurrLocationMarker = gMap.addMarker(markerOptions);
        //move map camera
        gMap.moveCamera(CameraUpdateFactory.newLatLng(mylocation));
        gMap.animateCamera(CameraUpdateFactory.zoomTo(11));

    }

    private void setRecurringAlarm(Context context) {

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        long triggerTime = SystemClock.elapsedRealtime()
                + INTERVAL;
        Intent downloader = new Intent(context, Tracker_Service.class);
        downloader.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, downloader, PendingIntent.FLAG_CANCEL_CURRENT);
        //If the Toggle is turned on, set the repeating alarm with a 15 minute interval
        if (alarmManager != null) {
            alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    triggerTime, INTERVAL, pendingIntent);
        }

    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, TrackerActivity.this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onMapClick(LatLng latLng) {
        // Adding new item to the ArrayList
       /* final LatLng start=mylocation;
        MarkerPoints.add(mylocation);

        TrackerActivity.this.runOnUiThread(new Runnable() {
            public void run() {
                // Creating MarkerOptions
                MarkerOptions options = new MarkerOptions();
                // Setting the position of the marker
                options.position(start).title("Your Position").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
                gMap.addMarker(options);

                // Checks, whether start and end locations are captured
                if (MarkerPoints.size() >= 2) {
                    LatLng origin = MarkerPoints.get(0);
                    LatLng dest = MarkerPoints.get(1);

                    // Getting URL to the Google Directions API
                    String url = getUrl(origin, dest);
                    Log.d("onMapClick", url.toString());
                    FetchUrl FetchUrl = new FetchUrl();

                    // Start downloading json data from Google Directions API
                    FetchUrl.execute(url);
                    //move map camera
                    gMap.moveCamera(CameraUpdateFactory.newLatLng(origin));
                    gMap.animateCamera(CameraUpdateFactory.zoomTo(11));
                }
            }
        });*/
    }

    private class RouteBroadCastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String local = intent.getExtras().getString("Location_Status");
            assert local != null;
            if (local.equals("Received")) {
                //get all data from database
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                HashMap<String, String> map = new HashMap<>();
                map = (HashMap<String, String>) getIntent().getExtras().get("data");
                buildGoogleApiClient();
                location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                LatLng latLng = null;
                if (map != null) {
                    latLng = new LatLng(Double.parseDouble(map.get(Constants.lat)), Double.parseDouble(map.get(Constants.lon)));
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(latLng);
                    markerOptions.title("Provider Position");
                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
                    mCurrLocationMarker = gMap.addMarker(markerOptions);
                    gMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                    gMap.animateCamera(CameraUpdateFactory.zoomTo(11));
                    String url;
                   if(map.get("ispickedup").equalsIgnoreCase("true")){
                       url = getUrl(droplocation, latLng);
                   }else {
                       url = getUrl(mylocation, latLng);
                   }
                    Log.d("Onlocationchanged", url);
                    FetchUrl FetchUrl = new FetchUrl();

                    // Start downloading json data from Google Directions API
                    FetchUrl.execute(url);
                }

                //move map camera

                Log.d(TAG, "onReceive: New place updated");
            }
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mGoogleApiClient.connect();
    }

    public String getUrl(LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Sensor enabled
        String sensor = "sensor=false";
        // API Key
        String API_KEY = "key=AIzaSyB9EVYM7q1cnpzqZWGHUWy0EemS-yUST3Y";
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&" + API_KEY;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;

        return url;
    }

    /**
     * A method to download json data from url
     */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();
            Log.d("downloadUrl", data.toString());
            br.close();

        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }


    // Fetches data from url passed
    private class FetchUrl extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
                Log.d("Background Task data", data.toString());
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);

        }
    }


    /**
     * A class to parse the Google Places in JSON format
     */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                Log.d("ParserTask", jsonData[0].toString());
                DataParser parser = new DataParser();
                Log.d("ParserTask", parser.toString());

                // Starts parsing data
                routes = parser.parse(jObject);
                Log.d("ParserTask", "Executing routes");
                Log.d("ParserTask", routes.toString());


            } catch (Exception e) {
                Log.d("ParserTask", e.toString());
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points;
            PolylineOptions lineOptions = null;

            durationtxt.setText(duration);
            distancetxt.setText(dist);

            // Traversing through all the routes
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(10);
                lineOptions.color(Color.RED);

                Log.d("onPostExecute", "onPostExecute lineoptions decoded");

            }

            // Drawing polyline in the Google Map for the i-th route
            if (lineOptions != null) {
                gMap.addPolyline(lineOptions);
            } else {
                Log.d("onPostExecute", "without Polylines drawn");
            }
        }
    }
    public class Get_location extends AsyncTask<String, Integer, String> {
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
            String mob=hashMap.get(Constants.serviceprovider_id);
            OkHttpClient client = new OkHttpClient();
            RequestBody body = new FormBody.Builder()
                    .add("sp_id",mob )
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
                    String data = jonj.getString("data");
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
                    LatLng latLng = null;
                    if (map != null) {
                        if(myMarker!=null)
                            myMarker.remove();
                        latLng = new LatLng(Double.parseDouble(map.get(Constants.lat)), Double.parseDouble(map.get(Constants.lon)));
                        MarkerOptions markerOptions = new MarkerOptions();
                        markerOptions.position(latLng);
                        markerOptions.title(hashMap.get(Constants.companyname));
                        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
                        myMarker = gMap.addMarker(markerOptions);
                        gMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                        gMap.animateCamera(CameraUpdateFactory.zoomTo(11));
                        String url;
                        if(map.get("ispickedup").equalsIgnoreCase("true")){
                            url = getUrl(droplocation, latLng);
                        }else {
                            url = getUrl(mylocation, latLng);
                        }
                        Log.d("Onlocationchanged", url);
                        FetchUrl FetchUrl = new FetchUrl();

                        // Start downloading json data from Google Directions API
                        FetchUrl.execute(url);
                    }
                }
            }catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

}
