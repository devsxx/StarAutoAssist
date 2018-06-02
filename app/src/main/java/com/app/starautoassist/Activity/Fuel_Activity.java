package com.app.starautoassist.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Loader;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.app.starautoassist.Helper.GetSet;
import com.app.starautoassist.Others.Constants;
import com.app.starautoassist.R;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Fuel_Activity extends AppCompatActivity {

   /* private MapView mapView;*/
    private Spinner spinnerprice, spinnerfuel;
    private TextView tvlitre;
    private Button btnproceed;
    List<String> avail_amount=new ArrayList<String>();
    List<String> fuletype=new ArrayList<String>();
    List<String> perltr=new ArrayList<String>();
    String fuelprice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Fuel Service");
        setContentView(R.layout.activity_fuel);
        new Get_Fuel_Service(Fuel_Activity.this).execute();
        /*mapView = findViewById(R.id.mapView);*/
        spinnerprice = findViewById(R.id.spin_price);
        spinnerfuel = findViewById(R.id.spin_fuel);
        tvlitre = findViewById(R.id.fuel_tv_litre);
        btnproceed = findViewById(R.id.fuel_btn_proceed);

        spinnerfuel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ArrayAdapter<String> adapterprice = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, avail_amount);
                adapterprice.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerprice.setAdapter(adapterprice);
                if(position==0){
                  fuelprice=perltr.get(0);
                }
                else if (fuletype.get(position).equalsIgnoreCase("petrol")){
                     fuelprice = perltr.get(position);
                }else if (fuletype.get(position).equalsIgnoreCase("diesel")){
                    fuelprice = perltr.get(position);
                }else if (fuletype.get(position).equalsIgnoreCase("gas")){
                    fuelprice = perltr.get(position);
                }
                Toast.makeText(getApplicationContext(), spinnerfuel.getSelectedItem().toString() + "\tis Selected", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerprice.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               Float ltr= Float.valueOf(avail_amount.get(position))/Float.valueOf(fuelprice);
               tvlitre.setText(String.format("%.2f",ltr));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
    public class Get_Fuel_Service extends AsyncTask<String, Integer, String> {
        private Context context;
        private String url = Constants.BaseURL + Constants.get_fuel_request;
        ProgressDialog progress;
        @Nullable
        String user_id;

        public Get_Fuel_Service(Context ctx) {
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
            Request request = new Request.Builder()
                    .url(url)
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
            HashMap<String, String> map;
            JSONObject jonj = null;
            try {
                jonj = new JSONObject(jsonData);
                if (jonj.getString("status").equalsIgnoreCase(
                        "success")) {
                    String data=jonj.getString("data");
                    JSONArray array=new JSONArray(data);
                    fuletype.add(0,"Select Fuel Type");
                    perltr.add(0,"0");
                    for(int i=0;i<array.length();i++) {
                        JSONObject object = array.getJSONObject(i);
                        String type = object.getString("sub_category");
                        String price = object.getString("price");
                        fuletype.add(type);
                        perltr.add(price);
                    }

                    JSONObject obj=array.getJSONObject(1);
                    String amout=obj.getString("rate");
                    avail_amount.add(0,"Select amount");
                    avail_amount = Arrays.asList(amout.split(","));
                    ArrayAdapter<String> adapterfuel = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item,fuletype);
                    adapterfuel.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerfuel.setAdapter(adapterfuel);
                }else  Toast.makeText(context,jonj.getString("message"),Toast.LENGTH_SHORT).show();
            }catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
