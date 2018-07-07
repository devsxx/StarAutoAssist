package com.app.starautoassist.Activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.starautoassist.Adapter.PlacesAutoCompleteAdapter;
import com.app.starautoassist.Helper.GPSTracker;
import com.app.starautoassist.Helper.GetSet;
import com.app.starautoassist.Others.Constants;
import com.app.starautoassist.Others.Starautoassist_Application;
import com.app.starautoassist.R;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Towing_Activity extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback, TextWatcher {
    MapView mapView;
    private GoogleMap map;
    String TAG="Towing_Activity";
    GoogleApiClient mGoogleApiClient;
    LocationManager locationManager;
    TextView setFrom, setTo;
    ImageView myLocation;
    int screenHeight, screenWidth;
    public static boolean locationfound = false;
    AlertDialog alertDialog;
    public static String fromlocation = "",tolocation="";
    public static double flat, flon, tlat, tlon;
    public static double onreslat,onreslon;
    AutoCompleteTextView from, to;
    GPSTracker gps;
    TextView submitbtn;
    String amount="",brand="",model="",flatbed="";
    RelativeLayout fromLayout, toLayout, next;
    private LatLng center;
    SupportMapFragment mapFragment;
    private Geocoder geocoder;
    public String tow_type="";
    private List<Address> addresses;
    private CountDownTimer mDragTimer;
    private boolean mTimerIsRunning = false;
    android.support.v7.app.AlertDialog.Builder towbuilder;
    android.support.v7.app.AlertDialog towdialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Towing Service");
        setContentView(R.layout.activity_location);
        fromLayout = findViewById(R.id.fromlay);
        toLayout = findViewById(R.id.droplay);
        next = findViewById(R.id.bottomLay);
        myLocation = findViewById(R.id.my_location);
        from = findViewById(R.id.fromaddress);
        to = findViewById(R.id.toaddress);
        submitbtn = findViewById(R.id.apply);
        setFrom = findViewById(R.id.fromset);
        setTo = findViewById(R.id.toset);
        if(getIntent().hasExtra("service_chrg")) {
            amount= getIntent().getStringExtra("service_chrg");
            brand= getIntent().getStringExtra("brand");
            model= getIntent().getStringExtra("model");
            flatbed= getIntent().getStringExtra("flatbed");
        }
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        setFrom.setOnClickListener(this);
        setTo.setOnClickListener(this);
        myLocation.setOnClickListener(this);
        next.setOnClickListener(this);
        from.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String str = (String) adapterView.getItemAtPosition(position);
                if (str.trim().length() != 0) {
                    try {
                        new getLocationFromString("from").execute(str).get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }

                }

            }
        });
        to.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String str = (String) adapterView.getItemAtPosition(position);
                if (str.trim().length() != 0) {
                    try {
                      new getLocationFromString("to").execute(str).get();

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
        from.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()==0){
                    setFrom.setEnabled(true);
                    setFrom.setTextColor(getResources().getColor(R.color.green));
                }else {
                    setFrom.setEnabled(false);
                    setFrom.setTextColor(getResources().getColor(R.color.gray));
                }
            }
        });
        to.addTextChangedListener(this);
        from.setAdapter(new PlacesAutoCompleteAdapter(Towing_Activity.this, R.layout.mapsuggestiontextview_layout));
        to.setAdapter(new PlacesAutoCompleteAdapter(Towing_Activity.this, R.layout.mapsuggestiontextview_layout));

        Display display = getWindowManager().getDefaultDisplay();
        int height = display.getHeight();
        int weight = display.getWidth();
        screenHeight = height * 60 / 100;
        screenWidth = weight * 80 / 100;

        towbuilder = new android.support.v7.app.AlertDialog.Builder(Towing_Activity.this);
        LayoutInflater towlayoutInflater = Towing_Activity.this.getLayoutInflater();
        View towview = towlayoutInflater.inflate(R.layout.tow_service_dialog, null);
        ImageButton ivwheellift = towview.findViewById(R.id.tow_iv_wheellift);
        ImageButton ivflatbed = towview.findViewById(R.id.tow_iv_flatbed);


        towbuilder.setView(towview);
        towbuilder.setTitle("Select type of Tow Truck :");
        towbuilder.setCancelable(false);


        towdialog  = towbuilder.create();
        ivwheellift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tow_type="Wheel Lift";
                towdialog.dismiss();
            }
        });


        ivflatbed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tow_type="Flat Bed";
                towdialog.dismiss();
            }
        });
        if(flatbed.equalsIgnoreCase("0")) {
            towdialog.show();
        }
        else
            tow_type="Flat Bed";


       // imm = (InputMethodManager)getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        alertDialog = new AlertDialog.Builder(Towing_Activity.this).create();
        alertDialog.setTitle(getString(R.string.gps_settings));
        alertDialog.setMessage(getString(R.string.gps_notenabled));
        alertDialog.setButton(getString(R.string.settings), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        });
        alertDialog.setButton2(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.setCancelable(false);
        Log.d(TAG, "onCreate: ");


        from.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    try {
                        InputMethodManager imm=(InputMethodManager)getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        if (imm != null) {
                            imm.hideSoftInputFromWindow(from.getWindowToken(), 0);
                        }
                        if (from.getText().toString().trim().length() != 0) {
                            new getLocationFromString("from").execute(from.getText().toString().trim()).get();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    return true;
                }
                return false;
            }
        });

        ViewTreeObserver observer = from.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Log.v("searchLay.getWidth()", "searchLay.getWidth()==" + fromLayout.getWidth());
                if (fromLayout.getWidth() > 0) {
                    from.setDropDownWidth(fromLayout.getWidth());
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
                        fromLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    } else {
                        fromLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    }
                }
            }
        });
        to.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    try {
                        InputMethodManager imm=(InputMethodManager)getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        if (imm != null) {
                            imm.hideSoftInputFromWindow(to.getWindowToken(), 0);
                        }

                        if (to.getText().toString().trim().length() != 0) {
                            new getLocationFromString("to").execute(to.getText().toString().trim()).get();

                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    return true;
                }
                return false;
            }
        });

        ViewTreeObserver observer1 = to.getViewTreeObserver();
        observer1.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Log.v("searchLay.getWidth()", "searchLay.getWidth()==" + toLayout.getWidth());
                if (toLayout.getWidth() > 0) {
                    to.setDropDownWidth(toLayout.getWidth());
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
                        toLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    } else {
                        toLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    }
                }
            }
        });
    }

    private void setLocation() {
            if (flat == 0 && flon == 0) {
                if (gps.canGetLocation()) {
                    if (Starautoassist_Application.isNetworkAvailable(Towing_Activity.this)) {
                        flat = gps.getLatitude();
                        flon = gps.getLongitude();

                        Log.v("lati", "lat" + flat);
                        Log.v("longi", "longi" + flon);
                    } else {

                    }
                } else {
                    if (!alertDialog.isShowing()) {
                        alertDialog.show();
                    }
                    locationfound = true;
                }
            }
            mapFragment.getMapAsync(this);
        }




    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fromset:
                flat=center.latitude;
                flon=center.longitude;
                new GetLocationAsync(center.latitude, center.longitude,"from").execute();
                break;
            case R.id.toset:
                tlat=center.latitude;
                tlon=center.longitude;
                new GetLocationAsync(center.latitude, center.longitude,"to").execute();
                break;

            case R.id.my_location:
                if (gps.canGetLocation()) {
                    mapFragment.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(GoogleMap googleMap) {
                          googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new
                                    LatLng(gps.getLatitude(), gps.getLongitude()), 15));
                        }
                    });

                } else {
                    if (!alertDialog.isShowing()) {
                        alertDialog.show();
                    }
                    locationfound = true;
                }
                break;
            case R.id.bottomLay:
                    if(from.getText().toString().trim().equals("")||to.getText().toString().equals("")){
                        Toast.makeText(this, "Please set locations on corresponding fields", Toast.LENGTH_SHORT).show();
                    }else if(flat==0.0 || flon==0.0 ){
                        Toast.makeText(Towing_Activity.this, "Pickup location not set properly", Toast.LENGTH_SHORT).show();
                    }
                    else if(tlat==0.0 || tlon==0.0 ){
                        Toast.makeText(Towing_Activity.this, "Pickup location not set properly", Toast.LENGTH_SHORT).show();
                    }/*else {

                        Log.d(TAG, "F.Lat&Lon: "+flat+" "+flon);
                        Log.d(TAG, "T.Lat&Lon: "+tlat+" "+tlon);
                        Log.d(TAG, "From Towing_Activity: "+from.getText().toString().trim());
                        Log.d(TAG, "To Towing_Activity: "+to.getText().toString().trim());
                        new Towing_Request_Async(Towing_Activity.this,tow_type).execute();
                    }*/
                else{
                        final Dialog dialog = new Dialog(Towing_Activity.this);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialog.setContentView(R.layout.bill_page_dialog);
                        TextView brandname = (TextView) dialog.findViewById(R.id.brandval);
                        TextView modelname = (TextView) dialog.findViewById(R.id.modelval);
                        TextView sname = (TextView) dialog.findViewById(R.id.servicename);
                        TextView samount = (TextView) dialog.findViewById(R.id.serviceamt);
                        TextView total = (TextView) dialog.findViewById(R.id.totalvalue);
                        TextView des = (TextView) dialog.findViewById(R.id.towdes);
                        des.setVisibility(View.VISIBLE);
                        brandname.setText(brand);
                        modelname.setText(model);
                        sname.setText(R.string.towing);
                        samount.setText(new StringBuilder().append("RM").append(" ").append(amount).toString());
                        total.setText(new StringBuilder().append("RM").append(" ").append(amount).append("  *").toString());
                        Button confirmbtn = (Button) dialog.findViewById(R.id.confirmbtn);
                        Button cancel = (Button) dialog.findViewById(R.id.cancelbtn);
                        confirmbtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Log.d(TAG, "F.Lat&Lon: "+flat+" "+flon);
                                Log.d(TAG, "T.Lat&Lon: "+tlat+" "+tlon);
                                Log.d(TAG, "From Towing_Activity: "+from.getText().toString().trim());
                                Log.d(TAG, "To Towing_Activity: "+to.getText().toString().trim());
                                new Towing_Request_Async(Towing_Activity.this,tow_type).execute();
                            }
                        });
                        cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                        Window window = dialog.getWindow();
                        WindowManager.LayoutParams wlp = window.getAttributes();
                        wlp.gravity = Gravity.CENTER;
                        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
                        window.setAttributes(wlp);
                        dialog.setCancelable(true);
                        dialog.setCanceledOnTouchOutside(false);
                        if (!dialog.isShowing()) {
                            dialog.show();
                        }
                    }
                break;
        }
    }

    @Override
    protected void onResume() {
        Starautoassist_Application.registerReceiver(this);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){

            Toast.makeText(this, "GPS is Enabled in your devide", Toast.LENGTH_SHORT).show();
            gps = new GPSTracker(Towing_Activity.this);
            if (flat == 0 && flon == 0) {
                if (gps.canGetLocation()) {
                    if (Starautoassist_Application.isNetworkAvailable(Towing_Activity.this)) {
                        flat = gps.getLatitude();
                        flon = gps.getLongitude();
                        Log.v("lati", "lat" + flat);
                        Log.v("longi", "longi" + flon);
                    } else {

                    }
                } else {
                    locationfound = true;
                }
            }mapFragment.getMapAsync(this);
        }else{
            if (!alertDialog.isShowing()) {
                alertDialog.show();
            }
        }
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(Towing_Activity.this,HomeActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapFragment.onDestroy();
    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapFragment.onLowMemory();
    }
    @Override
    protected void onPause() {
        super.onPause();
        // For Internet checking disconnect
        Starautoassist_Application.unregisterReceiver(this);
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    if(s.length()==0){
        setTo.setEnabled(true);
        setTo.setTextColor(getResources().getColor(R.color.green));
    }else {
        setTo.setEnabled(false);
        setTo.setTextColor(getResources().getColor(R.color.gray));
    }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        googleMap.setMyLocationEnabled(true);
        googleMap.setTrafficEnabled(true);
        googleMap.setIndoorEnabled(true);
        googleMap.setBuildingsEnabled(true);
        googleMap.getUiSettings().setCompassEnabled(false);
        Log.d(TAG, "onMapReady: "+flat+ "  " +flon);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(flat,flon),11));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(flat, flon), 15));
        center=googleMap.getCameraPosition().target;
        googleMap.setOnCameraMoveStartedListener(new GoogleMap.OnCameraMoveStartedListener() {
            @Override
            public void onCameraMoveStarted(int i) {
                mTimerIsRunning = true;
            }
        });
        googleMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                center = googleMap.getCameraPosition().target;
              //  mZoom = googleMap.getCameraPosition().zoom;
            }
        });
    }


    /** for get the address from lat, lon **/
    private class GetLocationAsync extends AsyncTask<String, Void, String> {

        // boolean duplicateResponse;
        double x, y;
        String locationtype;
        StringBuilder str;

        public GetLocationAsync(double latitude, double longitude,String locationtype) {
            x = latitude;
            this.locationtype=locationtype;
            y = longitude;
        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... params) {

            try {
                geocoder = new Geocoder(Towing_Activity.this, getResources().getConfiguration().locale);
                addresses = geocoder.getFromLocation(x, y, 1);
                str = new StringBuilder();
                if (Geocoder.isPresent() && addresses.size() > 0) {

                    Address returnAddress = addresses.get(0);

                    String localityString = returnAddress.getLocality();
                    String city = returnAddress.getCountryName();
                    String region_code = returnAddress.getCountryCode();
                    String zipcode = returnAddress.getPostalCode();

                    str.append(localityString + "");
                    str.append(city + "" + region_code + "");
                    str.append(zipcode + "");

                } else {
                }
            } catch (IOException e) {
                Log.e("tag", e.getMessage());
            }
            return null;

        }

        @Override
        protected void onPostExecute(String result) {
            try {
                if (addresses != null && !addresses.isEmpty()) {
                    if(locationtype.equals("from")) {
                        fromlocation = addresses.get(0).getAddressLine(0) + ", " + addresses.get(0).getAddressLine(1) + ", " + addresses.get(0).getCountryName();
                        from.setText(fromlocation);
                    }
                    else {
                        tolocation = addresses.get(0).getAddressLine(0) + ", " + addresses.get(0).getAddressLine(1) + ", " + addresses.get(0).getCountryName();
                        to.setText(tolocation);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {

        }
    }
    /** for get the lat, lon from address **/
    class getLocationFromString extends AsyncTask<String, Void, Double[]> {
       String where;
        public getLocationFromString(String where) {
            this.where=where;
        }
                    @Override
        protected Double[] doInBackground(String... params) {
            final Double latn[] = new Double[2];
            HttpURLConnection conn = null;
            StringBuilder jsonResults = new StringBuilder();
            try {
                StringBuilder sb = new StringBuilder("http://maps.google.com/maps/api/geocode/json");
                sb.append("?address=" + URLEncoder.encode(params[0], "utf8"));
                sb.append("&ka&sensor=false");
                sb.append("&language="+ getResources().getConfiguration().locale.getLanguage());
                URL url = new URL(sb.toString());

                Log.v("MAP URL", "" + url);

                conn = (HttpURLConnection) url.openConnection();
                InputStreamReader in = new InputStreamReader(conn.getInputStream());

                // Load the results into a StringBuilder
                int read;
                char[] buff = new char[1024];
                while ((read = in.read(buff)) != -1) {
                    jsonResults.append(buff, 0, read);
                }
            } catch (MalformedURLException e) {
                Log.e("Error", "Error processing Places API URL", e);
                return latn;
            } catch (IOException e) {
                Log.e("Error", "Error connecting to Places API", e);
                return latn;
            } finally {
                if (conn != null) {
                    conn.disconnect();
                }
            }

            try {
                JSONObject jsonObject = new JSONObject(jsonResults.toString());
                Log.v("jsonObject", "jsonObject=" + jsonObject);
                latn[1] = ((JSONArray) jsonObject.get("results")).getJSONObject(0)
                        .getJSONObject("geometry").getJSONObject("location")
                        .getDouble("lng");
                latn[0] = ((JSONArray) jsonObject.get("results")).getJSONObject(0)
                        .getJSONObject("geometry").getJSONObject("location")
                        .getDouble("lat");
                Log.v("lat & lon", " lat = " + latn[0] + " &lon = " + latn[1]);
                if(where.equalsIgnoreCase("from"))
                {
                    flat=latn[0];
                    flon=latn[1];
                    Towing_Activity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mapsync(flat, flon);
                        }
                    });


                }
                if(where.equalsIgnoreCase("to")){
                    tlat=latn[0];
                    tlon=latn[1];
                    Towing_Activity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mapsync(tlat, tlon);
                        }
                    });

                }

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (NullPointerException e){
                e.printStackTrace();
            } catch (Exception e){
                e.printStackTrace();
            }
            return latn;
        }
    }

    public void mapsync(final double lat, final double lon) {
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lon), 11));
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lon), 15));
            }
        });
    }

    public class Towing_Request_Async extends AsyncTask<String, Integer, String> {
        private Context context;
        private String towtype;
        private String url = Constants.BaseURL + Constants.send_req_towing;
        ProgressDialog progress;
        @Nullable
        String user_id;

        public Towing_Request_Async(Context ctx, String towtype) {
            context = ctx;
            this.towtype = towtype;
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
                    .add(Constants.mobileno, GetSet.getMobileno())
                    .add("servicename", "Towing")
                    .add("towtype", towtype)
                    .add("service_amount", amount)
                    .add("brand", brand)
                    .add("model", model)
                    .add("pickup_location", flat+","+flon)
                    .add("drop_location", tlat+","+tlon)
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
                        "success")) {
                   // TODO: request code here
                    Toast.makeText(context,"Request send successfully",Toast.LENGTH_SHORT).show();
                    finish();
                    Intent i = new Intent(Towing_Activity.this, SentRequestActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                }else {
                    Toast.makeText(context,jonj.getString("message"),Toast.LENGTH_SHORT).show();
                    finish();
                    Intent i = new Intent(Towing_Activity.this, HomeActivity.class);
                    startActivity(i);
                }
            }catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

}
