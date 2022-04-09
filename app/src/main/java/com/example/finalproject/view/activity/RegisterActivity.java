package com.example.finalproject.view.activity;

import static com.example.finalproject.utils.Constants.EMAIL_REGEX;
import static com.example.finalproject.utils.Constants.NAME_REGEX1;
import static com.example.finalproject.utils.Constants.NAME_REGEX2;
import static com.example.finalproject.utils.Constants.OTHER;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.finalproject.R;
import com.example.finalproject.databinding.ActivityRegisterBinding;
import com.example.finalproject.view.activity.dialog.LoadingDialogCustom;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;

    private FirebaseAuth mAuth;

    private DatabaseReference mDatabase;

    private LoadingDialogCustom dialogLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Enable softInputMode PAN
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        //Turn off header bar of android
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);

        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        mAuth = FirebaseAuth.getInstance();

        mDatabase = FirebaseDatabase.getInstance().getReference();

        dialogLoading = new LoadingDialogCustom(RegisterActivity.this);
        textChangeWatcher();

        binding.btnregister.setOnClickListener(view1 -> validateSignup());

        binding.login.setOnClickListener(view12 -> startActivity(new Intent(RegisterActivity.this, LoginActivity.class).addFlags(
                Intent.FLAG_ACTIVITY_CLEAR_TASK
        )));

        binding.btnBack.setOnClickListener(view13 -> startActivity(new Intent(RegisterActivity.this, LoginActivity.class).addFlags(
                Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP
        )));

    }

    private void textChangeWatcher() {
        binding.tietNameInput.addTextChangedListener(tw_name);
        binding.tietEmailRegisterInput.addTextChangedListener(tw_email);
        binding.tietPasswordRegisterInput.addTextChangedListener(tw_passwords);
        binding.tietPasswordConfirm.addTextChangedListener(tw_confirmPassword);

    }

    //TextWatcher: validation confirmation passwords of signup screen
    private final TextWatcher tw_confirmPassword = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            String password = Objects.requireNonNull(binding.tietPasswordRegisterInput.getText()).toString().trim();
            String confirmPassword = editable.toString().trim();

            if (editable.length() == 0) {
                binding.tilPasswordConfirmInput.setErrorEnabled(true);
                binding.tilPasswordConfirmInput.setError(getString(R.string.error_input_confirm_password_signup));
                binding.tilPasswordConfirmInput.requestFocus();
            }

            if (!confirmPassword.equals(password)) {
                binding.tilPasswordConfirmInput.setErrorEnabled(true);
                binding.tilPasswordConfirmInput.setError(getString(R.string.error_input_password_unmatch_signup));
                binding.tilPasswordConfirmInput.requestFocus();
            } else {
                binding.tilPasswordConfirmInput.setErrorEnabled(false);
            }

        }
    };

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
                binding.tilPasswordRegisterInput.setErrorEnabled(true);
                binding.tilPasswordRegisterInput.setError(getString(R.string.error_input_password_signup));
                binding.tilPasswordRegisterInput.requestFocus();
            }

            if (editable.length() >= 0 && editable.length() < 8) {
                binding.tilPasswordRegisterInput.setErrorEnabled(true);
                binding.tilPasswordRegisterInput.setError(getString(R.string.error_password_less_than_six_letters));
                binding.tilPasswordRegisterInput.requestFocus();
            } else {
                binding.tilPasswordRegisterInput.setErrorEnabled(false);

            }

        }
    };


    //TextWatcher email include email length not empty,contain letters @
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

    private void validateSignup() {
        String name = Objects.requireNonNull(Objects.requireNonNull(binding.tietNameInput.getText()).toString());
        String email = Objects.requireNonNull(Objects.requireNonNull(binding.tietEmailRegisterInput.getText()).toString());
        String password = Objects.requireNonNull(Objects.requireNonNull(binding.tietPasswordRegisterInput.getText()).toString());
        String confirmPass = Objects.requireNonNull(Objects.requireNonNull(binding.tietPasswordConfirm.getText()).toString());

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
            binding.tilPasswordConfirmInput.setErrorEnabled(true);
            binding.tilPasswordConfirmInput.setError(getString(R.string.error_input_password_unmatch_signup));
            binding.tilPasswordConfirmInput.requestFocus();

        }

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPass) ||
                !password.equals(confirmPass)) {
            Toast.makeText(RegisterActivity.this, "Please fill out the required field!!!", Toast.LENGTH_SHORT).show();
        } else {
            registerUser(name, email, password);
        }
    }

    private void registerUser(final String name, final String email, final String password) {
        dialogLoading.showLoadingDialog();

        mAuth.createUserWithEmailAndPassword(email,password).addOnSuccessListener(authResult -> {
            HashMap<String, Object> userInfo = new HashMap<>();
            userInfo.put("fullName",name);
            userInfo.put("email",email);
            userInfo.put("userImgId","");
            userInfo.put("dob","");
            userInfo.put("phoneNumber","");
            userInfo.put("gender",OTHER);

            userInfo.put("userId",Objects.requireNonNull(Objects.requireNonNull(mAuth.getCurrentUser()).getUid()));

            mDatabase.child("Users").child(mAuth.getCurrentUser().getUid()).setValue(userInfo).addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
                            .setDisplayName(name).build();
                    Objects.requireNonNull(user).updateProfile(profileChangeRequest);

                    //Sending verification link to email of clients
                    mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()){
                            dialogLoading.hideLoadingDialog();
                            Toast.makeText(RegisterActivity.this,getString(R.string.ta_successful_registration),Toast.LENGTH_SHORT)
                                    .show();
                            startActivity(new Intent(RegisterActivity.this,LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|
                                    Intent.FLAG_ACTIVITY_CLEAR_TOP));
                            finish();
                        }
                        else {
                            dialogLoading.hideLoadingDialog();
                            Toast.makeText(RegisterActivity.this, Objects.requireNonNull(task1.getException()).getMessage(),Toast.LENGTH_SHORT).show();
                        }


                    });


                }
            }).addOnFailureListener(e -> {
                dialogLoading.hideLoadingDialog();
                Toast.makeText(RegisterActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
            });
        }).addOnFailureListener(e -> {
            dialogLoading.hideLoadingDialog();
            Toast.makeText(RegisterActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
        });


    }
}