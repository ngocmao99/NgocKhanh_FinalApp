package com.example.finalproject.view.activity;

import static com.example.finalproject.utils.Constants.AVATAR;
import static com.example.finalproject.utils.Constants.DEFAULT_RED_CODE;
import static com.example.finalproject.utils.Constants.DEFAULT_VALUE;
import static com.example.finalproject.utils.Constants.DISTRICT;
import static com.example.finalproject.utils.Constants.DOB;
import static com.example.finalproject.utils.Constants.EMAIL_REGEX;
import static com.example.finalproject.utils.Constants.FULLNAME;
import static com.example.finalproject.utils.Constants.GENDER;
import static com.example.finalproject.utils.Constants.HOUSE_NUMBER;
import static com.example.finalproject.utils.Constants.LATITUDE;
import static com.example.finalproject.utils.Constants.LONGITUDE;
import static com.example.finalproject.utils.Constants.NAME_REGEX1;
import static com.example.finalproject.utils.Constants.NAME_REGEX2;
import static com.example.finalproject.utils.Constants.OTHER;
import static com.example.finalproject.utils.Constants.PASSWORD_REGEX;
import static com.example.finalproject.utils.Constants.PATH_USER;
import static com.example.finalproject.utils.Constants.PHONE_NUMBER;
import static com.example.finalproject.utils.Constants.POSTAL_CODE;
import static com.example.finalproject.utils.Constants.PROVINCE;
import static com.example.finalproject.utils.Constants.USER_EMAIL;
import static com.example.finalproject.utils.Constants.USER_ID;
import static com.example.finalproject.utils.Constants.WARD;

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
    //TextWatcher: validation confirmation passwords to ensure it not empty and equal the password field.
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
            if (editable.length() > 0) {
                binding.tilPasswordConfirmInput.setErrorEnabled(false);
            }
            if (confirmPassword.equals(password)) {
                binding.tilPasswordConfirmInput.setErrorEnabled(false);
            }
        }
    };
    //TextWWatcher: validation password has the lenght is bigger than 0 and matches with the format password.
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
            if (editable.length() > 0) {
                binding.tilPasswordRegisterInput.setErrorEnabled(false);
            }
            if (password.matches(PASSWORD_REGEX)) {
                binding.tilPasswordRegisterInput.setErrorEnabled(false);
            }
        }
    };
    //TextWatcher email include email length not empty and matches the correct format for email address (email address contains character @
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
            if (editable.length() >= 0) {
                binding.tilEmailRegisterInput.setErrorEnabled(false);
            }
            if (email.matches(EMAIL_REGEX)) {
                binding.tilEmailRegisterInput.setErrorEnabled(false);
            }
        }
    };
    //TextWatcher: ensure the user's fullname doesn't contains the special characters or number.
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
            if (editable.length() >= 0) {
                binding.tilName.setErrorEnabled(false);
            }
            if (name.matches(NAME_REGEX1) || !name.matches(NAME_REGEX2)) {
                binding.tilName.setErrorEnabled(false);
            }
        }
    };

    private void validateSignup() {
        String name = Objects.requireNonNull(Objects.requireNonNull(binding.tietNameInput.getText()).toString());
        String email = Objects.requireNonNull(Objects.requireNonNull(binding.tietEmailRegisterInput.getText()).toString());
        String password = Objects.requireNonNull(Objects.requireNonNull(binding.tietPasswordRegisterInput.getText()).toString());
        String confirmPass = Objects.requireNonNull(Objects.requireNonNull(binding.tietPasswordConfirm.getText()).toString());
        // prevent user do not fill out name field.
        if (TextUtils.isEmpty(name)) {
            binding.tilName.setErrorEnabled(true);
            binding.tilName.setError("Name cannot be empty");
            binding.tilName.requestFocus();
        }
        //prevent user do not fill out email field.
        if (TextUtils.isEmpty(email)) {
            binding.tilEmailRegisterInput.setErrorEnabled(true);
            binding.tilEmailRegisterInput.setError("Email cannot be empty");
            binding.tilEmailRegisterInput.requestFocus();
        }
        //prevent user do not fill out password field.
        if (TextUtils.isEmpty(password)) {
            binding.tilPasswordRegisterInput.setErrorEnabled(true);
            binding.tilPasswordRegisterInput.setError(getString(R.string.error_input_password_signup));
            binding.tilPasswordRegisterInput.requestFocus();
        }
        //prevent user do not fill out confirm password field.
        if (TextUtils.isEmpty(confirmPass)) {
            binding.tilPasswordConfirmInput.setErrorEnabled(true);
            binding.tilPasswordConfirmInput.setError(getString(R.string.error_input_confirm_password_signup));
            binding.tilPasswordConfirmInput.requestFocus();
        }
        //ensure the confirm password matches with password
        if (!password.equals(confirmPass)) {
            binding.tilPasswordConfirmInput.setErrorEnabled(true);
            binding.tilPasswordConfirmInput.setError(getString(R.string.error_input_password_unmatch_signup));
            binding.tilPasswordConfirmInput.requestFocus();
        }
        //prevent the password don't matches with the password format
        if(!password.matches(PASSWORD_REGEX)){
            binding.tilPasswordRegisterInput.setErrorEnabled(true);
            binding.tilPasswordRegisterInput.setError(getString(R.string.error_password_format));
            binding.tilPasswordRegisterInput.requestFocus();
        }
        //prevent the password don't matches with the email format
        if (!email.matches(EMAIL_REGEX)){
            binding.tilEmailRegisterInput.setErrorEnabled(true);
            binding.tilEmailRegisterInput.setError(getString(R.string.error_invalid_email));
            binding.tilEmailRegisterInput.requestFocus();
        }
        //prevent the user's name consist of the special characters or number
        if(!name.matches(NAME_REGEX1) || name.matches(NAME_REGEX2)){
            binding.tilName.setErrorEnabled(true);
            binding.tilName.setError(getString(R.string.error_name_unique_signup));
            binding.tilName.requestFocus();
        }

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPass) ||
                !password.equals(confirmPass)) {
            Toast.makeText(RegisterActivity.this, getString(R.string.error_empty_form), Toast.LENGTH_SHORT).show();
        } else {
            registerUser(name, email, password);
        }
    }
    // register method
    private void registerUser(final String name, final String email, final String password) {
        dialogLoading.showLoadingDialog();
        //create new user with email and password method
        mAuth.createUserWithEmailAndPassword(email,password).addOnSuccessListener(authResult -> {
            HashMap<String, Object> userProfile = new HashMap<>();
            userProfile.put(FULLNAME, name);
            userProfile.put(USER_EMAIL, email);
            userProfile.put(AVATAR, DEFAULT_VALUE);
            userProfile.put(DOB, DEFAULT_VALUE);
            userProfile.put(PHONE_NUMBER, DEFAULT_VALUE);
            userProfile.put(GENDER,OTHER);
            userProfile.put(LATITUDE,DEFAULT_RED_CODE);
            userProfile.put(LONGITUDE,DEFAULT_RED_CODE);
            userProfile.put(PROVINCE,DEFAULT_VALUE);
            userProfile.put(POSTAL_CODE,DEFAULT_VALUE);
            userProfile.put(DISTRICT,DEFAULT_VALUE);
            userProfile.put(WARD,DEFAULT_VALUE);
            userProfile.put(HOUSE_NUMBER,DEFAULT_VALUE);
            //get the userID via FirebaseAuth
            userProfile.put(USER_ID,Objects.requireNonNull(Objects.requireNonNull(mAuth.getCurrentUser()).getUid()));
            //push user date to firebase real-time method
            mDatabase.child(PATH_USER).child(mAuth.getCurrentUser().getUid()).setValue(userProfile).addOnCompleteListener(task -> {
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