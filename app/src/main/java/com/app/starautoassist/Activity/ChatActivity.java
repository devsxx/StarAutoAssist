package com.app.starautoassist.Activity;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.app.starautoassist.Adapter.ChatAdapter;
import com.app.starautoassist.Data.Message;
import com.app.starautoassist.Others.Constants;
import com.app.starautoassist.Others.NotificationUtilz;
import com.app.starautoassist.Others.Starautoassist_Application;
import com.app.starautoassist.R;
import com.app.starautoassist.Services.FirebaseInstanceIDService;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener{

    BroadcastReceiver mRegistrationBroadcastReceiver;
    private ProgressDialog dialog;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    private ArrayList<Message> messages;
    private Button buttonSend;
    private EditText editTextMessage;
    String senderid,receiver_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Intent intent = getIntent();
        receiver_id = intent.getStringExtra("receiverid");
        Constants.pref=getApplicationContext().getSharedPreferences("StarAutoAssist",MODE_PRIVATE);

        recyclerView = findViewById(R.id.rv_chat);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        messages = new ArrayList<>();

        adapter = new ChatAdapter(senderid, ChatActivity.this, messages);
        recyclerView.setAdapter(adapter);

        senderid = Constants.pref.getString("mobileno","");

        fetchMessages();
        
        buttonSend = findViewById(R.id.buttonSend);
        buttonSend.setOnClickListener(this);
        editTextMessage = findViewById(R.id.editTextMessage);

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                if (intent.getAction().equals(Constants.DATA_NOTIFICATION)){

                    String id="",message = "",senderid="",imageUrl,timestamp;
                    String datamessage = intent.getStringExtra("data");
                    JSONObject data = null;

                    try {
                        JSONObject obj = new JSONObject(datamessage);
                            data = obj.getJSONObject("data");
                            id = data.getString("id");
                            message = data.getString("message");
                            senderid = data.getString("senderid");
                            imageUrl = "";
                            timestamp = data.getString("timestamp");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    addMessage(message, senderid);
                }
            }
        };

        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());

        if (ConnectionResult.SUCCESS != resultCode) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                Toast.makeText(getApplicationContext(), "Google Play Service is not install/enabled in this device!", Toast.LENGTH_LONG).show();
                GooglePlayServicesUtil.showErrorNotification(resultCode, getApplicationContext());

            } else {
                Toast.makeText(getApplicationContext(), "This device does not support for Google Play Service!", Toast.LENGTH_LONG).show();
            }
        } else {
            Intent itent = new Intent(this, FirebaseInstanceIDService.class);
            startService(itent);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        // For Internet checking
        Starautoassist_Application.registerReceiver(ChatActivity.this);
        LocalBroadcastManager.getInstance(ChatActivity.this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Constants.DATA_NOTIFICATION));
        NotificationUtilz.clearNotifications(getApplicationContext());
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        // For Internet disconnect checking
        Starautoassist_Application.unregisterReceiver(ChatActivity.this);
        super.onPause();
    }


    private void addMessage(String message, String id) {

        Message mes = new Message(id, message, getTimeStamp());
        messages.add(mes);
        scrollToBottom();

        editTextMessage.setText("");

    }

    private void scrollToBottom() {

        adapter.notifyDataSetChanged();

        if (adapter.getItemCount() > 1){

            recyclerView.getLayoutManager().smoothScrollToPosition(recyclerView, null, adapter.getItemCount() - 1);
        }
    }

    private void fetchMessages() {

       new getMessage(this, senderid, receiver_id).execute();
    }
    
    public static String getTimeStamp() {

        SimpleDateFormat format = new SimpleDateFormat("hh:mm a");
        Date date=new Date();
        date.getTime();
        return format.format(date);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.buttonSend:
                String smessage = editTextMessage.getText().toString();
                if (!smessage.equalsIgnoreCase("")){
                    String stime = getTimeStamp();
                    new sendMessage( ChatActivity.this, senderid, receiver_id, smessage, stime).execute();
                }
        }

    }

    private class sendMessage extends AsyncTask<String, Integer, String>{

        String fromid, toid, smessage, stime;
        Context context;
        String url = Constants.send_message;
        ProgressDialog progress;

        public sendMessage(Context context, String fromid, String toid, String smessage, String stime) {
            this.fromid = fromid;
            this.toid = toid;
            this.smessage = smessage;
            this.stime = stime;
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
                    .add("fromid", fromid.trim())
                    .add("toid", toid.trim())
                    .add("message", smessage.trim())
                    .add("sentat", stime.trim())
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
                        Message msg = new Message(senderid, smessage, stime);
                        messages.add(msg);
                        adapter = new ChatAdapter(senderid, ChatActivity.this, messages);
                        recyclerView.setAdapter(adapter);
                        scrollToBottom();
                        editTextMessage.setText("");
                        Toast.makeText(ChatActivity.this, "Message sent..", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(ChatActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                }
            }catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    private class getMessage extends AsyncTask<String, Integer, String>{

        Context context;
        String url = Constants.BaseURL+Constants.get_message;
        ProgressDialog progress;
        String sid, smsg, stime, fromid, toid;

        public getMessage(Context context, String fromid, String toid) {
            this.context = context;
            this.fromid = fromid;
            this.toid = toid;
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
                    .add("fromid", fromid)
                    .add("toid", toid)
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

                    for (int i = 0; i < array.length(); i++) {

                        JSONObject object = array.getJSONObject(i);

                        sid = object.getString("sentid");
                        smsg = object.getString("message");
                        stime = object.getString("sentat");

                        Message objmsg = new Message(sid, smsg, stime);
                        messages.add(objmsg);
                    }

                    adapter = new ChatAdapter(senderid, ChatActivity.this, messages);
                    recyclerView.setAdapter(adapter);
                    scrollToBottom();

                }else {
                    Toast.makeText(ChatActivity.this, "message", Toast.LENGTH_SHORT).show();
                }
            }catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
