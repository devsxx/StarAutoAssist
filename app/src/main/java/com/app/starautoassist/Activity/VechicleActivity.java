package com.app.starautoassist.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.app.starautoassist.R;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

public class VechicleActivity extends AppCompatActivity {

    private SearchableSpinner spinnerbrand, spinnertype;
    private Button btnsubmit;
    private EditText etmodel, etplate;

    String[] brand = { "Alfa Romeo", "Aston Martin", "Audi", "Bentley", "Citroen", "Ferrari",
            "BMW", "Bufori", "Caterham", "Chana", "Chery", "Chevrolet", "Fiat" , "Ford",
            "Haval", "Honda", "Hyundai", "Infiniti", "Isuzu", "Jaguar", "Jeep", "Kia",
            "Lamborghini", "Land Rover", "Lexus", "Lotus", "Mahindra", "Maserati", "Maxus",
            "Mazda", "McLaren", "Mercedes-Benz", "MINI", "Mitsubishi", "Nissan", "Perodua",
            "Peugeot", "Porsche", "Proton", "Renault", "Rolls-Royce", "Skoda", "SsangYong",
            "Subaru", "Suzuki", "Tata", "Toyota", "Volkswagen", "Volvo" };

    String[] type = { "Sedan", "Hatchback", "MPV", "SUV", "Pickup", "Wagon", "Coupe",
            "Convertible", "Commercial" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("My Vechicle");
        setContentView(R.layout.activity_vechicle);

        btnsubmit = findViewById(R.id.vechicle_btn_submit);
        etmodel = findViewById(R.id.vechicle_et_carmodel);
        etplate = findViewById(R.id.vechicle_et_plateno);
        spinnerbrand = findViewById(R.id.spin_carbrand);
        spinnertype = findViewById(R.id.spin_cartype);
        spinnerbrand.setPositiveButton("OK");
        spinnertype.setPositiveButton("OK");

        ArrayAdapter<String> brandadapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, brand);
        brandadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerbrand.setAdapter(brandadapter);

        ArrayAdapter<String> typeadapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, type);
        typeadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnertype.setAdapter(typeadapter);

        spinnerbrand.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Toast.makeText(getApplicationContext(), spinnerbrand.getSelectedItem().toString() + "\tis Selected", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnertype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Toast.makeText(getApplicationContext(), spinnertype.getSelectedItem().toString()+ "\tis Selected", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}
