package com.app.starautoassist.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.app.starautoassist.R;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

public class Fuel_Activity extends AppCompatActivity {

   /* private MapView mapView;*/
    private SearchableSpinner spinnerprice, spinnerfuel;
    private TextView tvlitre;
    private Button btnproceed;
    String[] price = { "RM10", "RM30", "RM50", "RM70", "RM90", "RM110", "RM130", "RM150"};
    String[] fuel = { "RON 95", "RON 97", "Diesel"};
    float fuelprice;
    int setprice0 = 10, setprice1 = 30, setprice2 = 50, setprice3 = 70, setprice4 = 90, setprice5 = 110, setprice6 = 130, setprice7 = 150;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Fuel Service");
        setContentView(R.layout.activity_fuel);

        /*mapView = findViewById(R.id.mapView);*/
        spinnerprice = findViewById(R.id.spin_price);
        spinnerfuel = findViewById(R.id.spin_fuel);
        tvlitre = findViewById(R.id.fuel_tv_litre);
        btnproceed = findViewById(R.id.fuel_btn_proceed);

        spinnerprice.setPositiveButton("OK");
        spinnerfuel.setPositiveButton("OK");

        ArrayAdapter<String> adapterprice = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, price);
        adapterprice.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerprice.setAdapter(adapterprice);

        ArrayAdapter<String> adapterfuel = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, fuel);
        adapterfuel.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerfuel.setAdapter(adapterfuel);

        spinnerfuel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0){
                     fuelprice = (float) 2.20;
                }else if (position == 1){
                    fuelprice = (float) 2.47;
                }else if (position == 2){
                    fuelprice = (float) 2.18;
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

                if (position == 0){
                    float result = (setprice0 / fuelprice);
                    String price = String.format("%.2f", result);
                    tvlitre.setText(price + "\tLitres");

                }else if (position == 1){
                    float result = (setprice1 / fuelprice);
                    String price = String.format("%.2f", result);
                    tvlitre.setText(price + "\tLitres");

                }else if (position == 2){
                    float result = (setprice2 / fuelprice);
                    String price = String.format("%.2f", result);
                    tvlitre.setText(price + "\tLitres");

                }else if (position == 3){
                    float result = (setprice3 / fuelprice);
                    String price = String.format("%.2f", result);
                    tvlitre.setText(price + "\tLitres");

                }else if (position == 4){
                    float result = (setprice4 / fuelprice);
                    String price = String.format("%.2f", result);
                    tvlitre.setText(price + "\tLitres");

                }else if (position == 5){
                    float result = (setprice5 / fuelprice);
                    String price = String.format("%.2f", result);
                    tvlitre.setText(price + "\tLitres");

                }else if (position == 6){
                    float result = (setprice6 / fuelprice);
                    String price = String.format("%.2f", result);
                    tvlitre.setText(price + "\tLitres");

                }else if (position == 7){
                    float result = (setprice7 / fuelprice);
                    String price = String.format("%.2f", result);
                    tvlitre.setText(price + "\tLitres");

                }
                Toast.makeText(getApplicationContext(), spinnerprice.getSelectedItem().toString() + "\tis Selected", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}
