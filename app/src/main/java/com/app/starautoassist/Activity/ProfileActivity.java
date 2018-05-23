package com.app.starautoassist.Activity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.app.starautoassist.R;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.io.IOException;

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
}
