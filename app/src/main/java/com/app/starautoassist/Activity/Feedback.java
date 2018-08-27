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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.app.starautoassist.Others.Constants;
import com.app.starautoassist.Others.Starautoassist_Application;
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

public class Feedback extends AppCompatActivity {
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        final EditText feedback=(EditText)findViewById(R.id.feedback);
        TextView send=(TextView)findViewById(R.id.sendbtn);
        Constants.pref=getApplicationContext().getSharedPreferences("StarAutoAssist",MODE_PRIVATE);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String feed=feedback.getText().toString().trim();
                new Send_Feedback(Feedback.this,feed).execute();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(Feedback.this,HomeActivity.class);
        startActivity(intent);
        finish();
    }

    public class Send_Feedback extends AsyncTask<String, Integer, String> {
        private Context context;
        private String feeback;
        private String url = Constants.BaseURL + Constants.send_feedback;

        public Send_Feedback(Context ctx, String feeback) {
            context = ctx;
            this.feeback = feeback;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog=new ProgressDialog(Feedback.this);
            progressDialog.setTitle("Sending Feedback");
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
                    .add(Constants.mobileno, Constants.pref.getString("mobileno",""))
                    .add(Constants.firstname, Constants.pref.getString("firstname",""))
                    .add("feedback",feeback)
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
                    Intent intent=new Intent(Feedback.this,HomeActivity.class);
                    startActivity(intent);
                    finish();
                    Toast.makeText(getApplicationContext(),"Feedback send successfully",Toast.LENGTH_SHORT).show();
                }else Toast.makeText(getApplicationContext(),jonj.getString("message"),Toast.LENGTH_SHORT).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
