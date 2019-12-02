package com.example.mnygrower;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private Button loginButton;
    private EditText etPassword, etEmail;
    private TextView Registration;
    private FirebaseAuth firebaseAuth;
    private int attempts = 5;
    private ProgressDialog progressDialog;
    private TextView forgotpassword;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginButton = findViewById(R.id.btnLogin);
        etEmail = findViewById(R.id.etMail);
        etPassword = findViewById(R.id.etPassword);
        Registration = findViewById(R.id.tvRegister);
        forgotpassword = findViewById(R.id.tvPasswordRemember);

        firebaseAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);

        FirebaseUser user = firebaseAuth. getCurrentUser();

        /*if(user != null){
            finish();
            startActivity(new Intent(MainActivity.this, SecondActivity.class));   //NIEWYLOGOWYWANIE
        }*/

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etEmail.getText().toString().isEmpty() || etPassword.getText().toString().isEmpty()){
                    Toast.makeText(MainActivity.this, "Podaj Email i Hasło", Toast.LENGTH_SHORT).show();
                }else  validate(etEmail.getText().toString(), etPassword.getText().toString());

            }
        });

        Registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, RegisterActivity.class));
            }
        });

        forgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, PasswordActivity.class));
            }
        });
    }
    protected void validate(String userName, String userPassword) {

        progressDialog.setMessage("Check my other Apps !");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(userName, userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    progressDialog.dismiss();
                    //Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();  //Odkomentuj przy niewymaganej weryfikacji
                    //startActivity(new Intent(MainActivity.this, SecondActivity.class));                //           - || -
                    checkEmailVerification();                                                            // zakomentuj przy niewymaganej weryfikacji
                } else {
                    attempts -= 1;

                    Toast.makeText(MainActivity.this, "Login failed Attepmts left: " + attempts, Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
                if (attempts == 0) loginButton.setEnabled(false);
            }

        });
    }

    private void checkEmailVerification(){
        FirebaseUser firebaseUser = firebaseAuth.getInstance().getCurrentUser();
        Boolean emailflag = firebaseUser.isEmailVerified();

        if(emailflag){
            finish();
            startActivity(new Intent(MainActivity.this, SecondActivity.class));
        }else{
            Toast.makeText(this, "Verify your email !", Toast.LENGTH_SHORT).show();
            firebaseAuth.signOut();
        }
    }
}

