package com.app.starautoassist.Activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.starautoassist.Helper.GPSTracker;
import com.app.starautoassist.Helper.GetSet;
import com.app.starautoassist.Others.Constants;
import com.app.starautoassist.Others.Starautoassist_Application;
import com.app.starautoassist.R;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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

import static com.app.starautoassist.Others.Constants.isLocationpermission_enabled;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    private EditText mobileno,mobilenoforget, etpass,new_password,confirm_password;
    private Button btnlogin, btngoogle, btnfacebook,updatepassbtn;
    private TextView tvforgot, tvcreate;
    // for google plus //
    private static final int RC_SIGN_IN = 9001;
    private GoogleApiClient mGoogleApiClient;
    private boolean mSignInClicked;
    private ProgressDialog mConnectionProgressDialog;
    GPSTracker gps;
    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;
    android.app.AlertDialog alertDialog;
    LocationManager locationManager;
    ScrollView scrollView;
    private AccessToken mAccessToken;
    CallbackManager callbackManager;
    EditText etotpphone,confirmotpcode;
    String mobile_no="";
    Button btnotp,btnconfirm;
    LinearLayout linearLayout,resetpasslayout,fb,google;
    RelativeLayout relativeLayout;
    AccessTokenTracker accessTokenTracker;
    SharedPreferences pref ;
    SharedPreferences.Editor editor;
    BroadcastReceiver receiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        setContentView(R.layout.activity_login);
        pref= getApplicationContext().getSharedPreferences(Constants.SHARED_PREF, 0);
        editor= pref.edit();
        Constants.pref = getApplicationContext().getSharedPreferences("StarAutoAssist",MODE_PRIVATE);
        Constants.editor = Constants.pref.edit();
        relativeLayout= findViewById(R.id.main);
        Starautoassist_Application.setupUI(LoginActivity.this,relativeLayout);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        permissincheck();
        alertDialog = new android.app.AlertDialog.Builder(LoginActivity.this).create();
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
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
           // Toast.makeText(this, "GPS is Enabled in your devide", Toast.LENGTH_SHORT).show();
            Log.d("Login", "GPS is Enabled ");
        }else{
            if (!alertDialog.isShowing()) {
                alertDialog.show();
            }
        }

        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.app.starautoassist",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
        changeStatusBarColor();
        scrollView = findViewById(R.id.loginscroll);
        linearLayout = findViewById(R.id.otplayout);
        resetpasslayout = findViewById(R.id.resetpasswordlayout);
        mobileno = findViewById(R.id.log_et_mobileno);
        etpass = findViewById(R.id.log_et_pass);
        tvcreate = findViewById(R.id.log_tv_create);
        tvforgot = findViewById(R.id.log_tv_forgot);
        btnlogin = findViewById(R.id.log_btn_login);
        mobilenoforget = findViewById(R.id.et_otp_phone);
        new_password = findViewById(R.id.newpass);
        confirm_password = findViewById(R.id.confirmpass);
        updatepassbtn = findViewById(R.id.update_password);
        btnotp = findViewById(R.id.btn_otp);
        fb= findViewById(R.id.fbLay);
        google= findViewById(R.id.gpLay);

        confirmotpcode = findViewById(R.id.confirmotp_code);
        btnconfirm = findViewById(R.id.btn_confirm);
        btnotp.setOnClickListener(this);
        btnconfirm.setOnClickListener(this);
        updatepassbtn.setOnClickListener(this);
        btnlogin.setOnClickListener(this);
        tvcreate.setOnClickListener(this);
        tvforgot.setOnClickListener(this);
        fb.setOnClickListener(this);
        google.setOnClickListener(this);

        loginToFacebook();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(LoginActivity.this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();
        mConnectionProgressDialog = new ProgressDialog(this);
        mConnectionProgressDialog.setMessage("Signing in...");
        mConnectionProgressDialog.setCanceledOnTouchOutside(false);
    }

    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    private void permissincheck() {
        List<String> permissionsNeeded = new ArrayList<String>();
        final List<String> permissionsList = new ArrayList<String>();
        if (!addPermission(permissionsList, Manifest.permission.ACCESS_FINE_LOCATION))
            permissionsNeeded.add("Fine Location");
        if (!addPermission(permissionsList, Manifest.permission.CALL_PHONE))
            permissionsNeeded.add("Phone call");
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
        new AlertDialog.Builder(LoginActivity.this)
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
                perms.put(Manifest.permission.CALL_PHONE, PackageManager.PERMISSION_GRANTED);

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
                        && perms.get(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(LoginActivity.this, "All Permission Granted",Toast.LENGTH_SHORT).show();
                    isLocationpermission_enabled=true;
                } else {
                    // Permission Denied
                    Toast.makeText(LoginActivity.this, "Some Permission is Denied",Toast.LENGTH_SHORT).show();

                }
            }
            break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }

        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    /** Funtions for login into G+ **/
    private void handleSignInResult(GoogleSignInResult result) {
        Log.d("handleSignInResult", "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            try {
                GoogleSignInAccount acct = result.getSignInAccount();

                String personName = acct.getDisplayName();
                String email = acct.getEmail();
                String id = acct.getId();
                String personPhoto = "";

                if (acct.getPhotoUrl() == null){
                    personPhoto = "";
                } else {
                    personPhoto = acct.getPhotoUrl().toString();
                }

                HashMap<String, String> gplusData = new HashMap<String, String>();

                gplusData.put("type", "google");
                gplusData.put("email", email);
                gplusData.put("firstName", personName);
                gplusData.put("lastName", "");
                gplusData.put("image", personPhoto);

                new Social_login(LoginActivity.this,gplusData).execute();

                Log.v("personName", "personName" + personName);
                Log.v("personPhoto", "personPhoto" + personPhoto);
                Log.v("email", "email" + email);

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            // Signed out, show unauthenticated UI.
        }
    }

    /**
     * Function to login into facebook
     * */
    @SuppressWarnings("deprecation")
    public void loginToFacebook() {

        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {

                        GraphRequest request = GraphRequest.newMeRequest(
                                loginResult.getAccessToken(),
                                new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(
                                            JSONObject profile,
                                            GraphResponse response) {
                                        final HashMap<String, String> fbdata = new HashMap<String, String>();
                                        Log.v("json", "object" + profile);
                                        // Application code
                                        try {
                                            if (profile.has("email")) {
                                                // getting name of the user
                                                final String name = profile.getString("name");

                                                // getting email of the user
                                                final String email = profile.getString("email");

                                                // getting userId of the user
                                                final String userId = profile.getString("id");

                                                // getting firstName of the user
                                                final String firstName = profile.getString("first_name");

                                                // getting lastName of the user
                                                final String lastName = profile.getString("last_name");

                                                URL image_value = null;
                                                try {
                                                    image_value = new URL("http://graph.facebook.com/" + userId + "/picture?type=large");

                                                } catch (MalformedURLException e) {
                                                    e.printStackTrace();
                                                }


                                                fbdata.put("type", "facebook");
                                                fbdata.put("id", userId);
                                                fbdata.put("email", email);
                                                fbdata.put("firstName", firstName);
                                                fbdata.put("lastName", lastName);
                                                fbdata.put("image", "http://graph.facebook.com/" + userId + "/picture?type=large");
                                                Log.v("fbdata", "" + fbdata);
                                                LoginActivity.this.runOnUiThread(new Runnable() {

                                                    @SuppressWarnings("unchecked")
                                                    @Override
                                                    public void run() {
                                                        if (mConnectionProgressDialog != null && mConnectionProgressDialog.isShowing()) {
                                                            mConnectionProgressDialog.dismiss();
                                                        }

                                                        new Social_login(LoginActivity.this,fbdata).execute();
                                                    }
                                                });
                                            }else{
                                                Toast.makeText(LoginActivity.this, "Please check your Facebook permissions",Toast.LENGTH_SHORT).show();
                                            }

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id,name,email,first_name,last_name");
                        request.setParameters(parameters);
                        request.executeAsync();
                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(LoginActivity.this, "Facebook - Cancelled", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(FacebookException error) {
                        Toast.makeText(LoginActivity.this, "Facebook - " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        if (error instanceof FacebookAuthorizationException) {
                            if (com.facebook.AccessToken.getCurrentAccessToken() != null) {
                                LoginManager.getInstance().logOut();
                            }
                        }
                    }
                });
                    }





    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.v("onConnectionFailed", "onConnectionFailed");
        if (mSignInClicked) {
            mConnectionProgressDialog.dismiss();
            // The user has already clicked 'sign-in' so we attempt to resolve all
            // errors until the user is signed in, or they cancel.
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.log_btn_login:
                String phone = mobileno.getText().toString();
                String pass = etpass.getText().toString();
                if(!phone.equalsIgnoreCase("")&& !pass.equalsIgnoreCase(""))
                    new Login_Async(this,phone,pass).execute();
                else if(phone.equalsIgnoreCase(""))
                    Toast.makeText(LoginActivity.this,"Enter mobile number",Toast.LENGTH_SHORT).show();
                else Toast.makeText(LoginActivity.this,"Enter password",Toast.LENGTH_SHORT).show();

                break;
            case R.id.log_tv_create:
                Intent registerIntent = new Intent(this, RegisterActivity.class);
                startActivity(registerIntent);
                break;
            case R.id.update_password:
                new Forget_Password(LoginActivity.this,mobile_no,confirm_password.getText().toString().trim()).execute();
                break;
            case R.id.btn_otp:
                if(!mobilenoforget.getText().toString().equals("")&&mobilenoforget.getText().toString().length()>6) {
                    mobile_no = mobilenoforget.getText().toString().trim();
                     new GetOTP(LoginActivity.this, mobile_no).execute();
                    //Toast.makeText(getApplicationContext(),"Otp sent to your mobile successfully",Toast.LENGTH_SHORT).show();
                    try {
                        mobilenoforget.setVisibility(View.GONE);
                        btnotp.setVisibility(View.GONE);
                        confirmotpcode.setVisibility(View.VISIBLE);
                        btnconfirm.setVisibility(View.VISIBLE);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else Toast.makeText(LoginActivity.this,"Check your no",Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_confirm:
                String otp = confirmotpcode.getText().toString().trim();
                  new VerifyOTP(LoginActivity.this, mobile_no, otp).execute();
               // new VerifyOTP(LoginActivity.this, mobile_no, "1234").execute();
                break;
            case R.id.log_tv_forgot:
                scrollView.setVisibility(View.GONE);
                linearLayout.setVisibility(View.VISIBLE);
                break;
            case R.id.fbLay:
//                if(Constants.pref.getBoolean("isLogged",false)) {
//                   Intent intent=new Intent(this,HomeActivity.class);
//                   startActivity(intent);
//                   finish();
//                }else {

                    LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email"));
             //   }
                break;
            case R.id.gpLay:
                mSignInClicked = true;
                mConnectionProgressDialog.show();
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);
                break;
        }

    }

    @Override
    public void onBackPressed() {
        if ((scrollView.getVisibility() == View.GONE) && (linearLayout.getVisibility() == View.VISIBLE)) {
            linearLayout.setVisibility(View.GONE);
            scrollView.setVisibility(View.VISIBLE);
        } else {

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(LoginActivity.this);
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
        }
    }
    public class GetOTP extends AsyncTask<String, Integer, String> {
        private Context context;
        private String mobileno;
        private String url = Constants.BaseURL + Constants.getOTP;
        ProgressDialog progress;
        @Nullable
        String user_id;

        public GetOTP(Context ctx, String mobileno) {
            context = ctx;
            this.mobileno = mobileno;
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
                    .add(Constants.mobileno, mobileno)
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
                    mobile_no=mobileno;
                    //Toast.makeText(getApplicationContext(),"Otp sent to your mobile successfully",Toast.LENGTH_SHORT).show();
                    AlertDialog.Builder confirmotpbuilder = new AlertDialog.Builder(getApplicationContext());
                    LayoutInflater confirmotpinflater;
                    confirmotpinflater = getLayoutInflater();
                    View confirmotpview = confirmotpinflater.inflate(R.layout.confirm_otp_dialog, null);
                    confirmotpbuilder.setView(confirmotpview);
                    confirmotpbuilder.setTitle("Please wait for Confirmation !");
                    confirmotpbuilder.setCancelable(false);
                    final AlertDialog confirmotpdialog = confirmotpbuilder.create();
                    confirmotpdialog.show();
                    final EditText confirmotpcode = confirmotpdialog.findViewById(R.id.confirmotp_code);
                    Button btnconfirm = confirmotpdialog.findViewById(R.id.btn_confirm);
                    receiver = new BroadcastReceiver() {
                        @Override
                        public void onReceive(Context context, Intent intent) {
                            if (intent.getAction().equalsIgnoreCase("otp")) {
                                final String message = intent.getStringExtra("message");
                                confirmotpcode.setText(message);
                                //Do whatever you want with the code here
                            }
                        }
                    };
                    btnconfirm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String otp = confirmotpcode.getText().toString().trim();
                             new VerifyOTP(getApplicationContext(), mobile_no, otp).execute();
                            confirmotpdialog.dismiss();
                           // new VerifyOTP(LoginActivity.this, mobile_no, "1234").execute();

                        }
                    });

                } else {
                    Toast.makeText(LoginActivity.this, jonj.getString("message").trim(), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    public class VerifyOTP extends AsyncTask<String, Integer, String> {
        private Context context;
        private String mobileno, otp;
        private String url = Constants.BaseURL + Constants.verifyOTP;
        @Nullable
        String user_id;

        public VerifyOTP(Context ctx, String mobileno, String otp) {
            context = ctx;
            this.mobileno = mobileno;
            this.otp = otp;
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
                    .add(Constants.otp, otp)
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
                    scrollView.setVisibility(View.GONE);
                    linearLayout.setVisibility(View.GONE);
                    resetpasslayout.setVisibility(View.VISIBLE);
                }else Toast.makeText(getApplicationContext(),"Otp not verified",Toast.LENGTH_SHORT).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    public class Login_Async extends AsyncTask<String, Integer, String> {
        private Context context;
        private String username, password;
        private String url = Constants.BaseURL + Constants.login;
        ProgressDialog progress;
        @Nullable
        String user_id;

        public Login_Async(Context ctx, String username, String password) {
            context = ctx;
            this.username = username;
            this.password = password;
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
                    .add(Constants.password, password)
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

                        String data = jonj.getString("message");
                        JSONArray array = new JSONArray(data);
                        JSONObject jcat = array.getJSONObject(0);
                        GetSet.setIsLogged(true);
                        GetSet.setClientid(jcat.getString("client_id"));
                        GetSet.setEmail(jcat.getString("email"));
                        GetSet.setUser_type(jcat.getString("type"));
                        GetSet.setFirstname(jcat.getString("firstname"));
                        GetSet.setLastname(jcat.getString("lastname"));
                        GetSet.setAddress(jcat.getString("address"));
                        GetSet.setMobileno(jcat.getString("mobileno"));
                        GetSet.setImagename(jcat.getString("userimage"));
                        GetSet.setImageUrl(jcat.getString("userimageurl"));
                        GetSet.setSocialimg(jcat.getString("socialimage"));


                        Constants.editor.putBoolean("isLogged", true);
                        Constants.editor.putString("client_id", GetSet.getClientid());
                        Constants.editor.putString("type", GetSet.getUser_type());
                        Constants.editor.putString("firstname", GetSet.getFirstname());
                        Constants.editor.putString("lastname", GetSet.getLastname());
                        Constants.editor.putString("email", GetSet.getEmail());
                        Constants.editor.putString("mobileno", GetSet.getMobileno().trim());
                        Constants.editor.putString("address", GetSet.getAddress());
                        Constants.editor.putString("userimage", GetSet.getImagename());
                        Constants.editor.putString("userimageurl", GetSet.getImageUrl());
                        Constants.editor.putString("socialimage", GetSet.getSocialimg());
                        Constants.editor.commit();
                        SharedPreferences pref = getApplicationContext().getSharedPreferences(Constants.SHARED_PREF, 0);
                        Boolean isPushed = pref.getBoolean("isPushregisters", false);
                        if (!isPushed)
                            Registernotifi();
                        Intent intent = new Intent(context, HomeActivity.class);
                        startActivity(intent);
                        finish();
                    } else
                        Toast.makeText(context, jonj.getString("message"), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
        }
        }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Starautoassist_Application.freeMemory();
    }
    public class Forget_Password extends AsyncTask<String, Integer, String> {
        private Context context;
        private String mobileno,password;
        private String url = Constants.BaseURL + Constants.forgotpassword;

        public Forget_Password(Context ctx, String mobileno,String password) {
            context = ctx;
            this.mobileno = mobileno;
            this.password = password;
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
                    .add("newpassword", password)
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
                    Intent intent=new Intent(context,LoginActivity.class);
                    startActivity(intent);
                    finish();
                    Toast.makeText(getApplicationContext(),"Password changed successfully",Toast.LENGTH_SHORT).show();

                }else Toast.makeText(getApplicationContext(),jonj.getString("message"),Toast.LENGTH_SHORT).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // For Internet checking
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter("otp"));
        Starautoassist_Application.registerReceiver(LoginActivity.this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // For Internet disconnect checking
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
        Starautoassist_Application.unregisterReceiver(LoginActivity.this);
    }

    public class Social_login extends AsyncTask<String, Integer, String> {
        private Context context;
        private String url = Constants.BaseURL + Constants.socialreg;
        String img="";
        HashMap<String,String> smap;
        @Nullable
        String user_id;

        public Social_login(Context ctx,HashMap<String,String> smap) {
            context = ctx;
            this.smap=smap;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
              mConnectionProgressDialog.show();
        }

        @Nullable
        @Override
        protected String doInBackground(String... params) {
            String jsonData = null;
            Response response = null;
            OkHttpClient client = new OkHttpClient();
            RequestBody body = new FormBody.Builder()
                    .add(Constants.type, smap.get("type"))
                    .add(Constants.firstname, smap.get("firstName"))
                    .add(Constants.lastname, smap.get("lastName"))
                    .add(Constants.email, smap.get("email"))
                    .add(Constants.userimage,smap.get("image"))
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
            // progressDialog.dismiss();
            mConnectionProgressDialog.dismiss();
            Log.v("result", "" + jsonData);
            JSONObject jonj = null;
            try {
                jonj = new JSONObject(jsonData);
                if (jonj.getString("status").equalsIgnoreCase("success")||jonj.getString("status").equalsIgnoreCase("already")) {
                    GetSet.setIsLogged(true);
                    String data=jonj.getString("message");
                    JSONArray array=new JSONArray(data);
                    JSONObject jcat = array.getJSONObject(0);
                    GetSet.setIsLogged(true);
                    GetSet.setClientid(jcat.getString("client_id"));
                    GetSet.setEmail(jcat.getString("email"));
                    GetSet.setUser_type(jcat.getString("type"));
                    GetSet.setFirstname(jcat.getString("firstname"));
                    GetSet.setLastname(jcat.getString("lastname"));
                    GetSet.setAddress(jcat.getString("address"));
                    GetSet.setMobileno(jcat.getString("mobileno"));
                    GetSet.setImagename(jcat.getString("userimage"));
                    GetSet.setImageUrl(jcat.getString("userimageurl"));
                    GetSet.setSocialimg(jcat.getString("socialimage"));


                    Constants.editor.putBoolean("isLogged", true);
                    Constants.editor.putString("client_id", GetSet.getClientid());
                    Constants.editor.putString("firstname", GetSet.getFirstname());
                    Constants.editor.putString("lastname", GetSet.getLastname());
                    Constants.editor.putString("email", GetSet.getEmail());
                    Constants.editor.putString("type", GetSet.getUser_type());
                    Constants.editor.putString("mobileno", GetSet.getMobileno());
                    Constants.editor.putString("address", GetSet.getAddress());
                    Constants.editor.putString("userimage", GetSet.getImagename());
                    Constants.editor.putString("userimageurl", GetSet.getImageUrl());
                    Constants.editor.putString("socialimage", GetSet.getSocialimg());
                    Constants.editor.commit();
                    Intent intent=new Intent(context,HomeActivity.class);
                    startActivity(intent);
                    finish();


                }else
                {
                    Toast.makeText(getApplicationContext(),jonj.getString("message"),Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }
    /**  For register push notification **/
    public void  Registernotifi(){
        Starautoassist_Application aController = null;
        Constants.REGISTER_ID=FirebaseInstanceId.getInstance().getToken();
        Log.v("enetered push","registerid="+Constants.REGISTER_ID);
        editor.putString("regId", Constants.REGISTER_ID);
        editor.commit();

        if(Constants.REGISTER_ID!="" || !Constants.REGISTER_ID.equals("")){
            if(!pref.getString("regId","").equalsIgnoreCase("")) {
                aController = (Starautoassist_Application) getApplicationContext();
                Log.i("Login", "Device registered: regId = " + Constants.REGISTER_ID);
                aController.register(getApplicationContext());
                Log.e("Login", "sendRegistrationToServer: " + Constants.REGISTER_ID);
            }else Log.d("Login", "Push id already registered");
        }
    }
    }





