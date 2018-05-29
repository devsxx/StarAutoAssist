package com.app.starautoassist.Activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
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
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.starautoassist.Helper.EditProfilePhoto;
import com.app.starautoassist.Helper.GetSet;
import com.app.starautoassist.Others.Constants;
import com.app.starautoassist.R;
import com.bumptech.glide.Glide;
import com.facebook.CallbackManager;
import com.mikhaellopez.circularimageview.CircularImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;

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
    private TextView tvphone;
    private EditText etfirstname, etlastname, etaddress,etemail;
    private static final int PICK_PICTURE = 1;
    private static final String TAG = "ProfileActivity";
    CallbackManager callbackManager;
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
        etemail = findViewById(R.id.et_profile_email);


        circularImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
        gallerydialog();
            }
        });

    }


    @Override
    protected void onResume() {
        super.onResume();
        if (EditProfilePhoto.editPhoto){
            EditProfilePhoto.editPhoto = false;
         //   new UploadImage().execute(EditProfilePhoto.imgPath);
        }
    }
    /** for user choose the dp from camera or gallery **/
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
        ImageView gal = (ImageView) dialog.findViewById(R.id.galery);
        ImageView cam = (ImageView) dialog.findViewById(R.id.camra);

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
                    GetSet.setAddress(jonj.getString("address"));


                    Constants.editor.putString("userimage", GetSet.getImageUrl());
                    Constants.editor.putString("fname", GetSet.getFirstname());
                    Constants.editor.putString("lname", GetSet.getLastname());
                    Constants.editor.putString("mobileno", GetSet.getMobileno());
                    Constants.editor.putString("email", GetSet.getEmail());
                    Constants.editor.putString("address", GetSet.getAddress());

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
                    new UploadImg(ProfileActivity.this,picturePath).execute();
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
        callbackManager.onActivityResult(requestCode, resultCode, data);

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
            String firstname,lastname,email,address;
            firstname=etfirstname.getText().toString().trim();
            lastname=etlastname.getText().toString().trim();
            address=etaddress.getText().toString().trim();
            email=etemail.getText().toString().trim();
            String jsonData = null;
            Response response = null;
            OkHttpClient client = new OkHttpClient();
            RequestBody body = new FormBody.Builder()
                    .add(Constants.mobileno, GetSet.getMobileno())
                    .add("firstname", firstname)
                    .add("lastname", lastname)
                    .add("email", email)
                    .add("address", address)
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
                    GetSet.setAddress(jonj.getString("address"));


                    Constants.editor.putString("userimage", GetSet.getImageUrl());
                    Constants.editor.putString("fname", GetSet.getFirstname());
                    Constants.editor.putString("lname", GetSet.getLastname());
                    Constants.editor.putString("mobileno", GetSet.getMobileno());
                    Constants.editor.putString("email", GetSet.getEmail());
                    Constants.editor.putString("address", GetSet.getAddress());
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

    public void galleryAddPic(String file) {
        File f = new File(file);
        Uri contentUri = Uri.fromFile(f);
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, contentUri);
        sendBroadcast(mediaScanIntent);
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
        Context context;
        String path;

        public UploadImg(Context ctx, String path) {
            context = ctx;
            this.path = path;

        }

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(ProfileActivity.this);
            progressDialog.setMessage("Please Wait....");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... str) {

            String res = null;
            try {
                File sourceFile = new File(path);
                Log.d("TAG", "File...::::" + sourceFile + " : " + sourceFile.exists());
                final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/*");
                String filename = path.substring(path.lastIndexOf("/") + 1);
                RequestBody requestBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("imagename", filename, RequestBody.create(MEDIA_TYPE_PNG, sourceFile))
                        .build();

                Request request = new Request.Builder()
                        .url(Constants.BaseURL + Constants.uploadimage)
                        .post(requestBody)
                        .build();

                OkHttpClient client = new OkHttpClient();
                okhttp3.Response response = client.newCall(request).execute();
                res = response.body().string();
                Log.e("TAG", "Response : " + res);
                return res;

            } catch (UnknownHostException | UnsupportedEncodingException e) {
                Log.e("TAG", "Error: " + e.getLocalizedMessage());
            } catch (Exception e) {
                Log.e("TAG", "Other Error: " + e.getLocalizedMessage());
            }
            return res;

        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            if (progressDialog != null)
                progressDialog.dismiss();

            if (response != null) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equals("success")) {
                      String  uploadedImage = jsonObject.getString("imagename");
                      String  viewUrl = jsonObject.getString("imageurl");
                        Glide
                                .with(context)
                                .load(viewUrl)
                                .placeholder(R.drawable.logo)
                                .into(circularImageView);

                    } else {
                        Toast.makeText(ProfileActivity.this, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(ProfileActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
            }

        }
    }
}
