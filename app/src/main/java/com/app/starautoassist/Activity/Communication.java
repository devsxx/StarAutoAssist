package com.app.starautoassist.Activity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.app.starautoassist.Adapter.Accepted_Request_Adapter;
import com.app.starautoassist.Adapter.Review_Adapter;
import com.app.starautoassist.Helper.GetSet;
import com.app.starautoassist.Others.Constants;
import com.app.starautoassist.Others.Starautoassist_Application;
import com.app.starautoassist.R;
import com.bumptech.glide.Glide;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Communication extends AppCompatActivity implements View.OnClickListener {
    RecyclerView recyclerView;
    MaterialEditText name, companyname, contactno, address;
    CircularImageView img;
    RatingBar ratingBar;
    TextView callbtn, chatbtn, mapbtn, acceptbtn, declinebtn;
    Review_Adapter review_adapter;
    LinearLayout threebuttonlayout, acceptdeclinelayout;
    LinearLayoutManager layoutManager;
    HashMap<String, String> hashMap;
    private static final int REQUEST_PHONE_CALL = 1;
    float overallrating = 0;
    public static ArrayList<HashMap<String, String>> reviewlist = new ArrayList<HashMap<String, String>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_communication);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        hashMap = (HashMap<String, String>) getIntent().getExtras().get("map");
        new Get_Review_Async(Communication.this).execute();
        name = (MaterialEditText) findViewById(R.id.fname);
        img = (CircularImageView) findViewById(R.id.spimage);
        companyname = (MaterialEditText) findViewById(R.id.companyname);
        contactno = (MaterialEditText) findViewById(R.id.mobileno);
        address = (MaterialEditText) findViewById(R.id.address);
        ratingBar = (RatingBar) findViewById(R.id.ratingbar);
        recyclerView = (RecyclerView) findViewById(R.id.reviewlist);
        threebuttonlayout = (LinearLayout) findViewById(R.id.tributton);
        callbtn = (TextView) findViewById(R.id.callbutton);
        chatbtn = (TextView) findViewById(R.id.chatbutton);
        mapbtn = (TextView) findViewById(R.id.mapbutton);
        acceptdeclinelayout = (LinearLayout) findViewById(R.id.acceptdeclinelay);
        acceptbtn = (TextView) findViewById(R.id.accept_req_btn);
        declinebtn = (TextView) findViewById(R.id.decline_req_btn);
        if (hashMap.get(Constants.status).equalsIgnoreCase("3")) {
            threebuttonlayout.setVisibility(View.VISIBLE);
            acceptdeclinelayout.setVisibility(View.GONE);
        } else {
            threebuttonlayout.setVisibility(View.GONE);
            acceptdeclinelayout.setVisibility(View.VISIBLE);
        }
        name.setText(hashMap.get(Constants.firstname));
        companyname.setText(hashMap.get(Constants.companyname));
        contactno.setText(hashMap.get(Constants.serviceprovider_id));
        address.setText(hashMap.get(Constants.address));
        Glide.with(this)
                .load(Constants.SPIMGURL + hashMap.get(Constants.providerimage))
                .into(img);
        callbtn.setOnClickListener(this);
        chatbtn.setOnClickListener(this);
        mapbtn.setOnClickListener(this);
        acceptbtn.setOnClickListener(this);
        declinebtn.setOnClickListener(this);

        Constants.pref=getApplicationContext().getSharedPreferences("StarAutoAssist",MODE_PRIVATE);
        Constants.editor = Constants.pref.edit();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.callbutton:
                String mobileno=hashMap.get(Constants.serviceprovider_id);
                Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel",mobileno , null));
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(Communication.this, new String[]{Manifest.permission.CALL_PHONE},REQUEST_PHONE_CALL);
                    return;
                }
                startActivity(callIntent);
                break;
            case R.id.chatbutton:
                String client = Constants.pref.getString("mobileno", "");
                String provider = hashMap.get(Constants.serviceprovider_id);
                new fetchToken(this, client, provider).execute();
                break;
            case R.id.mapbutton:
                Intent intent=new Intent(Communication.this,TrackerActivity.class);
                intent.putExtra("map",hashMap);
                startActivity(intent);
                break;
            case R.id.accept_req_btn:
              String  type = "0";
                new Send_request_to_sp(this, hashMap.get(Constants.serviceid), type, hashMap.get(Constants.serviceprovider_id)).execute();
                break;
            case R.id.decline_req_btn:
                type = "1";
                new Send_request_to_sp(this, hashMap.get(Constants.serviceid), type, hashMap.get(Constants.serviceprovider_id)).execute();
                break;
        }

    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Starautoassist_Application.freeMemory();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_PHONE_CALL: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
                }
                else
                {
                }
                return;
            }
        }
    }

    public class Get_Review_Async extends AsyncTask<String, Integer, String> {
        private Context context;
        private String url = Constants.BaseURL + Constants.get_review_list;
        ProgressDialog progress;
        @Nullable
        String user_id;
        HashMap<String, String> map;
        public Get_Review_Async(Context ctx) {
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
                    .add("sp_no", hashMap.get(Constants.serviceprovider_id))
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
            String clientname,clientimage,rating,reviews,clientid,spcontact,address,service_description,providerimage;
            Log.v("result", "" + jsonData);
            JSONObject jonj = null;
            try {
                jonj = new JSONObject(jsonData);
                if(jsonData!=null) {
                    if (jonj.getString("status").equalsIgnoreCase(
                            "success")) {
                        String data = jonj.getString("data");
                        JSONArray array = new JSONArray(data);
                        reviewlist.clear();
                        float ratingtotal = 0;
                        for (int i = 0; i < array.length(); i++) {
                            map = new HashMap<String, String>();
                            JSONObject object = array.getJSONObject(i);
                            clientname = object.getString(Constants.firstname);
                            if (object.getString(Constants.socialimage).equalsIgnoreCase(""))
                                clientimage = object.getString(Constants.userimageurl);
                            else
                                clientimage = object.getString(Constants.socialimage);
                            rating = object.getString(Constants.rating);
                            ratingtotal = ratingtotal + Float.parseFloat(rating);
                            reviews = object.getString(Constants.review);
                            map.put(Constants.firstname, clientname);
                            map.put(Constants.userimage, clientimage);
                            map.put(Constants.rating, rating);
                            map.put(Constants.review, reviews);
                            reviewlist.add(map);
                        }
                        int count = array.length();
                        overallrating = ratingtotal / count;
                        ratingBar.setNumStars(5);
                        ratingBar.setRating(overallrating);
                        review_adapter = new Review_Adapter(Communication.this, reviewlist);
                        layoutManager = new LinearLayoutManager(Communication.this, LinearLayoutManager.VERTICAL, false);
                        recyclerView.setLayoutManager(layoutManager);
                        recyclerView.setAdapter(review_adapter);
                    }
                }else {
                    recyclerView.setVisibility(View.GONE);
                }

            }catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    public class Send_request_to_sp extends AsyncTask<String, Integer, String> {
        private Context context;
        private String url = Constants.BaseURL + Constants.send_response_to_sp;
        ProgressDialog progress;
        @Nullable
        String service_id="";
        String type,sp_id;
        HashMap<String, String> map;
        public Send_request_to_sp(Context ctx, String service_id, String type,String sp_id) {
            context = ctx;
            this.service_id=service_id;
            this.type=type;
            this.sp_id=sp_id;
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
                    .add(Constants.serviceprovider_id, sp_id)
                    .add(Constants.serviceid, service_id)
                    .add(Constants.type, type)
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
            String sname,time,sid,status,sprice;
            Log.v("result", "" + jsonData);
            JSONObject jonj = null;
            try {
                jonj = new JSONObject(jsonData);
                if (jonj.getString("status").equalsIgnoreCase(
                        "success")) {
                    Toast.makeText(context,jonj.getString("message"),Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(context, HomeActivity.class);
                    context.startActivity(intent);
                    ((Activity)context).finish();
                }
            }catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    private class fetchToken extends AsyncTask<String, Integer, String>{

        private Context context;
        private String url = Constants.BaseURL + Constants.getpushid;
        ProgressDialog progress;
        String  providerid,clientid;

        public fetchToken(Context context, String clientid, String providerid) {
            this.context = context;
            this.clientid = clientid;
            this.providerid = providerid;
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
                    .add(Constants.client_id, clientid)
                    .add(Constants.serviceprovider_id, providerid)
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
            String c_pushid, s_pushid;
            Log.v("result", "" + jsonData);
            JSONObject jonj = null;
            try {
                jonj = new JSONObject(jsonData);

                if (jonj.getString("status").equalsIgnoreCase(
                        "success")) {
                    Intent intent=new Intent(context, ChatActivity.class);
                    intent.putExtra("receiverid",hashMap.get(Constants.serviceprovider_id));
                    context.startActivity(intent);
                }
            }catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }
}
