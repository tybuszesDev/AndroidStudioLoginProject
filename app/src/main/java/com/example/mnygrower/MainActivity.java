package com.example.mnygrower;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button loginButton;
    private EditText etPassword, etEmail;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginButton = findViewById(R.id.btnLogin);
        etEmail = findViewById(R.id.etMail);
        etPassword = findViewById(R.id.etPassword);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()){
                    startActivity(new Intent(MainActivity.this, SecondActivity.class));
                }
                else Toast.makeText(MainActivity.this, "Check your login data and try again", Toast.LENGTH_SHORT).show();

            }
        });
    }
    protected boolean validate(){
        String pass, name;
        pass = etPassword.getText().toString();
        name = etEmail.getText().toString();
        if (pass.equals("tomek1") && name.equals("tomek1")) return true;
        else return false;
    }
}
