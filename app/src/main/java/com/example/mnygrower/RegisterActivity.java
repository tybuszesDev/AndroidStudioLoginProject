package com.example.mnygrower;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;


public class RegisterActivity extends AppCompatActivity {

    TextView backToLogin;
    FirebaseAuth firebaseAuth;
    EditText registerName, registerMail, registerPass, registerPass2, userAge;
    Switch accept;
    Button regButton;
    String email, name, age, password;
    TextView termsText;
    ImageView userProfilePic;
    private FirebaseStorage firebaseStorage;
    private static int PICK_IMAGE = 123;
    Uri imagePath;
    private StorageReference storageReference;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == PICK_IMAGE && resultCode == RESULT_OK && data.getData() != null){
            imagePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),imagePath);
                userProfilePic.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

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
        termsText = findViewById(R.id.terms_text);
        userProfilePic = findViewById(R.id.ivProfile);



        firebaseAuth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();

        storageReference = firebaseStorage.getReference();

        userProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");    //application/*  audio/mp3 doc/pdf
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select image"),PICK_IMAGE);
            }
        });



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
                                finish();
                                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                                firebaseAuth.signOut();
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


        termsText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"));
                startActivity(browserIntent);
            }
        });


    }
    private boolean validate(){
        if(!registerPass.getText().toString().equals(registerPass2.getText().toString()) ||
                registerName.getText().toString().isEmpty() ||
                registerMail.getText().toString().isEmpty() ||
                registerPass.getText().toString().isEmpty() ||
                registerPass2.getText().toString().isEmpty() ||
                imagePath == null ||
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
        StorageReference imageReference = storageReference.child(firebaseAuth.getUid()).child("Images").child("Profile Pic"); // User Id/Images/profille_pic
        UploadTask uploadTask = imageReference.putFile(imagePath);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RegisterActivity.this, "Uplad failed", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(RegisterActivity.this, "Upload Successfull", Toast.LENGTH_SHORT).show();
            }
        });
        UserProfile userProfile = new UserProfile(age, email , name);
        myRef.setValue(userProfile);
    }
}
