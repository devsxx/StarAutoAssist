package com.app.starautoassist.Activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.starautoassist.Helper.GPSTracker;
import com.app.starautoassist.Helper.GetSet;
import com.app.starautoassist.Helper.ResultDelegate;
import com.app.starautoassist.Helper.ResultPaymentMessage;
import com.app.starautoassist.Others.Constants;
import com.app.starautoassist.Others.Starautoassist_Application;
import com.app.starautoassist.R;
import com.ipay.IPayIH;
import com.ipay.IPayIHPayment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.app.starautoassist.Others.Constants.resultTitle;
import static com.app.starautoassist.Others.Starautoassist_Application.checkLocationPermission;

public class JumpstartActivity extends AppCompatActivity {

    private TextView jsdes,jsdes1, tvcharge;
    private Button btnsend;
    GPSTracker gpsTracker;
    String ref;
    String amount="",model="",brand="";
    Double lat=0.0,lon=0.0;
    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;
    String latlon;
    RelativeLayout resultLay,parentLay;
    TextView resultval,okbutton;
    ImageView resulimage;
    public  final String DATA = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public Random RANDOM = new Random();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jumpstart);

        tvcharge = findViewById(R.id.jumpstart_tv_charge);
        btnsend = findViewById(R.id.jumpstart_btn_send);
        jsdes = findViewById(R.id.jsdescription);
        jsdes1 = findViewById(R.id.jsdescription1);
        parentLay=findViewById(R.id.jumpparent);
        resultLay=findViewById(R.id.resultlayjump);
        resultval=findViewById(R.id.resulttextvalue);
        resulimage=findViewById(R.id.resultpage);
        okbutton=findViewById(R.id.okbtn);
        jsdes.setText(Html.fromHtml(getString(R.string.jumpstartdes)));
        jsdes1.setText(Html.fromHtml(getString(R.string.jumpstartdes1)));
        setlocation();
        if(getIntent().hasExtra("service_chrg")) {
            amount= getIntent().getStringExtra("service_chrg");
            brand= getIntent().getStringExtra("brand");
            model= getIntent().getStringExtra("model");
        }
        btnsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(lat==0.0 && lon==0.0) {
                    Toast.makeText(JumpstartActivity.this, "Please gives us persmission to find location", Toast.LENGTH_SHORT).show();
                    permissincheck();
                    setlocation();
                }else if(lat==0.0 || lon==0.0){
                    Toast.makeText(JumpstartActivity.this, "Location not available please try again", Toast.LENGTH_SHORT).show();
                    setlocation();
                }else{
                    final Dialog dialog = new Dialog(JumpstartActivity.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.setContentView(R.layout.bill_page_dialog);
                    TextView brandname = (TextView) dialog.findViewById(R.id.brandval);
                    TextView modelname = (TextView) dialog.findViewById(R.id.modelval);
                    TextView sname = (TextView) dialog.findViewById(R.id.servicename);
                    TextView samount = (TextView) dialog.findViewById(R.id.serviceamt);
                    TextView total = (TextView) dialog.findViewById(R.id.totalvalue);
                    brandname.setText(brand);
                    modelname.setText(model);
                    sname.setText(R.string.jump_start);
                    samount.setText(new StringBuilder().append("RM").append(" ").append(amount).toString());
                    total.setText(new StringBuilder().append("RM").append(" ").append(amount).append("  *").toString());
                    Button confirmbtn = (Button) dialog.findViewById(R.id.confirmbtn);
                    Button cancel = (Button) dialog.findViewById(R.id.cancelbtn);
                    confirmbtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                IPayIHPayment payment = new IPayIHPayment();
                                payment.setMerchantCode(Constants.MerchantCode);
                                payment.setMerchantKey(Constants.MerchantKey);
                                payment.setPaymentId("");
                                payment.setCurrency(Constants.Currency);
                                ref=randomString(10);
                                payment.setRefNo(ref);
                                //payment.setAmount(amount);
                                payment.setAmount("1.0");

                                payment.setProdDesc("Jumpstart Service fee");
                                payment.setUserName(GetSet.getUserName());
                                payment.setUserEmail(GetSet.getEmail());
                                payment.setUserContact(GetSet.getMobileno());
                                payment.setRemark("");
                                payment.setLang(Constants.Lang);
                                payment.setCountry(Constants.Country);
                                payment.setBackendPostURL(Constants.backendPostURL);
                                payment.setResponseURL(Constants.responsetURL);


                                Intent selectionIntent = IPayIH.getInstance().checkout(payment
                                        , JumpstartActivity.this,new ResultDelegate(), IPayIH.PAY_METHOD_CREDIT_CARD);
                                startActivityForResult(selectionIntent, 1);
                                dialog.dismiss();
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }

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
            }
        });

        tvcharge.setText(amount);
    }
    public  String randomString(int len) {
        StringBuilder sb = new StringBuilder(len);

        for (int i = 0; i < len; i++) {
            sb.append(DATA.charAt(RANDOM.nextInt(DATA.length())));
        }

        return sb.toString();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode != 1 || data == null) {
            return;
        }

        ResultPaymentMessage resultPaymentMessage = new ResultPaymentMessage();

        if (resultTitle != null) {
            if (resultTitle.equalsIgnoreCase("SUCCESS"))
            {
                new Payment_Async(JumpstartActivity.this).execute();
                parentLay.setVisibility(View.GONE);
                resultLay.setVisibility(View.VISIBLE);
                resulimage.setImageDrawable(getResources().getDrawable(R.drawable.payment_success));
                resultval.setText("Your Transaction Id is: "+GetSet.getTransid());
                okbutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new Jumpstart_Request_Async(JumpstartActivity.this).execute();
                    }
                });
            }else {
                parentLay.setVisibility(View.GONE);
                resultLay.setVisibility(View.VISIBLE);
                resulimage.setImageDrawable(getResources().getDrawable(R.drawable.payment_failed));
                resultval.setTextColor(getResources().getColor(R.color.white));
                resultval.setText("Failed due to: "+GetSet.getErrDes());
                okbutton.setText("OK");
                okbutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                    Intent intent=new Intent(JumpstartActivity.this,HomeActivity.class);
                    startActivity(intent);
                    finish();
                    }
                });
            }
            resultTitle = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // For Internet checking
        Starautoassist_Application.registerReceiver(JumpstartActivity.this);
    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Starautoassist_Application.freeMemory();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // For Internet disconnect checking
        Starautoassist_Application.unregisterReceiver(JumpstartActivity.this);
    }

    private void setlocation() {
        gpsTracker=new GPSTracker(this);
        lat=gpsTracker.getLatitude();
        lon=gpsTracker.getLongitude();
        latlon=lat.toString().trim()+lon.toString().trim();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(JumpstartActivity.this,HomeActivity.class);
        startActivity(intent);
        finish();
    }

    public class Jumpstart_Request_Async extends AsyncTask<String, Integer, String> {
        private Context context;
        private String url = Constants.BaseURL + Constants.send_req_jumpstart;
        ProgressDialog progress;
        @Nullable
        String user_id;

        public Jumpstart_Request_Async(Context ctx) {
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
                    .add("servicename", "Jump Start")
                    .add("service_amount", amount)
                    .add("brand", brand)
                    .add("model", model)
                    .add("client_location", lat+","+lon)
                    .add("transid", GetSet.getTransid())
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
                    GetSet.setTransid(null);
                    Toast.makeText(context,"Request send successfully",Toast.LENGTH_SHORT).show();
                    finish();
                    Intent i = new Intent(JumpstartActivity.this, SentRequestActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                }else {
                    Toast.makeText(context,jonj.getString("message"),Toast.LENGTH_SHORT).show();
                    finish();
                    Intent i = new Intent(JumpstartActivity.this, HomeActivity.class);
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
        new AlertDialog.Builder(JumpstartActivity.this)
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
                return shouldShowRequestPermissionRationale(permission);
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
                    Toast.makeText(JumpstartActivity.this, "All Permission Granted",Toast.LENGTH_SHORT).show();
                } else {
                    // Permission Denied
                    Toast.makeText(JumpstartActivity.this, "Some Permission is Denied",Toast.LENGTH_SHORT).show();
                }
            }
            break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
    public class Payment_Async extends AsyncTask<String, Integer, String> {
        private Context context;
        private String url = Constants.BaseURL + Constants.payment_req;
        ProgressDialog progress;
        @Nullable
        String user_id;

        public Payment_Async(Context ctx) {
            context = ctx;
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
            GetSet.setMobileno(Constants.pref.getString("mobileno",""));
            OkHttpClient client = new OkHttpClient();
            RequestBody body = new FormBody.Builder()
                    .add(Constants.client_id, GetSet.getMobileno())
                    .add(Constants.client_name, Constants.pref.getString("firstname",""))
                    .add(Constants.amount, amount)
                    .add(Constants.refno, ref)
                    .add(Constants.des, "Jumpstart service fee")
                    .add(Constants.email, Constants.pref.getString("email",""))
                    .add(Constants.transid, GetSet.getTransid())
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
                    Toast.makeText(context,"Transaction saved successfully",Toast.LENGTH_SHORT).show();

                }else {
                    Toast.makeText(context,jonj.getString("message"),Toast.LENGTH_SHORT).show();
                    finish();
                    Intent i = new Intent(JumpstartActivity.this, HomeActivity.class);
                    startActivity(i);
                }
            }catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
