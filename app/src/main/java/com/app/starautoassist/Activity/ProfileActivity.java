package com.app.starautoassist.Activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.starautoassist.Helper.EditProfilePhoto;
import com.app.starautoassist.Helper.GetSet;
import com.app.starautoassist.Helper.Image_Upload;
import com.app.starautoassist.Others.Constants;
import com.app.starautoassist.Others.Starautoassist_Application;
import com.app.starautoassist.R;
import com.bumptech.glide.Glide;
import com.facebook.CallbackManager;
import com.google.firebase.iid.FirebaseInstanceId;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Random;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.graphics.BitmapFactory.decodeFile;

public class ProfileActivity extends AppCompatActivity {

    private CircularImageView circularImageView;
    private MaterialEditText etfirstname, etlastname, etaddress, etphone, etemail,otp;
    String imagepath = "";
    private Boolean upflag = false;
    String fname;
    TextView verifytxt;
    String image = "", imageurl = "";
    Button submitbtn;
    String mobileno = "";
    String uploadedImage = "", viewUrl = "";
    private ProgressDialog pDialog;
    SharedPreferences pref ;
    SharedPreferences.Editor editor;
    private static final String TAG = "ProfileActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Profile");
        setContentView(R.layout.activity_profile);

        circularImageView = findViewById(R.id.civ_profile);
        etphone = findViewById(R.id.tv_profile_phone);
        pref= getApplicationContext().getSharedPreferences(Constants.SHARED_PREF, 0);
        editor= pref.edit();
        verifytxt = findViewById(R.id.verify);
        etfirstname = findViewById(R.id.et_profile_firstname);
        etlastname = findViewById(R.id.et_profile_lastname);
        etaddress = findViewById(R.id.et_profile_address);
        etemail = findViewById(R.id.et_profile_email);
        submitbtn = findViewById(R.id.btn_profile_save);


        Constants.pref = getApplicationContext().getSharedPreferences("StarAutoAssist", MODE_PRIVATE);
        Constants.editor = Constants.pref.edit();

        mobileno = Constants.pref.getString("mobileno", "");
        //Async for getting profile data
        new Get_Profile_Async(ProfileActivity.this).execute();
        circularImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gallerydialog();
            }
        });
        submitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!etphone.getText().toString().trim().equalsIgnoreCase(""))
                    new Edit_Profile_Async(ProfileActivity.this, GetSet.getMobileno()).execute();
                else
                    Toast.makeText(ProfileActivity.this, "Please enter mobile no", Toast.LENGTH_SHORT).show();
            }
        });
        verifytxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new GetOTP(ProfileActivity.this,etphone.getText().toString().trim()).execute();

            }
        });

    }

    public class GetOTP extends AsyncTask<String, Integer, String> {
        private Context context;
        private String mobileno;
        private String url = Constants.BaseURL + Constants.getSocialOTP;
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
                    .add(Constants.email, GetSet.getEmail())
                    .add(Constants.type, GetSet.getUser_type())
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
                    LayoutInflater li = LayoutInflater.from(ProfileActivity.this);
                    View promptsView = li.inflate(R.layout.dialog_window, null);

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                            ProfileActivity.this);

                    // set prompts.xml to alertdialog builder
                    alertDialogBuilder.setView(promptsView);

                    final EditText userInput = (EditText) promptsView
                            .findViewById(R.id.editTextResult);

                    // set dialog message
                    alertDialogBuilder
                            .setCancelable(false)
                            .setPositiveButton("Verify",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,int id) {
                                            String otp=userInput.getText().toString().trim();
                                            new VerifyOTP(ProfileActivity.this, mobileno, otp).execute();
                                        }
                                    })
                            .setNegativeButton("Cancel",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,int id) {
                                            dialog.cancel();
                                        }
                                    });

                    // create alert dialog
                    AlertDialog alertDialog = alertDialogBuilder.create();

                    // show it
                    alertDialog.show();

                } else {
                    Toast.makeText(ProfileActivity.this, jonj.getString("message").trim(), Toast.LENGTH_SHORT).show();
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
                    GetSet.setMobileno(mobileno);
                    Constants.editor.putString("mobileno", GetSet.getMobileno());
                    Constants.editor.commit();
                    etphone.setEnabled(false);
                    verifytxt.setEnabled(false);
                    verifytxt.setText("Verified");
                    verifytxt.setTextColor(getResources().getColor(R.color.green));
                    SharedPreferences pref = getApplicationContext().getSharedPreferences(Constants.SHARED_PREF, 0);
                    Boolean isPushed=pref.getBoolean("isPushregisters",false);
                    if(!isPushed)
                        Registernotifi();
                }else Toast.makeText(getApplicationContext(),"Otp not verified",Toast.LENGTH_SHORT).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    /**  For register push notification **/
    public void  Registernotifi(){
        Starautoassist_Application aController = null;
        Constants.REGISTER_ID= FirebaseInstanceId.getInstance().getToken();
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
    //Setting data on fields from aysnc result
    private void setdata() {
        if (!Constants.pref.getString("userimageurl", "").equalsIgnoreCase("")) {
            image = Constants.pref.getString("userimageurl", "");
            Glide
                    .with(ProfileActivity.this)
                    .load(image)
                    .thumbnail(0.1f)
                    .into(circularImageView);
        } else if (!Constants.pref.getString("socialimage", "").equalsIgnoreCase("")) {
            image = Constants.pref.getString("socialimage", "");
            Glide
                    .with(ProfileActivity.this)
                    .load(image)
                    .thumbnail(0.1f)
                    .into(circularImageView);
        }else if (!Constants.pref.getString("carlogo", "").equalsIgnoreCase("")) {
            image = Constants.pref.getString("carlogo", "");
            Glide
                    .with(ProfileActivity.this)
                    .load(image)
                    .thumbnail(0.1f)
                    .into(circularImageView);
        } else {
            Glide
                    .with(ProfileActivity.this)
                    .load(R.drawable.logo)
                    .thumbnail(0.1f)
                    .into(circularImageView);
        }

        if (GetSet.getMobileno() != null && !GetSet.getMobileno().equalsIgnoreCase("")) {
            etphone.setText(GetSet.getMobileno());
            etphone.setEnabled(false);
            verifytxt.setEnabled(false);
            verifytxt.setText("Verified");
            verifytxt.setTextColor(getResources().getColor(R.color.green));
        } else {
            verifytxt.setEnabled(true);
            etphone.setEnabled(true);
        }
        etfirstname.setText(GetSet.getFirstname());
        etlastname.setText(GetSet.getLastname());
        etemail.setText(GetSet.getEmail());
        etaddress.setText(GetSet.getAddress());
    }


    @Override
    protected void onResume() {
        super.onResume();
        // For Internet checking
        Starautoassist_Application.registerReceiver(ProfileActivity.this);
        if (EditProfilePhoto.editPhoto) {
            EditProfilePhoto.editPhoto = false;
            new UploadImg().execute(EditProfilePhoto.imgPath);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // For Internet disconnect checking
        Starautoassist_Application.unregisterReceiver(ProfileActivity.this);
    }

    /**
     * for user choose the dp from camera or gallery
     **/
    public void gallerydialog() {
        final Dialog dialog = new Dialog(ProfileActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.gallerypopup);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);
        ImageView gal = dialog.findViewById(R.id.galery);
        ImageView cam = dialog.findViewById(R.id.camra);

        cam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProfileActivity.this, EditProfilePhoto.class);
                startActivity(i);
                dialog.dismiss();
            }
        });
        gal.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent in = new Intent(
                                Intent.ACTION_PICK,
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(in, 2);
                        dialog.dismiss();
                    }
                });
        if (!dialog.isShowing()) {
            dialog.show();
        }
    }


    private Bitmap decodeFile(String fPath) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        opts.inDither = false;
        opts.inPurgeable = true;
        opts.inInputShareable = true;
        BitmapFactory.decodeFile(fPath, opts);
        final int REQUIRED_SIZE = 1024;
        int scale = 1;

        if (opts.outHeight > REQUIRED_SIZE || opts.outWidth > REQUIRED_SIZE) {
            final int heightRatio = Math.round((float) opts.outHeight
                    / (float) REQUIRED_SIZE);
            final int widthRatio = Math.round((float) opts.outWidth
                    / (float) REQUIRED_SIZE);
            scale = heightRatio < widthRatio ? heightRatio : widthRatio;//
        }
        opts.inJustDecodeBounds = false;
        opts.inSampleSize = scale;
        Bitmap bm = BitmapFactory.decodeFile(fPath, opts).copy(
                Bitmap.Config.RGB_565, false);
        return bm;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Log.v("RESULT_OK", "");
            if (requestCode == 2) {
                try {
                    Uri selectedImage = data.getData();
                    String[] filePath = {MediaStore.Images.Media.DATA};
                    Cursor c = getContentResolver().query(selectedImage,
                            filePath, null, null, null);
                    c.moveToFirst();
                    int columnIndex = c.getColumnIndex(filePath[0]);
                    String picturePath = c.getString(columnIndex);
                    Log.v("path of gallery", picturePath + "");
                    c.close();
                    Bitmap thumbnail = decodeFile(picturePath);
                    Log.v("gallery code bitmap", "" + thumbnail);
                    new UploadImg().execute(picturePath);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                } catch (OutOfMemoryError ome) {
                    ome.printStackTrace();
                }
            }
        } else {
            Log.v("else" + requestCode, "result" + resultCode);
        }
    }


    public class Edit_Profile_Async extends AsyncTask<String, Integer, String> {
        private Context context;
        private String mobileno;
        private String url = Constants.BaseURL + Constants.profile;
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
            String firstname, lastname, email, address;
            firstname = etfirstname.getText().toString().trim();
            lastname = etlastname.getText().toString().trim();
            address = etaddress.getText().toString().trim();
            email = etemail.getText().toString().trim();
            String jsonData = null;
            Response response = null;
            OkHttpClient client = new OkHttpClient();
            RequestBody body = new FormBody.Builder()
                    .add(Constants.mobileno, etphone.getText().toString().trim())
                    .add("firstname", firstname)
                    .add("lastname", lastname)
                    .add("email", email)
                    .add("address", address)
                    .add("userimage", image)
                    .add("userimageurl", imageurl)
                    .add("usertype", GetSet.getUser_type())
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
                    JSONObject object = array.getJSONObject(0);
                    GetSet.setIsLogged(true);
                    GetSet.setImagename(object.getString("userimage"));
                    GetSet.setImageUrl(object.getString("userimageurl"));
                    GetSet.setFirstname(object.getString("firstname"));
                    GetSet.setLastname(object.getString("lastname"));
                    GetSet.setMobileno(object.getString("mobileno"));
                    GetSet.setEmail(object.getString("email"));
                    GetSet.setAddress(object.getString("address"));


                    Constants.editor.putString("userimage", GetSet.getImagename());
                    Constants.editor.putString("userimageurl", GetSet.getImageUrl());
                    Constants.editor.putString("fname", GetSet.getFirstname());
                    Constants.editor.putString("lname", GetSet.getLastname());
                    Constants.editor.putString("mobileno", GetSet.getMobileno());
                    Constants.editor.putString("email", GetSet.getEmail());
                    Constants.editor.putString("address", GetSet.getAddress());
                    Constants.editor.commit();

                    finish();
                    Intent i = new Intent(ProfileActivity.this, HomeActivity.class);
                    startActivity(i);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    public void galleryAddPic(String file) {
        File f = new File(file);
        Uri contentUri = Uri.fromFile(f);
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, contentUri);
        sendBroadcast(mediaScanIntent);
    }

    class DoFileUpload extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {

            pDialog = new ProgressDialog(ProfileActivity.this);
            pDialog.setMessage("wait uploading Image..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                // Set your file path here
                FileInputStream fstrm = new FileInputStream(imagepath);
                // Set your server page url (and the file title/description)
                Image_Upload hfu = new Image_Upload(Constants.BaseURL + Constants.uploadimage, fname);
                upflag = hfu.Send_Now(fstrm);
            } catch (FileNotFoundException e) {
                // Error: File not found
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String file_url) {
            if (pDialog.isShowing()) {
                pDialog.dismiss();
            }
            if (upflag) {
                Toast.makeText(getApplicationContext(), "Uploading Complete", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), "Unfortunately file is not Uploaded..", Toast.LENGTH_LONG).show();
            }
        }
    }


    public File saveBitmapToFile(File file) {
        try {

            // BitmapFactory options to downsize the image
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            o.inSampleSize = 2;
            o.inPreferredConfig = Bitmap.Config.RGB_565;
            // factor of downsizing the image

            FileInputStream inputStream = new FileInputStream(file);
            //Bitmap selectedBitmap = null;
            BitmapFactory.decodeStream(inputStream, null, o);
            inputStream.close();

            // The new size we want to scale to
            final int REQUIRED_SIZE = 1024;

            // Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while (o.outWidth / scale / 2 >= REQUIRED_SIZE &&
                    o.outHeight / scale / 2 >= REQUIRED_SIZE) {
                scale *= 2;
            }
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            o2.inPreferredConfig = Bitmap.Config.RGB_565;
            inputStream = new FileInputStream(file);

            Bitmap selectedBitmap = BitmapFactory.decodeStream(inputStream, null, o2);
            inputStream.close();

            // here i override the original image file

            //file.createNewFile();
            File sdCard = Environment.getExternalStorageDirectory();
            File dir = new File(sdCard.getAbsolutePath() + "/" + getString(R.string.app_name));
            dir.mkdirs();
            file = new File(dir, String.valueOf(System.currentTimeMillis()) + ".jpg");
            FileOutputStream outputStream = new FileOutputStream(file);
            selectedBitmap.compress(Bitmap.CompressFormat.JPEG, 70, outputStream);
            galleryAddPic(file.toString());
            outputStream.flush();
            outputStream.close();

            return file;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public class UploadImg extends AsyncTask<String, Integer, String> {
        JSONObject jsonobject = null;
        String Json = "";
        String status;
        ProgressDialog pd;

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(ProfileActivity.this);
            pd.setMessage("Image uploading please wait...");
            pd.show();
        }

        @Override
        protected String doInBackground(String... imgpath) {
            HttpURLConnection conn = null;
            DataOutputStream dos = null;
            DataInputStream inStream = null;
            StringBuilder builder = new StringBuilder();
            String lineEnd = "\r\n";
            String twoHyphens = "--";
            String boundary = "*****";
            int bytesRead, bytesAvailable, bufferSize;
            byte[] buffer;
            int maxBufferSize = 1 * 1024 * 1024;
            String urlString = Constants.BaseURL + Constants.uploadimage;
            try {
                String exsistingFileName = imgpath[0];
                Log.v(" exsistingFileName", exsistingFileName);
                FileInputStream fileInputStream = new FileInputStream(saveBitmapToFile(new File(exsistingFileName)));
                URL url = new URL(urlString);
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setUseCaches(false);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                dos = new DataOutputStream(conn.getOutputStream());
                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"images\";filename=\""
                        + exsistingFileName + "\"" + lineEnd);
                dos.writeBytes(lineEnd);
                Log.e("MediaPlayer", "Headers are written");
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];

                Log.v("buffer", "buffer" + buffer);

                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                while (bytesRead > 0) {
                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                    Log.v("bytesRead", "bytesRead" + bytesRead);
                }
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        conn.getInputStream()));
                String inputLine;
                Log.v("in", "" + in);
                while ((inputLine = in.readLine()) != null)
                    builder.append(inputLine);

                Log.e("MediaPlayer", "File is written");
                fileInputStream.close();
                Json = builder.toString();
                dos.flush();
                dos.close();

            } catch (MalformedURLException ex) {
                Log.e("MediaPlayer", "error: " + ex.getMessage(), ex);
            } catch (IOException ioe) {
                Log.e("MediaPlayer", "error: " + ioe.getMessage(), ioe);
            }
            try {
                inStream = new DataInputStream(conn.getInputStream());
                String str;
                while ((str = inStream.readLine()) != null) {
                    Log.e("MediaPlayer", "Server Response" + str);
                }
                inStream.close();
            } catch (IOException ioex) {
                Log.e("MediaPlayer", "error: " + ioex.getMessage(), ioex);
            }
            try {
                jsonobject = new JSONObject(Json);
                Log.v("json", "json" + Json);
                status = jsonobject.getString("status");
                if (status.equals("success")) {
                    image = jsonobject.getString("image");
                    imageurl = jsonobject.getString("imageurl");

                }

            } catch (JSONException e) {
                status = "false";
                e.printStackTrace();
            } catch (NullPointerException e) {
                status = "false";
                e.printStackTrace();
            } catch (Exception e) {
                status = "false";
                e.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            Log.v("editprofile", "imageupload" + uploadedImage);
            Glide.with(ProfileActivity.this).load(imageurl).into(circularImageView);
            pd.dismiss();
        }

    }

    public class Get_Profile_Async extends AsyncTask<String, Integer, String> {

        private Context context;
        private String url = Constants.BaseURL + Constants.getprofile;
        ProgressDialog progress;

        public Get_Profile_Async(Context ctx) {

            context = ctx;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progress = new ProgressDialog(ProfileActivity.this);
            progress.setMessage("Please wait ....");
            progress.setTitle("Loading");
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.show();

        }

        @Override
        protected String doInBackground(String... strings) {

            String jsonData = null;
            Response response = null;
            OkHttpClient client = new OkHttpClient();
            RequestBody body = new FormBody.Builder()
                    .add(Constants.mobileno, mobileno)
                    .add(Constants.email, GetSet.getEmail())
                    .add(Constants.type, GetSet.getUser_type())
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
                    JSONObject object = array.getJSONObject(0);
                    GetSet.setIsLogged(true);
                    GetSet.setClientid(object.getString("client_id"));
                    GetSet.setUser_type(object.getString("type"));
                    GetSet.setMobileno(object.getString("mobileno"));
                    GetSet.setBrand(object.getString("brand"));
                    GetSet.setModel(object.getString("model"));
                    GetSet.setPlatenot(object.getString("plateno"));
                    GetSet.setFirstname(object.getString("firstname"));
                    GetSet.setLastname(object.getString("lastname"));
                    GetSet.setEmail(object.getString("email"));
                    GetSet.setAddress(object.getString("address"));
                    GetSet.setImagename(object.getString("userimage"));
                    GetSet.setImageUrl(object.getString("userimageurl"));
                    GetSet.setSocialimg(object.getString("socialimage"));


                    Constants.editor.putString("client_id", GetSet.getClientid());
                    Constants.editor.putString("firstname", GetSet.getFirstname());
                    Constants.editor.putString("lastname", GetSet.getLastname());
                    Constants.editor.putString("mobileno", GetSet.getMobileno());
                    Constants.editor.putString("email", GetSet.getEmail());
                    Constants.editor.putString("address", GetSet.getAddress());
                    Constants.editor.putString("brand", GetSet.getBrand());
                    Constants.editor.putString("type", GetSet.getUser_type());
                    Constants.editor.putString("model", GetSet.getModel());
                    Constants.editor.putString("plateno", GetSet.getPlatenot());
                    Constants.editor.putString("userimage", GetSet.getImagename());
                    Constants.editor.putString("userimageurl", GetSet.getImageUrl());
                    Constants.editor.putString("socialimage", GetSet.getSocialimg());
                    Constants.editor.commit();
                    Constants.editor.apply();
                    setdata();
                }
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
}
