package com.example.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {
    TextInputEditText RName;
    TextInputEditText RegEmail;
    TextInputEditText RegPassword;
    TextView LoginHere;
    Button btnRegister;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        RName = findViewById(R.id.tiet_nameInput);
        RegEmail = findViewById(R.id.tiet_emailRegisterInput);
        RegPassword = findViewById(R.id.tiet_passwordRegisterInput);
        LoginHere = findViewById(R.id.Under_login2);
        btnRegister = findViewById(R.id.btnregister);

        mAuth = FirebaseAuth.getInstance();

        btnRegister.setOnClickListener(view ->{
            createUser();
        });

        LoginHere.setOnClickListener(view ->{
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
        });
    }

    private void createUser(){
        String name = RName.getText().toString();
        String email = RegEmail.getText().toString();
        String password = RegPassword.getText().toString();
        if (TextUtils.isEmpty(name)) {
            TextInputLayout til = findViewById(R.id.til_NameInput);
            til.setError("Name cannot be empty");
            til.requestFocus();
        }if (TextUtils.isEmpty(email)){
            TextInputLayout til = findViewById(R.id.til_EmailRegisterInput);
            til.setError("Email cannot be empty");
            til.requestFocus();
        }else if (TextUtils.isEmpty(password)){
            TextInputLayout til = findViewById(R.id.til_PasswordRegisterInput);
            til.setError("Password cannot be empty");
            til.requestFocus();
        }else{
            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(RegisterActivity.this, "User registered successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                    }else{
                        Toast.makeText(RegisterActivity.this, "Registration Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

}