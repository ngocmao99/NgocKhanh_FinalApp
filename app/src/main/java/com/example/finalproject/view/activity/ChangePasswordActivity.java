package com.example.finalproject.view.activity;

import static com.example.finalproject.R.color.first;
import static com.example.finalproject.utils.Constants.PASSWORD_REGEX;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.finalproject.R;
import com.example.finalproject.base.BaseActivity;
import com.example.finalproject.databinding.ActivityChangePasswordBinding;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

import es.dmoral.toasty.Toasty;

public class ChangePasswordActivity extends BaseActivity {
    private ActivityChangePasswordBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChangePasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
        updatePassword();
        watcherInput();
        handleBackButton();
    }
    private void init(){
        mAuth = FirebaseAuth.getInstance();
    }

    private void updatePassword() {
        binding.btnUpdate.setOnClickListener(view -> validate());
    }

    //validate input
    public void validate(){
        String currentPw = Objects.requireNonNull(binding.tietCurrentPw.getText().toString());
        String newPw = Objects.requireNonNull(binding.tietNewPw.getText().toString());
        String confirmPw = Objects.requireNonNull(binding.tietConfirmPw.getText().toString());

        if(TextUtils.isEmpty(currentPw)){
            binding.tilCurrentPw.setErrorEnabled(true);
            binding.tilCurrentPw.setError(getString(R.string.error_current_pw_empty));
            binding.tilCurrentPw.requestFocus();
        }

        if (TextUtils.isEmpty(newPw)){
            binding.tilNewPw.setErrorEnabled(true);
            binding.tilNewPw.setError(getString(R.string.error_new_pw_empty));
            binding.tilNewPw.requestFocus();
        }
        // Validate password format
        //Password must contain at least one digit [0-9].
        //Password must contain at least one lowercase Latin character [a-z].
        //Password must contain at least one uppercase Latin character [A-Z].
        //Password must contain at least one special character like ! @ # & ( ).
        //Password must contain a length of at least 8 characters and a maximum of 20 characters.
        else if(!newPw.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}$")){
            binding.tilNewPw.setErrorEnabled(true);
            binding.tilNewPw.setError("This password doesn't reach the required format");
            binding.tilNewPw.requestFocus();
        }

        if(TextUtils.isEmpty(confirmPw)){
            binding.tilConfirmPw.setErrorEnabled(true);
            binding.tilConfirmPw.setError(getString(R.string.error_confirm_pw_empty));
            binding.tilConfirmPw.requestFocus();
        }
        else if(!confirmPw.equals(newPw)){
            binding.tilConfirmPw.setErrorEnabled(true);
            binding.tilConfirmPw.setError("The confirm password does not match with the new password");
            binding.tilConfirmPw.requestFocus();
        }

        if (TextUtils.isEmpty(currentPw) && TextUtils.isEmpty(newPw) && TextUtils.isEmpty(confirmPw)){
            binding.tilCurrentPw.setErrorEnabled(true);
            binding.tilCurrentPw.setError(getString(R.string.error_current_pw_empty));
            binding.tilNewPw.setErrorEnabled(true);
            binding.tilNewPw.setError(getString(R.string.error_new_pw_empty));
            binding.tilConfirmPw.setErrorEnabled(true);
            binding.tilConfirmPw.setError(getString(R.string.error_confirm_pw_empty));

            Toasty.error(this,"Please fill out the required fields!",Toasty.LENGTH_SHORT).show();
        }
        else{
            setPassword(currentPw,newPw);
        }

    }

    //set new password
    private void setPassword(String currentPw, String newPw){
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        if (mUser != null){
            String userEmail = mUser.getEmail();

            if (userEmail != null){
                AuthCredential mCredential = EmailAuthProvider.getCredential(userEmail,currentPw);

                //Prompt the user to re-provide their sign-in credentials
                mUser.reauthenticate(mCredential).addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        // implement update password process
                        mUser.updatePassword(newPw).addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()){
                                Toasty.success(ChangePasswordActivity.this, "The password is updated successfully.",Toasty.LENGTH_SHORT).show();
                                FirebaseAuth.getInstance().signOut();
                                moveToLoginScreen();
                            }
                            else {
                                Toasty.error(ChangePasswordActivity.this, "Update password failure because of incorrect password. Kindly re-check your current password",Toasty.LENGTH_SHORT).show();
                            }
                        });
                    }
                    else{
                        Toasty.error(ChangePasswordActivity.this,"Authentication failed!!!").show();
                    }
                });
            }
        }
    }

    //textChangeWatcher input
    private void watcherInput(){
        binding.tietCurrentPw.addTextChangedListener(tw_currentPw);
        binding.tietNewPw.addTextChangedListener(tw_newPw);
        binding.tietConfirmPw.addTextChangedListener(tw_confirmPw);
    }

    //after click Change Password Button and change success -> move to Login Screen
    private void moveToLoginScreen(){
        Intent intent = new Intent();
        intent.setClass(this, LoginActivity.class);
        startActivity(intent);
        Animatoo.animateSlideRight(this);
    }

    private final TextWatcher tw_confirmPw = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            int confirmPwLength = editable.length();
            String confirmPw = editable.toString().trim();
            String newPw = binding.tietNewPw.getText().toString().trim();

            if(confirmPwLength!=0){
                binding.tilConfirmPw.setErrorEnabled(false);
            }
            else if(confirmPw.equals(newPw)){
                binding.tilConfirmPw.setErrorEnabled(false);
            }
        }
    };

    private final TextWatcher tw_newPw = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            int newPwLength = editable.length();
            String newPw = editable.toString().trim();
            if(newPwLength != 0){
                binding.tilNewPw.setErrorEnabled(false);
            }
            if(newPw.matches(PASSWORD_REGEX)){
                binding.tilNewPw.setErrorEnabled(false);
            }
        }
    };

    private final TextWatcher tw_currentPw = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            int currentPw = editable.length();
            if(currentPw != 0){
                binding.tilCurrentPw.setErrorEnabled(false);
            }

        }
    };

    // Back button on tool bar
    private void handleBackButton(){
        binding.toolBar.toolBarBack.setOnClickListener(view -> {
            onBackPressed();
            Animatoo.animateSlideRight(this);
            onSupportNavigateUp();
        });
        binding.toolBar.toolbarTitle.setText(getString(R.string.txt_change_pass));
        binding.toolBar.toolbarTitle.setVisibility(View.VISIBLE);
    }



}