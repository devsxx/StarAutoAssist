package com.app.starautoassist.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.app.starautoassist.Adapter.MyCars_Adapter;
import com.app.starautoassist.Others.Constants;
import com.app.starautoassist.Others.Starautoassist_Application;
import com.app.starautoassist.R;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

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

public class VechicleActivity extends AppCompatActivity {

    private SearchableSpinner spinnerbrand, spinnertype;
    private Button btnsubmit;
    private EditText etmodel, etplate;
    String brandname, modelname, plateno,brandlogo,flatbedvalue;
    ArrayAdapter<String> brandadapter,modeladapter;
    MyCars_Adapter myCars_adapter;
    ArrayList<String> brands=new ArrayList<String>();
    ArrayList<String> model=new ArrayList<String>();
    String mobileno;
    RecyclerView mycarListview;
    LinearLayoutManager layoutManager;
    ArrayList<HashMap<String, String>> carlist = new ArrayList<HashMap<String, String>>();
    public static ArrayList<HashMap<String, String>> mycars = new ArrayList<HashMap<String, String>>();
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Starautoassist_Application.freeMemory();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Star Auto Assist");
        setContentView(R.layout.activity_vechicle);

        Constants.pref = getApplicationContext().getSharedPreferences("StarAutoAssist",
                MODE_PRIVATE);
        Constants.editor = Constants.pref.edit();
        mobileno = Constants.pref.getString("mobileno", null);

        new Get_Cars(VechicleActivity.this).execute();
        new MyCarList(VechicleActivity.this).execute();
        spinnertype = findViewById(R.id.vechicle_et_carmodel);
        spinnerbrand = findViewById(R.id.spin_carbrand);
        etplate = findViewById(R.id.vechicle_et_plateno);
        mycarListview = findViewById(R.id.mycarslistview);
        btnsubmit = findViewById(R.id.vechicle_btn_submit);




        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (spinnerbrand.getSelectedItem().toString().equals("")) {
                    Toast.makeText(VechicleActivity.this, "Please select brand name", Toast.LENGTH_SHORT).show();
                } else if (spinnertype.getSelectedItem().toString().equalsIgnoreCase("")) {
                    Toast.makeText(VechicleActivity.this, "Please enter model", Toast.LENGTH_SHORT).show();
                } else if (etplate.getText().toString().trim().equalsIgnoreCase("")) {
                    Toast.makeText(VechicleActivity.this, "Please enter licence plate number", Toast.LENGTH_SHORT).show();
                } else {
                    modelname = spinnertype.getSelectedItem().toString();
                    plateno = etplate.getText().toString().trim();
                    new Add_Vechicle_Async(VechicleActivity.this, mobileno, brandname, modelname, plateno,brandlogo,flatbedvalue).execute();
                }
            }
        });




    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(VechicleActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // For Internet checking
        Starautoassist_Application.registerReceiver(VechicleActivity.this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // For Internet disconnect checking
        Starautoassist_Application.unregisterReceiver(VechicleActivity.this);
    }

    public class Add_Vechicle_Async extends AsyncTask<String, Integer, String> {
        Context context;
        private String mobileno, brand, model, plateno,flatbedvalue,brandlogo;
        private String url = Constants.BaseURL + Constants.addcar;

        private Add_Vechicle_Async(Context ctx, String mobileno, String brand, String model, String plateno,String brandlogo,String flatbedvalue) {
            context = ctx;
            this.mobileno = mobileno;
            this.brand = brand;
            this.model = model;
            this.plateno = plateno;
            this.brandlogo=brandlogo;
            this.flatbedvalue=flatbedvalue;
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
                    .add("brand", brand)
                    .add("model", model)
                    .add("plateno", plateno)
                    .add("flatbed", flatbedvalue)
                    .add("logo", brandlogo)
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
                    Constants.editor.putBoolean("isCarAdded", true);
                    Constants.editor.putString("carlogo",brandlogo);
                    Constants.editor.apply();
                    finish();
                    Intent i = new Intent(VechicleActivity.this, HomeActivity.class);
                    startActivity(i);
                    Toast.makeText(getApplicationContext(), "Car details added successfully", Toast.LENGTH_SHORT).show();

                } else
                    Toast.makeText(getApplicationContext(), jonj.getString("message"), Toast.LENGTH_SHORT).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public class Get_Cars extends AsyncTask<String, Integer, String> {
        Context context;
        private String url = Constants.BaseURL + Constants.getcars;
        HashMap<String, String> map,map2;
        String id,carbrand,carmodel,carbrand2,flatbed,logo;
        private Get_Cars(Context ctx) {
            context = ctx;
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
            Log.v("result", "" + jsonData);
            JSONObject jonj = null;
            carlist.clear();
            String choosedbrand;
            HashMap<String,String> hashMap;
            try {
                jonj = new JSONObject(jsonData);
                if (jonj.getString("status").equalsIgnoreCase(
                        "success")) {
                    String data = jonj.getString("message");
                    String brandslist = jonj.getString("brands");
                    JSONArray array = new JSONArray(data);
                    JSONArray brandsarray = new JSONArray(brandslist);

                    for (int i = 0; i < array.length(); i++) {
                        map = new HashMap<String, String>();
                        JSONObject object = array.getJSONObject(i);
                        id = object.getString(Constants.id);
                        carmodel = object.getString(Constants.model);
                        carbrand = object.getString(Constants.brand);
                        flatbed = object.getString(Constants.flatbed);
                        logo = object.getString(Constants.logo);

                        map.put(Constants.id, id);
                        map.put(Constants.model, carmodel);
                        map.put(Constants.brand, carbrand);
                        map.put(Constants.flatbed, flatbed);
                        map.put(Constants.logo, logo);

                        carlist.add(map);
                    }
                    for(int j=0;j<brandsarray.length();j++){
                        map2=new HashMap<String, String>();
                        JSONObject object = brandsarray.getJSONObject(j);
                        carbrand2 = object.getString(Constants.brand);
                        brands.add(carbrand2);
                    }
                    brandadapter = new ArrayAdapter<String>(VechicleActivity.this, android.R.layout.simple_spinner_item, brands);
                    brandadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerbrand.setAdapter(brandadapter);
                    spinnerbrand.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                            model.clear();
                            brandname=brands.get(pos);

                            for(int i=0;i<carlist.size();i++){
                                String choosedbrand=carlist.get(i).get("brand");
                                if(brandname.equalsIgnoreCase(choosedbrand)){
                                    model.add(carlist.get(i).get("model"));
                                    brandlogo=carlist.get(i).get("logo");
                                    flatbedvalue=carlist.get(i).get("flatbed");
                                }
                            }

                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });

                    modeladapter = new ArrayAdapter<String>(VechicleActivity.this, android.R.layout.simple_spinner_item, model);
                    modeladapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnertype.setAdapter(modeladapter);

                } else
                    Toast.makeText(getApplicationContext(), jonj.getString("message"), Toast.LENGTH_SHORT).show();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public class MyCarList extends AsyncTask<String, Integer, String> {
        Context context;
        private String url = Constants.BaseURL + Constants.getmycars;
        HashMap<String, String> map;
        String id,carbrand,carmodel,logo;
        private MyCarList(Context ctx) {
            context = ctx;
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
            HashMap<String,String> hashMap;
            try {
                jonj = new JSONObject(jsonData);
                if (jonj.getString("status").equalsIgnoreCase(
                        "success")) {
                    String data = jonj.getString("message");
                    JSONArray array = new JSONArray(data);
                    mycars.clear();
                    for (int i = 0; i < array.length(); i++) {
                        map = new HashMap<String, String>();
                        JSONObject object = array.getJSONObject(i);
                        id = object.getString(Constants.cno);
                        carmodel = object.getString(Constants.model);
                        carbrand = object.getString(Constants.brand);
                        plateno = object.getString(Constants.plateno);
                        logo=object.getString(Constants.logo);

                        map.put(Constants.cno, id);
                        map.put(Constants.model, carmodel);
                        map.put(Constants.brand, carbrand);
                        map.put(Constants.plateno, plateno);
                        map.put(Constants.logo, logo);
                        mycars.add(map);
                    }
                    myCars_adapter=new MyCars_Adapter(VechicleActivity.this,mycars);
                    layoutManager = new LinearLayoutManager(VechicleActivity.this, LinearLayoutManager.VERTICAL, false);
                    mycarListview.setLayoutManager(layoutManager);
                    mycarListview.setAdapter(myCars_adapter);
                } else
                    Toast.makeText(getApplicationContext(), jonj.getString("message"), Toast.LENGTH_SHORT).show();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}

