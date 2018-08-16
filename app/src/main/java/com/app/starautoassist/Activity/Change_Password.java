package com.app.starautoassist.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.app.starautoassist.Helper.GetSet;
import com.app.starautoassist.Others.Constants;
import com.app.starautoassist.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Change_Password extends AppCompatActivity {
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change__password);
        final EditText etoldpass = (EditText)findViewById(R.id.et_change_oldpass);
        final EditText etnewpass = (EditText)findViewById(R.id.et_change_newpass);
        final EditText etconfirmpass = (EditText)findViewById(R.id.et_change_confirmpass);
        Button btnchangepass = (Button) findViewById(R.id.btn_changepass);

        btnchangepass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldpass = etoldpass.getText().toString();
                String newpass = etnewpass.getText().toString();
                String confirmpass = etconfirmpass.getText().toString();

                if (newpass.equals(confirmpass)){
                    //proceed to further code
                    new Change_Pass(Change_Password.this, GetSet.getMobileno(),oldpass,newpass).execute();


                }else {

                    Toast.makeText(Change_Password.this, "Password do not Match!", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    public class Change_Pass extends AsyncTask<String, Integer, String> {
        private Context context;
        private String mobileno, oldpass,newpass;
        private String url = Constants.BaseURL + Constants.changepassword;

        public Change_Pass(Context ctx, String mobileno, String oldpass,String newpass) {
            context = ctx;
            this.mobileno = mobileno;
            this.oldpass = oldpass;
            this.newpass = newpass;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog=new ProgressDialog(Change_Password.this);
            progressDialog.setTitle("Changing password");
            progressDialog.setMessage("Please wait... Loading");
            progressDialog.show();

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
            progressDialog.dismiss();
            try {
                jonj = new JSONObject(jsonData);
                if (jonj.getString("status").equalsIgnoreCase(
                        "success")) {
                    Intent intent=new Intent(Change_Password.this,HomeActivity.class);
                    startActivity(intent);
                    finish();
                    Toast.makeText(getApplicationContext(),"Password changed successfully",Toast.LENGTH_SHORT).show();
                }else Toast.makeText(getApplicationContext(),jonj.getString("message"),Toast.LENGTH_SHORT).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
