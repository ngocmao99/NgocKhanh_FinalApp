package com.example.finalproject.view.activity;

import static com.example.finalproject.utils.Constants.EMAIL_REGEX;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.finalproject.R;
import com.example.finalproject.databinding.ActivityLoginBinding;
import com.example.finalproject.view.activity.dialog.LoadingDialogCustom;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    //Declare viewBinding alternative for findViewById
    private ActivityLoginBinding binding;

    private FirebaseAuth mAuth;
    
    private LoadingDialogCustom loadDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Turn off header bar of android
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        //apply view binding
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        mAuth = FirebaseAuth.getInstance();

        textWatcher();

        //Declare loading dialog custom
        loadDialog = new LoadingDialogCustom(LoginActivity.this);

        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateInput();
            }
        });

        binding.signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                Animatoo.animateSlideLeft(LoginActivity.this);
            }
        });

        binding.txtForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ForgetPassword.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|
                        Intent.FLAG_ACTIVITY_CLEAR_TOP));
                Animatoo.animateSlideRight(LoginActivity.this);
            }
        });

    }

    private void textWatcher() {
        binding.tietEmailInput.addTextChangedListener(tw_email);
        binding.tietPasswordInput.addTextChangedListener(tw_passwords);

    }
    private final TextWatcher tw_passwords = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (editable.length() == 0) {
                binding.tilPasswordInput.setErrorEnabled(true);
                binding.tilPasswordInput.setError(getString(R.string.error_input_password_signup));
                binding.tilPasswordInput.requestFocus();
            }

            if (editable.length() >= 0 && editable.length() < 8) {
                binding.tilPasswordInput.setErrorEnabled(true);
                binding.tilPasswordInput.setError(getString(R.string.error_password_less_than_six_letters));
                binding.tilPasswordInput.requestFocus();
            } else {
                binding.tilPasswordInput.setErrorEnabled(false);

            }

        }
    };
    
    private final TextWatcher tw_email = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            String email = s.toString().trim();

            if (s.length() == 0) {
                binding.tilEmailInput.setErrorEnabled(true);
                binding.tilEmailInput.setError(getString(R.string.error_input_email_empty));
                binding.tilEmailInput.requestFocus();
            }

            if (!email.matches(EMAIL_REGEX)) {
                binding.tilEmailInput.setErrorEnabled(true);
                binding.tilEmailInput.setError(getString(R.string.error_invalid_email));
                binding.tilEmailInput.requestFocus();

            } else {
                binding.tilEmailInput.setErrorEnabled(false);
            }

        }
    };

    private void validateInput(){
        String email = Objects.requireNonNull(binding.tietEmailInput.getText().toString());
        String password = Objects.requireNonNull(binding.tietPasswordInput.getText().toString());

        if (TextUtils.isEmpty(email)){
            binding.tilEmailInput.setErrorEnabled(true);
            binding.tilEmailInput.setError(getString(R.string.error_input_email_empty));
            binding.tilEmailInput.requestFocus();
        }
        if (TextUtils.isEmpty(password)){
            binding.tilPasswordInput.setErrorEnabled(true);
            binding.tilPasswordInput.setError(getString(R.string.error_input_password_signup));
            binding.tilPasswordInput.requestFocus();
        }
        if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){
            Toast.makeText(LoginActivity.this,getText(R.string.error_empty_credential),Toast.LENGTH_SHORT).show();
        }
        else{
            loginWithCheck(email,password);
        }
    }

    private void loginWithCheck(String email, String password) {
        loadDialog.showLoadingDialog();

        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){

                    if(!mAuth.getCurrentUser().isEmailVerified()){
                        loadDialog.hideLoadingDialog();
                        Toast.makeText(LoginActivity.this,getText(R.string.error_not_verified_email), Toast.LENGTH_SHORT).show();
                    }
                    else{
                        loadDialog.hideLoadingDialog();
                        //TODO: turn off note mode for startActivity() move to Home Activity.
//                        startActivity(new Intent(LoginActivity.this,HomeActivity.class).addFlags(
//                                Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP));
//                        Animatoo.animateSlideLeft(LoginActivity.this);
                    }

                }else{
                    loadDialog.hideLoadingDialog();
                    Toast.makeText(LoginActivity.this,task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}