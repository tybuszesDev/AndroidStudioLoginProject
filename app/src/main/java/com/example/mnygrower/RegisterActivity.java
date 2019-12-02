package com.example.mnygrower;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class RegisterActivity extends AppCompatActivity {

    TextView backToLogin;
    FirebaseAuth firebaseAuth;
    EditText registerName, registerMail, registerPass, registerPass2;
    Switch accept;
    Button regButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        registerName =findViewById(R.id.etRegisterName);
        registerMail = findViewById(R.id.etRegisterEmail);
        registerPass = findViewById(R.id.etRegisterPassword);
        registerPass2 = findViewById(R.id.etRegisterPassword2);
        accept = findViewById(R.id.sRegisterAccept);
        backToLogin = findViewById(R.id.tvRegisterBackLogin);
        regButton = findViewById(R.id.btnRegisterButton);

        firebaseAuth = FirebaseAuth.getInstance();

        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()){
                    String user_email = registerMail.getText().toString().trim();
                    String user_password = registerPass.getText().toString().trim();

                    firebaseAuth.createUserWithEmailAndPassword(user_email, user_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()) {
                                Toast.makeText(RegisterActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                            }
                            else{
                                Toast.makeText(RegisterActivity.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
                else Toast.makeText(RegisterActivity.this, "Rejestracja nie powiodła się", Toast.LENGTH_SHORT).show();
            }
        });

        backToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
            }
        });


    }
    private boolean validate(){
        if(!registerPass.getText().toString().equals(registerPass2.getText().toString()) ||
                registerName.getText().toString().isEmpty() ||
                registerMail.getText().toString().isEmpty() ||
                registerPass.getText().toString().isEmpty() ||
                registerPass2.getText().toString().isEmpty()
        )
            return false;
        else if (accept.isChecked()){
            return true;
        }
        else return false;


    }
}
