package com.app.starautoassist.Activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.app.starautoassist.Helper.GPSTracker;
import com.app.starautoassist.Helper.GetSet;
import com.app.starautoassist.Others.Constants;
import com.app.starautoassist.Others.Starautoassist_Application;
import com.app.starautoassist.R;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Fuel_Activity extends AppCompatActivity {

   /* private MapView mapView;*/
    private Spinner spinnerprice, spinnerfuel;
    private TextView tvlitre;
    private Button btnproceed;
    GPSTracker gpsTracker;
    List<String> avail_amount=new ArrayList<String>();
    List<String> fuletype=new ArrayList<String>();
    List<String> perltr=new ArrayList<String>();
    String fuelprice;
    Double lat=0.0,lon=0.0;
    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;
    String amount="";
    GPSTracker gps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Fuel Service");
        setContentView(R.layout.activity_fuel);
        new Get_Fuel_Service(Fuel_Activity.this).execute();
        /*mapView = findViewById(R.id.mapView);*/
        spinnerprice = findViewById(R.id.spin_price);
        spinnerfuel = findViewById(R.id.spin_fuel);
        tvlitre = findViewById(R.id.fuel_tv_litre);
        btnproceed = findViewById(R.id.fuel_btn_proceed);

        if(getIntent().hasExtra("service_chrg")) {
            amount= getIntent().getStringExtra("service_chrg");
        }
        btnproceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(lat==0.0 && lon==0.0) {
                    Toast.makeText(Fuel_Activity.this, "Please gives us persmission to find location", Toast.LENGTH_SHORT).show();
                    permissincheck();
                    setlocation();
                }else  new Fuel_Request_Async(Fuel_Activity.this).execute();
            }
        });
        setlocation();
        spinnerfuel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ArrayAdapter<String> adapterprice = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, avail_amount);
                adapterprice.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerprice.setAdapter(adapterprice);
                if(position==0){
                  fuelprice=perltr.get(0);
                }
                else if (fuletype.get(position).equalsIgnoreCase("petrol")){
                     fuelprice = perltr.get(position);
                }else if (fuletype.get(position).equalsIgnoreCase("diesel")){
                    fuelprice = perltr.get(position);
                }else if (fuletype.get(position).equalsIgnoreCase("gas")){
                    fuelprice = perltr.get(position);
                }
                Toast.makeText(getApplicationContext(), spinnerfuel.getSelectedItem().toString() + "\tis Selected", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerprice.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               Float ltr= Float.valueOf(avail_amount.get(position))/Float.valueOf(fuelprice);
               tvlitre.setText(String.format("%.2f",ltr));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setlocation() {
        gpsTracker=new GPSTracker(this);
        lat=gpsTracker.getLatitude();
        lon=gpsTracker.getLongitude();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
    public class Get_Fuel_Service extends AsyncTask<String, Integer, String> {
        private Context context;
        private String url = Constants.BaseURL + Constants.get_fuel_request;
        ProgressDialog progress;
        @Nullable
        String user_id;

        public Get_Fuel_Service(Context ctx) {
            context = ctx;
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
            Request request = new Request.Builder()
                    .url(url)
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
            HashMap<String, String> map;
            JSONObject jonj = null;
            try {
                jonj = new JSONObject(jsonData);
                if (jonj.getString("status").equalsIgnoreCase(
                        "success")) {
                    String data=jonj.getString("data");
                    JSONArray array=new JSONArray(data);
                    fuletype.add(0,"Select Fuel Type");
                    perltr.add(0,"0");
                    for(int i=0;i<array.length();i++) {
                        JSONObject object = array.getJSONObject(i);
                        String type = object.getString("sub_category");
                        String price = object.getString("price");
                        fuletype.add(type);
                        perltr.add(price);
                    }

                    JSONObject obj=array.getJSONObject(1);
                    String amout=obj.getString("rate");
                    avail_amount.add(0,"Select amount");
                    avail_amount = Arrays.asList(amout.split(","));
                    ArrayAdapter<String> adapterfuel = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item,fuletype);
                    adapterfuel.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerfuel.setAdapter(adapterfuel);
                }else  Toast.makeText(context,jonj.getString("message"),Toast.LENGTH_SHORT).show();
            }catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
    public class Fuel_Request_Async extends AsyncTask<String, Integer, String> {
        private Context context;
        private String fueltype;
        private String url = Constants.BaseURL + Constants.send_req_fuel;
        ProgressDialog progress;
        @Nullable
        String user_id;

        public Fuel_Request_Async(Context ctx) {
            context = ctx;
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
                    .add("servicename", "Out of Fuel")
                    .add("service_amount", amount)
                    .add("fueltype", spinnerfuel.getSelectedItem().toString())
                    .add("amount", spinnerprice.getSelectedItem().toString())
                    .add("client_location", lat+","+lon)
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
                    Intent i = new Intent(Fuel_Activity.this, SentRequestActivity.class);
                    startActivity(i);
                }else {
                    Toast.makeText(context,jonj.getString("message"),Toast.LENGTH_SHORT).show();
                    finish();
                    Intent i = new Intent(Fuel_Activity.this, HomeActivity.class);
                    startActivity(i);
                }
            }catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
    private void permissincheck() {
        List<String> permissionsNeeded = new ArrayList<String>();
        final List<String> permissionsList = new ArrayList<String>();
        if (!addPermission(permissionsList, Manifest.permission.ACCESS_FINE_LOCATION))
            permissionsNeeded.add("Fine Location");
        if (!addPermission(permissionsList, Manifest.permission.ACCESS_COARSE_LOCATION))
            permissionsNeeded.add("Coarse Location");
        if (!addPermission(permissionsList, Manifest.permission.READ_EXTERNAL_STORAGE))
            permissionsNeeded.add("Read External Storage");
        if (!addPermission(permissionsList, Manifest.permission.WRITE_EXTERNAL_STORAGE))
            permissionsNeeded.add("Write External Storage");
        if (permissionsList.size() > 0) {
            if (permissionsNeeded.size() > 0) {
                // Need Rationale
                String message = "We need you to grant access to " + permissionsNeeded.get(0);
                for (int i = 1; i < permissionsNeeded.size(); i++)
                    message = message + ", " + permissionsNeeded.get(i);
                showMessageOKCancel(message,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                                            REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                                }
                            }
                        });
                return;
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                        REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
            }
            return;
        }


    }
    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(Fuel_Activity.this)
                .setTitle("Permission Request")
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }
    @TargetApi(Build.VERSION_CODES.M)
    private boolean addPermission(List<String> permissionsList, String permission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsList.add(permission);
                // Check for Rationale Option
                if (!shouldShowRequestPermissionRationale(permission))
                    return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS:
            {
                Map<String, Integer> perms = new HashMap<String, Integer>();
                // Initial
                perms.put(Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.ACCESS_COARSE_LOCATION, PackageManager.PERMISSION_GRANTED);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    perms.put(Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                }
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);

                // Fill with results
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);
                // Check for ACCESS_FINE_LOCATION
                if (perms.get(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(Fuel_Activity.this, "All Permission Granted",Toast.LENGTH_SHORT).show();
                } else {
                    // Permission Denied
                    Toast.makeText(Fuel_Activity.this, "Some Permission is Denied",Toast.LENGTH_SHORT).show();

                }
            }
            break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
