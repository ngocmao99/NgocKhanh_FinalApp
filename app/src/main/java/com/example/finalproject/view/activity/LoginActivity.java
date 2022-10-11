package com.example.finalproject.view.activity;

import static com.example.finalproject.utils.Constants.AVATAR;
import static com.example.finalproject.utils.Constants.DEFAULT_RED_CODE;
import static com.example.finalproject.utils.Constants.DEFAULT_VALUE;
import static com.example.finalproject.utils.Constants.DISTRICT;
import static com.example.finalproject.utils.Constants.DOB;
import static com.example.finalproject.utils.Constants.EMAIL_REGEX;
import static com.example.finalproject.utils.Constants.FB_TAG;
import static com.example.finalproject.utils.Constants.FULLNAME;
import static com.example.finalproject.utils.Constants.GENDER;
import static com.example.finalproject.utils.Constants.HOUSE_NUMBER;
import static com.example.finalproject.utils.Constants.LATITUDE;
import static com.example.finalproject.utils.Constants.LONGITUDE;
import static com.example.finalproject.utils.Constants.OTHER;
import static com.example.finalproject.utils.Constants.PASSWORD_REGEX;
import static com.example.finalproject.utils.Constants.PHONE_NUMBER;
import static com.example.finalproject.utils.Constants.POSTAL_CODE;
import static com.example.finalproject.utils.Constants.PROVINCE;
import static com.example.finalproject.utils.Constants.RC_SING_IN;
import static com.example.finalproject.utils.Constants.TAG;
import static com.example.finalproject.utils.Constants.USER_EMAIL;
import static com.example.finalproject.utils.Constants.WARD;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.finalproject.R;
import com.example.finalproject.databinding.ActivityLoginBinding;
import com.example.finalproject.view.activity.dialog.DialogCustom;
import com.example.finalproject.view.activity.dialog.LoadingDialogCustom;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

import es.dmoral.toasty.Toasty;

public class LoginActivity extends AppCompatActivity {
    //Declare viewBinding alternative for findViewById
    private ActivityLoginBinding binding;

    private FirebaseAuth mAuth;

    private LoadingDialogCustom loadDialog;

    //Declare GoogleSignInClient variable.
    private GoogleSignInClient mGoogleSignInClient;

    //Declare DatabaseReference
    private DatabaseReference mRef;

    private DialogCustom dialogCustom;

    private CallbackManager mCallbackManager;

    private FirebaseUser mUser;

    private FirebaseAuth.AuthStateListener authStateListener;

    private AccessTokenTracker accessTokenTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Enable softInputMode PAN
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE|WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        //Turn off header bar of android
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        //apply view binding
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        //init firebase auth
        mAuth = FirebaseAuth.getInstance();

        //init DatabaseReference
        mRef = FirebaseDatabase.getInstance().getReference();

        textWatcher();

        //Declare loading dialog custom
        loadDialog = new LoadingDialogCustom(LoginActivity.this);

        //Declare  dialog custom
        dialogCustom = new DialogCustom(LoginActivity.this);

        binding.btnLogin.setOnClickListener(view1 -> validateInput());

        binding.signup.setOnClickListener(view12 -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
            Animatoo.animateSlideLeft(LoginActivity.this);
        });

        binding.txtForgetPassword.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, ForgetPassword.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |
                    Intent.FLAG_ACTIVITY_CLEAR_TOP));
            Animatoo.animateSlideRight(LoginActivity.this);
        });

    }


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            updateUI(currentUser);
        }

    }

    private void updateUI(FirebaseUser currentUser) {
        if (mUser != null) {
            Log.d(FB_TAG, "onSuccess: FULLNAME:  " + mUser.getDisplayName());
            Log.d(FB_TAG, "onSuccess: EMAIL: " + mUser.getEmail());
            if (mUser.getPhotoUrl() != null) {
                String photoUrl = mUser.getPhotoUrl().toString();
                //Set up for using Picasso library to set the user photo url
                //photoUrl = photoUrl+"?type=large";
                //Picasso.get().load(photoUrl).into(...);
                Log.d(FB_TAG, "onSuccess: PHOTO URL" + photoUrl);
            } else {
                Log.d(FB_TAG, "onSuccess: PHOTO URL: NONE");
            }
        }
    }

    private void moveToHome() {
        startActivity(new Intent(LoginActivity.this, HomeActivity.class).addFlags(
                Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
        Animatoo.animateSlideLeft(LoginActivity.this);
        finish();
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
            String password = editable.toString().trim();
            if (editable.length() != 0) {
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

            if (s.length() != 0) {
                binding.tilEmailInput.setErrorEnabled(false);
            }
            if (email.matches(EMAIL_REGEX)) {
                binding.tilEmailInput.setErrorEnabled(false);

            }
        }
    };

    // verification the credential input
    private void validateInput() {
        String email = Objects.requireNonNull(Objects.requireNonNull(binding.tietEmailInput.getText()).toString());
        String password = Objects.requireNonNull(Objects.requireNonNull(binding.tietPasswordInput.getText()).toString());

        if (TextUtils.isEmpty(email)) {
            binding.tilEmailInput.setErrorEnabled(true);
            binding.tilEmailInput.setError(getString(R.string.error_input_email_empty));
            binding.tilEmailInput.requestFocus();
        }
        else if(!email.matches(EMAIL_REGEX)){
            binding.tilEmailInput.setErrorEnabled(true);
            binding.tilEmailInput.setError(getString(R.string.error_invalid_email));
            binding.tilEmailInput.requestFocus();
        }
        if (TextUtils.isEmpty(password)) {
            binding.tilPasswordInput.setErrorEnabled(true);
            binding.tilPasswordInput.setError(getString(R.string.error_input_password_signup));
            binding.tilPasswordInput.requestFocus();
        }
        if (TextUtils.isEmpty(email) && TextUtils.isEmpty(password)) {
            binding.tilEmailInput.setErrorEnabled(true);
            binding.tilEmailInput.setError(getString(R.string.error_input_email_empty));
            binding.tilPasswordInput.setErrorEnabled(true);
            binding.tilPasswordInput.setError(getString(R.string.error_input_password_signup));
            Toasty.error(LoginActivity.this,getString(R.string.error_empty_credential),Toasty.LENGTH_SHORT).show();

        } else {
            loginWithCheck(email, password);
        }
    }

    private void loginWithCheck(String email, String password) {
        loadDialog.showLoadingDialog();

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {

                if (!Objects.requireNonNull(mAuth.getCurrentUser()).isEmailVerified()) {
                    loadDialog.hideLoadingDialog();
                    Toast.makeText(LoginActivity.this, getText(R.string.error_not_verified_email), Toast.LENGTH_SHORT).show();
                } else {
                    loadDialog.hideLoadingDialog();
                    moveToHome();

                }

            } else {
                loadDialog.hideLoadingDialog();
                Toast.makeText(LoginActivity.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}