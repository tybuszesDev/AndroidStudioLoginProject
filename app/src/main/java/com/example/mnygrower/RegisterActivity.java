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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class RegisterActivity extends AppCompatActivity {

    TextView backToLogin;
    FirebaseAuth firebaseAuth;
    EditText registerName, registerMail, registerPass, registerPass2, userAge;
    Switch accept;
    Button regButton;
    String email, name, age, password;

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
        userAge = findViewById(R.id.etAge);



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
                                sendUserData();
                                sendEmailVerification();
                                Toast.makeText(RegisterActivity.this, "Successfully Registered, Verification mail sent!", Toast.LENGTH_SHORT).show();
                                //firebaseAuth.signOut();
                                finish();
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
                registerPass2.getText().toString().isEmpty() ||
                userAge.getText().toString().isEmpty()
        )
            return false;
        else if (accept.isChecked()){
            return true;
        }
        else return false;


    }
    private void sendEmailVerification(){
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser!=null){
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        sendUserData();
                        Toast.makeText(RegisterActivity.this, "Successfully Registered, Verification mail sent!", Toast.LENGTH_SHORT).show();
                        firebaseAuth.signOut();
                        finish();
                        startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                    }else{
                        Toast.makeText(RegisterActivity.this, "Verification mail hasnt been sent!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void sendUserData(){
        name = registerName.getText().toString();
        email = registerMail. getText().toString();
        age = userAge.getText().toString();
        password = registerPass.getText().toString();

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myRef = firebaseDatabase.getReference(firebaseAuth.getUid());
        UserProfile userProfile = new UserProfile(age, email , name);
        myRef.setValue(userProfile);
    }
}
