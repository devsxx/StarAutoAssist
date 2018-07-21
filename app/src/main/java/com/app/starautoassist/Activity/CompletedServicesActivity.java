package com.app.starautoassist.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;
import com.app.starautoassist.Adapter.Completed_Service_Adapter;
import com.app.starautoassist.Others.Constants;
import com.app.starautoassist.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CompletedServicesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    private Completed_Service_Adapter completedServiceAdapter;
    public static ArrayList<HashMap<String, String>> completed_list = new ArrayList<HashMap<String, String>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completed_services);

        recyclerView = findViewById(R.id.rv_completed);

        new completedService(this).execute();

    }

    private class completedService extends AsyncTask<String, IntroActivity, String>{

        Context context;
        String url = Constants.BaseURL+Constants.completedservice;
        ProgressDialog progress;
        HashMap<String, String> map;
        String sid, sname, samount;

        public completedService(Context context) {
            this.context = context;
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

        @Override
        protected String doInBackground(String... strings) {

            String jsonData = null;
            Response response = null;
            OkHttpClient client = new OkHttpClient();
            RequestBody body = new FormBody.Builder()
                  /*  .add("", )
                    .add("", )*/
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

                if (jsonData != null) {
                    jonj = new JSONObject(jsonData);

                    if (jonj.getString("status").equalsIgnoreCase(
                            "success")) {
                        String data = jonj.getString("data");
                        JSONArray array = new JSONArray(data);

                        completed_list.clear();

                        for (int i = 0; i < array.length(); i++) {
                            map = new HashMap<String, String>();
                            JSONObject object = array.getJSONObject(i);

                            sid = object.getString("");
                            sname = object.getString("");
                            samount = object.getString("");

                            map.put("", sid);
                            map.put("", sname);
                            map.put("", samount);

                            completed_list.add(map);

                        }
                        completedServiceAdapter = new Completed_Service_Adapter(CompletedServicesActivity.this, completed_list);
                        layoutManager = new LinearLayoutManager(CompletedServicesActivity.this, LinearLayoutManager.VERTICAL, false);
                        recyclerView.setLayoutManager(layoutManager);
                        recyclerView.setAdapter(completedServiceAdapter);
                    }
                }
                else {
                    Toast.makeText(CompletedServicesActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
