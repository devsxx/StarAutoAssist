package com.app.starautoassist.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.app.starautoassist.Others.Constants;
import com.app.starautoassist.Others.Starautoassist_Application;
import com.app.starautoassist.R;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class VechicleActivity extends AppCompatActivity {

    private Spinner spinnerbrand, spinnertype;
    private Button btnsubmit;
    private EditText etmodel, etplate;
    String brandname,modelname,plateno;
    Integer brandposition;
    ArrayAdapter<String> brandadapter;

    String mobileno;
    String[] brand = { "Alfa Romeo", "Aston Martin", "Audi", "Bentley", "Citroen", "Ferrari",
            "BMW", "Bufori", "Caterham", "Chana", "Chery", "Chevrolet", "Fiat" , "Ford",
            "Haval", "Honda", "Hyundai", "Infiniti", "Isuzu", "Jaguar", "Jeep", "Kia",
            "Lamborghini", "Land Rover", "Lexus", "Lotus", "Mahindra", "Maserati", "Maxus",
            "Mazda", "McLaren", "Mercedes-Benz", "MINI", "Mitsubishi", "Nissan", "Perodua",
            "Peugeot", "Porsche", "Proton", "Renault", "Rolls-Royce", "Skoda", "SsangYong",
            "Subaru", "Suzuki", "Tata", "Toyota", "Volkswagen", "Volvo" };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("My Vechicle");
        setContentView(R.layout.activity_vechicle);

        Constants.pref = getApplicationContext().getSharedPreferences("StarAutoAssist",
                MODE_PRIVATE);
        Constants.editor = Constants.pref.edit();
        mobileno=Constants.pref.getString("mobileno",null);
        btnsubmit = findViewById(R.id.vechicle_btn_submit);
        etmodel = findViewById(R.id.vechicle_et_carmodel);
        etplate = findViewById(R.id.vechicle_et_plateno);
        spinnerbrand = findViewById(R.id.spin_carbrand);
        //spinnerbrand.setPositiveButton("OK");
        brandadapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, brand);
        brandadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerbrand.setAdapter(brandadapter);
        Boolean isCarAdded=Constants.pref.getBoolean("isCarAdded",false);
        if(isCarAdded) {
            Integer pos = Constants.pref.getInt("brandposition", 0);
            String compareValue=Constants.pref.getString("brand","");
            if (compareValue != null) {
                int spinnerPosition = brandadapter.getPosition(compareValue);
            }
            spinnerbrand.setSelection(pos);
            etmodel.setText(Constants.pref.getString("model", null));
            etplate.setText(Constants.pref.getString("plateno", null));
            btnsubmit.setText("Update");
        }
        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(brandname.equals("")){
                    Toast.makeText(VechicleActivity.this, "Please select brand name", Toast.LENGTH_SHORT).show();
                }else if(etmodel.getText().toString().trim().equalsIgnoreCase("")){
                    Toast.makeText(VechicleActivity.this, "Please enter model", Toast.LENGTH_SHORT).show();
                }else if(etplate.getText().toString().trim().equalsIgnoreCase("")){
                    Toast.makeText(VechicleActivity.this, "Please enter licence plate number", Toast.LENGTH_SHORT).show();
                }else {
                    modelname=etmodel.getText().toString().trim();
                    plateno=etplate.getText().toString().trim();
                    new Add_Vechicle_Async(VechicleActivity.this,mobileno,brandname,modelname,plateno).execute();
                }
            }
        });




        spinnerbrand.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                brandposition=position;
                brandname=spinnerbrand.getSelectedItem().toString().trim();
                Toast.makeText(getApplicationContext(), brandname + "\tis Selected", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

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
        private String mobileno,brand,model,plateno;
        private String url = Constants.BaseURL + Constants.addcar;

        private Add_Vechicle_Async(Context ctx, String mobileno,String brand,String model,String plateno) {
            context = ctx;
            this.mobileno = mobileno;
            this.brand = brand;
            this.model = model;
            this.plateno = plateno;
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
                    Constants.editor.putBoolean("isCarAdded",true);
                    Constants.editor.putString("brand",brandname);
                    Constants.editor.putInt("brandposition",brandposition);
                    Constants.editor.putString("model",modelname);
                    Constants.editor.putString("plateno",plateno);
                    Constants.editor.commit();
                    Constants.editor.apply();
                    finish();

                    Toast.makeText(getApplicationContext(),"Car details added successfully",Toast.LENGTH_SHORT).show();

                }else Toast.makeText(getApplicationContext(),jonj.getString("message"),Toast.LENGTH_SHORT).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
