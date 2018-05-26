package com.app.starautoassist.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.app.starautoassist.Helper.GetSet;
import com.app.starautoassist.Others.Constants;
import com.app.starautoassist.R;
import com.mikhaellopez.circularimageview.CircularImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ProfileActivity extends AppCompatActivity {

    private CircularImageView circularImageView;
    private TextView tvphone;
    private EditText etfirstname, etlastname, etaddress;
    private static final int PICK_PICTURE = 1;
    private static final String TAG = "ProfileActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Profile");
        setContentView(R.layout.activity_profile);

        circularImageView = findViewById(R.id.civ_profile);
        tvphone = findViewById(R.id.tv_profile_phone);
        etfirstname = findViewById(R.id.et_profile_firstname);
        etlastname = findViewById(R.id.et_profile_lastname);
        etaddress = findViewById(R.id.et_profile_address);


        circularImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent profilepicintent = new Intent();
                profilepicintent.setType("image/*");
                profilepicintent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(profilepicintent, "Select Picture"), PICK_PICTURE);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_PICTURE && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                // Log.d(TAG, String.valueOf(bitmap));

                CircularImageView imageView =  findViewById(R.id.civ_profile);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public class Get_Profile_Async extends AsyncTask<String, Integer, String> {
        private Context context;
        private String mobileno;
        private String url = Constants.BaseURL + Constants.login;
        ProgressDialog progress;
        @Nullable
        String user_id;

        public Get_Profile_Async(Context ctx, String mobileno) {
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
                    GetSet.setIsLogged(true);
                    GetSet.setImageUrl(jonj.getString("userimage"));
                    GetSet.setFirstname(jonj.getString("firstname"));
                    GetSet.setLastname(jonj.getString("lastname"));
                    GetSet.setMobileno(jonj.getString("mobileno"));
                    GetSet.setEmail(jonj.getString("email"));
                    GetSet.setStreet(jonj.getString("street"));
                    GetSet.setArea(jonj.getString("area"));
                    GetSet.setLat(jonj.getString("lat"));
                    GetSet.setLon(jonj.getString("long"));

                    Constants.editor.putString("userimage", GetSet.getImageUrl());
                    Constants.editor.putString("fname", GetSet.getFirstname());
                    Constants.editor.putString("lname", GetSet.getLastname());
                    Constants.editor.putString("mobileno", GetSet.getMobileno());
                    Constants.editor.putString("email", GetSet.getEmail());
                    Constants.editor.putString("street", GetSet.getStreet());
                    Constants.editor.putString("area", GetSet.getArea());
                    Constants.editor.putString("lat", GetSet.getLat());
                    Constants.editor.putString("lon", GetSet.getLon());
                    Constants.editor.commit();
                    //  Registernotifi();
                    finish();
//                        Intent i = new Intent(LoginActivity.this, FragmentMainActivity.class);
//                        startActivity(i);
                }
            }catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
    public class Edit_Profile_Async extends AsyncTask<String, Integer, String> {
        private Context context;
        private String mobileno;
        private String url = Constants.BaseURL + Constants.login;
        ProgressDialog progress;
        @Nullable
        String user_id;

        public Edit_Profile_Async(Context ctx, String mobileno) {
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
                    .add(Constants.mobileno, GetSet.getMobileno())
                    .add("firstname", GetSet.getMobileno())
                    .add("lastname", GetSet.getMobileno())
                    .add("email", GetSet.getMobileno())
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
                    GetSet.setIsLogged(true);
                    GetSet.setImageUrl(jonj.getString("userimage"));
                    GetSet.setFirstname(jonj.getString("firstname"));
                    GetSet.setLastname(jonj.getString("lastname"));
                    GetSet.setMobileno(jonj.getString("mobileno"));
                    GetSet.setEmail(jonj.getString("email"));
                    GetSet.setStreet(jonj.getString("street"));
                    GetSet.setArea(jonj.getString("area"));

                    Constants.editor.putString("userimage", GetSet.getImageUrl());
                    Constants.editor.putString("fname", GetSet.getFirstname());
                    Constants.editor.putString("lname", GetSet.getLastname());
                    Constants.editor.putString("mobileno", GetSet.getMobileno());
                    Constants.editor.putString("email", GetSet.getEmail());
                    Constants.editor.putString("street", GetSet.getStreet());
                    Constants.editor.putString("area", GetSet.getArea());
                    Constants.editor.commit();
                    //  Registernotifi();
                    finish();
//                        Intent i = new Intent(LoginActivity.this, FragmentMainActivity.class);
//                        startActivity(i);
                }
            }catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
