package com.example.mnygrower;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class TaxiDriverActivity extends AppCompatActivity {

    TextView LicenseDate;
    TextView NIP;
    TextView SideNumber;
    TextView LicenseNumber;
    TextView Plate;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    Button Back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taxi_driver);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        LicenseDate = findViewById(R.id.tvLicenseDate);
        NIP = findViewById(R.id.tvNIP);
        SideNumber = findViewById(R.id.tvSideNumber);
        LicenseNumber = findViewById(R.id.tvLicenseNumber);
        Plate = findViewById(R.id.tvPlate);
        Back = findViewById(R.id.btnBack);

        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(TaxiDriverActivity.this, SecondActivity.class));
            }
        });

       // DatabaseReference databaseReference = firebaseDatabase.getReference(String.valueOf(firebaseDatabase.getReference("GA 2429C")));



    }
}
