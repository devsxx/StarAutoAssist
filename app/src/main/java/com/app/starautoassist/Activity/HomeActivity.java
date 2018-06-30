package com.app.starautoassist.Activity;

import android.Manifest;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.starautoassist.Adapter.MyCars_Adapter;
import com.app.starautoassist.Fragment.HomeFragment;
import com.app.starautoassist.Helper.GetSet;
import com.app.starautoassist.Others.Constants;
import com.app.starautoassist.Others.NotificationUtilz;
import com.app.starautoassist.Others.Starautoassist_Application;
import com.app.starautoassist.R;
import com.bumptech.glide.Glide;
import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.firebase.messaging.FirebaseMessaging;
import com.mikhaellopez.circularimageview.CircularImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    String url="";
    private NotificationUtilz notificationUtils;
    Boolean chkper;
    AlertDialog dialog;

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Constants.pref = getApplicationContext().getSharedPreferences("StarAutoAssist",MODE_PRIVATE);
        Constants.editor = Constants.pref.edit();
        getSupportActionBar().setTitle("Star Auto Assist");
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, new HomeFragment());
        fragmentTransaction.commit();
        chkper=checkLocationPermission();
        if(chkper)
            Constants.isLocationpermission_enabled=true;
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerLayout = navigationView.getHeaderView(0);
        CircularImageView imageView= headerLayout.findViewById(R.id.nav_iv);
        if(!Constants.pref.getString("socialimage","").equalsIgnoreCase("")){
            url= Constants.pref.getString("socialimage","");
            Glide
                    .with(HomeActivity.this)
                    .load(url)
                    .into(imageView);
        }
        else if(!Constants.pref.getString("userimage","").equalsIgnoreCase("")){
            url = Constants.BaseURL + "images/users/" + Constants.pref.getString("userimage","");
            Glide
                    .with(HomeActivity.this)
                    .load(url)
                    .into(imageView);
        }  else{
            Glide
                    .with(HomeActivity.this)
                    .load(R.drawable.logo)
                    .into(imageView);
        }
        if(Constants.pref!=null) {
            GetSet.setMobileno(Constants.pref.getString("mobileno", ""));
            GetSet.setEmail(Constants.pref.getString("email", ""));
            GetSet.setUser_type(Constants.pref.getString("type", ""));
            GetSet.setFirstname(Constants.pref.getString("firstname",""));
        }
        TextView username= headerLayout.findViewById(R.id.nav_tv_name);
        username.setText(GetSet.getFirstname());
        navigationView.setNavigationItemSelectedListener(this);

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // checking for type intent filter
                if (intent.getAction().equals(Constants.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(Constants.TOPIC_GLOBAL);
                    displayFirebaseRegId();
                } else if (intent.getAction().equals(Constants.PUSH_NOTIFICATION)) {
                    // new push notification is received
                    String message = intent.getStringExtra("message");
                    Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();
                    notificationUtils = new NotificationUtilz(context);
                    long timeStamp = System.currentTimeMillis();
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    notificationUtils.showNotificationMessage(getString(R.string.app_name), message, String.valueOf(timeStamp), intent);
                }
            }
        };

        displayFirebaseRegId();
    }

    private void displayFirebaseRegId() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Constants.SHARED_PREF, 0);
        String regId = pref.getString("regId", null);
        Log.e("Registered", "Firebase reg id: " + regId);

        if (!TextUtils.isEmpty(regId))
            Log.d("Hoome", "displayFirebaseRegId: "+regId);
        else
            Toast.makeText(getApplicationContext(), "Firebase Reg Id is not received yet!", Toast.LENGTH_LONG).show();
    }



    @Override
    protected void onResume() {
        super.onResume();
        // For Internet checking
        Starautoassist_Application.registerReceiver(HomeActivity.this);
        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(HomeActivity.this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Constants.REGISTRATION_COMPLETE));
        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(HomeActivity.this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Constants.PUSH_NOTIFICATION));
        // clear the notification area when the app is opened
        NotificationUtilz.clearNotifications(getApplicationContext());
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        // For Internet disconnect checking
        Starautoassist_Application.unregisterReceiver(HomeActivity.this);
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (getFragmentManager().getBackStackEntryCount() == 0) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(HomeActivity.this);
                alertDialog.setTitle("Exiting App Confirmation");
                alertDialog.setMessage("Are you sure you want to Exit?");
                alertDialog.setIcon(R.drawable.exit);
                alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(@NonNull DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alertDialog.show();
            }else super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
 /*       if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(final MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Fragment fragment = null;

        if (id == R.id.nav_home) {

            fragment = new HomeFragment();

        } else if (id == R.id.nav_profile) {

            Intent profileintent = new Intent(this, ProfileActivity.class);
            startActivity(profileintent);

        } else if (id == R.id.nav_vechicle) {

            Intent vechicleintent = new Intent(this, VechicleActivity.class);
            startActivity(vechicleintent);

        } else if (id == R.id.nav_acceptedrequest) {

            Intent vechicleintent = new Intent(this, AcceptedRequestActivity.class);
            startActivity(vechicleintent);
        }
        else if (id == R.id.nav_sentrequest) {

            Intent sentrequestintent = new Intent(this, SentRequestActivity.class);
            startActivity(sentrequestintent);

        } else if (id == R.id.nav_servicehistory) {

        /*    Intent servicehistoryintent = new Intent(this, ServiceHistoryActivity.class);
            startActivity(servicehistoryintent);
            finish();*/
            Toast.makeText(this, "This feature will be available soon", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_paymenthistory) {

//            Intent paymenthistoryintent = new Intent(this, PaymentHistoryActivity.class);
//            startActivity(paymenthistoryintent);
//            finish();
            Toast.makeText(this, "This feature will be available soon", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_changepasssword) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            LayoutInflater layoutInflater = this.getLayoutInflater();
            View view = layoutInflater.inflate(R.layout.change_password_dialog, null);

            builder.setView(view);
            builder.setTitle("Reset Your Password :");
            builder.setCancelable(true);

            dialog = builder.create();
            dialog.show();

            final EditText etoldpass = dialog.findViewById(R.id.et_change_oldpass);
            final EditText etnewpass = dialog.findViewById(R.id.et_change_newpass);
            final EditText etconfirmpass = dialog.findViewById(R.id.et_change_confirmpass);
            Button btnchangepass = dialog.findViewById(R.id.btn_changepass);

            btnchangepass.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String oldpass = etoldpass.getText().toString();
                    String newpass = etnewpass.getText().toString();
                    String confirmpass = etconfirmpass.getText().toString();

                    if (newpass.equals(confirmpass)){
                        //proceed to further code
                        new Change_Password(HomeActivity.this,GetSet.getMobileno(),oldpass,newpass).execute();


                    }else {

                        Toast.makeText(HomeActivity.this, "Password do not Match!", Toast.LENGTH_SHORT).show();
                    }

                }
            });

        } else if (id == R.id.nav_share) {
            Intent shareintent = new Intent(Intent.ACTION_SEND);
            shareintent.setType("text/plain");
            startActivity(Intent.createChooser(shareintent, "Share Via"));

        } else if (id == R.id.nav_logout) {
            signoutdialog();
        }

        if (fragment != null){

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.content_frame, fragment);
            transaction.commit();
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /** dialog for confirm the user to signout **/
    public void signoutdialog() {
        final Dialog dialog = new Dialog(HomeActivity.this ,R.style.AlertDialog);
        Display display = getWindowManager().getDefaultDisplay();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.default_dialog);
        dialog.getWindow().setLayout(display.getWidth()*90/100, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);

        TextView alertTitle = dialog.findViewById(R.id.alert_title);
        TextView alertMsg = dialog.findViewById(R.id.alert_msg);
        ImageView alertIcon = dialog.findViewById(R.id.alert_icon);
        TextView alertOk = dialog.findViewById(R.id.alert_button);
        TextView alertCancel = dialog.findViewById(R.id.cancel_button);

        alertMsg.setText(getString(R.string.reallySignOut));
        alertOk.setText(getString(R.string.yes));
        alertCancel.setText(getString(R.string.no));

        alertCancel.setVisibility(View.VISIBLE);

        alertOk.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Starautoassist_Application aController = new Starautoassist_Application();
                aController.unregister(HomeActivity.this);
                Constants.editor.clear();
                Constants.editor.commit();
                /*if (AccessToken.getCurrentAccessToken() != null)
                LoginManager.getInstance().logOut();*/
                GetSet.reset();
                dialog.dismiss();
                finish();
                Intent p = new Intent(HomeActivity.this, LoginActivity.class);
                startActivity(p);
            }
        });

        alertCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        if(!dialog.isShowing()){
            dialog.show();
        }
    }
    public class Change_Password extends AsyncTask<String, Integer, String> {
        private Context context;
        private String mobileno, oldpass,newpass;
        private String url = Constants.BaseURL + Constants.changepassword;

        public Change_Password(Context ctx, String mobileno, String oldpass,String newpass) {
            context = ctx;
            this.mobileno = mobileno;
            this.oldpass = oldpass;
            this.newpass = newpass;
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
                    .add(Constants.mobileno, mobileno)
                    .add("oldpassword", oldpass)
                    .add("newpassword", newpass)
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
                    Toast.makeText(getApplicationContext(),"Password changed successfully",Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }else Toast.makeText(getApplicationContext(),jonj.getString("message"),Toast.LENGTH_SHORT).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }



    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(HomeActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(HomeActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new android.app.AlertDialog.Builder(HomeActivity.this)
                        .setTitle("Location Permission")
                        .setMessage("Please give us location permission to perform service request")
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(HomeActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(HomeActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(HomeActivity.this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        Constants.isLocationpermission_enabled=true;
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                }
                return;
            }

        }
    }

    }

