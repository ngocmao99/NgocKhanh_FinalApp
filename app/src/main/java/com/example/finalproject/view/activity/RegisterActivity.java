package com.example.finalproject.view.activity;

import static com.example.finalproject.utils.Constants.EMAIL_REGEX;
import static com.example.finalproject.utils.Constants.NAME_REGEX1;
import static com.example.finalproject.utils.Constants.NAME_REGEX2;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.finalproject.R;
import com.example.finalproject.databinding.ActivityRegisterBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Turn off header bar of android
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);

        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        mAuth = FirebaseAuth.getInstance();

        textChangeWatcher();

        binding.btnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createUser();
            }
        });

        binding.login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class).addFlags(
                        Intent.FLAG_ACTIVITY_CLEAR_TASK
                ));
            }
        });

        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class).addFlags(
                        Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP
                ));
            }
        });

    }

    private void textChangeWatcher() {
        binding.tietNameInput.addTextChangedListener(tw_name);
        binding.tietEmailRegisterInput.addTextChangedListener(tw_email);

    }
    // Validate password. The passwords regex includes:
    //(?=.*[0-9])       # a digit must occur at least once
    //(?=.*[a-z])       # a lower case letter must occur at least once
    //(?=.*[A-Z])       # an upper case letter must occur at least once
    //(?=.*[@#$%^&+=])  # a special character must occur at least once you can replace with your special characters
    //(?=\\S+$)          # no whitespace allowed in the entire string
    // .{4,}             # anything, at least six places though

    private final TextWatcher tw_passwords = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            String password = editable.toString().trim();
            if(editable.length() == 0){
                binding.tilPasswordRegisterInput.setErrorEnabled(true);
                binding.tilPasswordRegisterInput.setError(getString(R.string.error_input_password_signup));
                binding.tilPasswordRegisterInput.requestFocus();
            }

            if(editable.length() >=0 && editable.length() < 6){
                binding.tilPasswordRegisterInput.setErrorEnabled(true);
                binding.tilPasswordRegisterInput.setError(getString(R.string.error_password_less_than_six_letters));
            }



        }
    };


    //TextWatcher email include email lenght not empty,contain letters @
    private final TextWatcher tw_email = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            String email = editable.toString().trim();

            if (editable.length() == 0) {
                binding.tilEmailRegisterInput.setErrorEnabled(true);
                binding.tilEmailRegisterInput.setError(getString(R.string.error_input_email_empty));
                binding.tilEmailRegisterInput.requestFocus();
            }

            if (!email.matches(EMAIL_REGEX)) {
                binding.tilEmailRegisterInput.setErrorEnabled(true);
                binding.tilEmailRegisterInput.setError(getString(R.string.error_invalid_email));
                binding.tilEmailRegisterInput.requestFocus();

            } else {
                binding.tilEmailRegisterInput.setErrorEnabled(false);
            }

        }
    };

    private final TextWatcher tw_name = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        //TextWatcher Name fields include names length > 1 and not contain number or unique letters
        @Override
        public void afterTextChanged(Editable editable) {
            String name = editable.toString().trim();
            if (editable.length() == 0) {
                binding.tilName.setErrorEnabled(true);
                binding.tilName.setError(getString(R.string.error_name_signup));
                binding.tilName.requestFocus();
            }
            if (!name.matches(NAME_REGEX1) || name.matches(NAME_REGEX2)) {
                binding.tilName.setErrorEnabled(true);
                binding.tilName.setError(getString(R.string.error_name_unique_signup));
                binding.tilName.requestFocus();
            } else {
                binding.tilName.setErrorEnabled(false);
            }

        }


    };

    private void createUser() {
        String name = Objects.requireNonNull(binding.tietNameInput.getText().toString());
        String email = Objects.requireNonNull(binding.tietEmailRegisterInput.getText().toString());
        String password = Objects.requireNonNull(binding.tietPasswordRegisterInput.getText().toString());
        String confirmPass = Objects.requireNonNull(binding.tietPasswordConfirm.getText().toString());
        if (TextUtils.isEmpty(name)) {
            binding.tilName.setErrorEnabled(true);
            binding.tilName.setError("Name cannot be empty");
            binding.tilName.requestFocus();
        }
        if (TextUtils.isEmpty(email)) {
            binding.tilEmailRegisterInput.setErrorEnabled(true);
            binding.tilEmailRegisterInput.setError("Email cannot be empty");
            binding.tilEmailRegisterInput.requestFocus();
        }
        if (TextUtils.isEmpty(password)) {
            binding.tilPasswordRegisterInput.setErrorEnabled(true);
            binding.tilPasswordRegisterInput.setError(getString(R.string.error_input_password_signup));
            binding.tilPasswordRegisterInput.requestFocus();
        }

        if (TextUtils.isEmpty(confirmPass)) {
            binding.tilPasswordConfirmInput.setErrorEnabled(true);
            binding.tilPasswordConfirmInput.setError(getString(R.string.error_input_confirm_password_signup));
            binding.tilPasswordConfirmInput.requestFocus();
        }
        if (!password.equals(confirmPass)) {
            binding.tilPasswordRegisterInput.setErrorEnabled(true);
            binding.tilPasswordRegisterInput.setError(getString(R.string.error_input_password_notmatch_signup));
            binding.tilPasswordConfirmInput.setErrorEnabled(true);
            binding.tilPasswordConfirmInput.setError(getString(R.string.error_input_password_notmatch_signup));
            binding.tilPasswordConfirmInput.requestFocus();

        }
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPass) ||
                !password.equals(confirmPass)) {
            Toast.makeText(RegisterActivity.this, "Please fill out the required field!!!", Toast.LENGTH_SHORT).show();
        } else {
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(RegisterActivity.this, "User registered successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                    } else {
                        Toast.makeText(RegisterActivity.this, "Registration Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

}