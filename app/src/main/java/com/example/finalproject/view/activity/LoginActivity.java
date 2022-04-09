package com.example.finalproject.view.activity;

import static com.example.finalproject.utils.Constants.EMAIL_REGEX;
import static com.example.finalproject.utils.Constants.FB_TAG;
import static com.example.finalproject.utils.Constants.OTHER;
import static com.example.finalproject.utils.Constants.RC_SING_IN;
import static com.example.finalproject.utils.Constants.TAG;

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
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
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
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

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

        //init Facebook SDK
        FacebookSdk.sdkInitialize(LoginActivity.this);

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

        // Configure sign-in to request the user's ID, email address, and basic profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))// Don't mention this error, when running app itself solve.
                .requestEmail().build();

        //Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(LoginActivity.this, gso);

        signInGoogle();

        signInFacebook();


    }

    private void signInFacebook() {
        binding.facebookSignInBtn.setOnClickListener(v -> {
            //Init Facebook login button
            mCallbackManager = CallbackManager.Factory.create();
            LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this,
                    Arrays.asList("email", "public_profile"));
            LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    Log.d(FB_TAG, "onSuccess: " + loginResult);
                    handleFacebookAccessToken(loginResult.getAccessToken());
                }

                @Override
                public void onCancel() {
                    Log.d(FB_TAG, "onCancel");

                }

                @Override
                public void onError(@NonNull FacebookException e) {
                    Log.d(FB_TAG, "onError: " + e.getMessage());

                }
            });
        });
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void handleFacebookAccessToken(AccessToken accessToken) {
        Log.d(FB_TAG, "handleFacebookToken: " + accessToken);

        AuthCredential authCredential = FacebookAuthProvider.getCredential(accessToken.getToken());
        mAuth.signInWithCredential(authCredential).addOnSuccessListener(authResult -> {
            loadDialog.showLoadingDialog();
            mUser = FirebaseAuth.getInstance().getCurrentUser();

            //get user information through FirebaseUser
            String userID = Objects.requireNonNull(mUser.getUid());
            String email = Objects.requireNonNull(mUser.getEmail());
            String fullName = Objects.requireNonNull(mUser.getDisplayName());

            //check if user is new or existing
            if (authResult.getAdditionalUserInfo().isNewUser()) {
                //user is new -- Account Created

                HashMap<String, Object> userProfile = new HashMap<>();

                userProfile.put("fullName", fullName);
                userProfile.put("email", email);
                userProfile.put("userImgId", "");
                userProfile.put("dob", "");
                userProfile.put("phoneNumber", "");
                userProfile.put("gender",OTHER);

                userProfile.put("userId", userID);

                mRef.child("Users").child(userID).setValue(userProfile)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                mAuth.getCurrentUser().sendEmailVerification()
                                        .addOnCompleteListener(task1 -> {
                                            loadDialog.hideLoadingDialog();
                                            dialogCustom.showLoadingDialog(getDrawable(R.drawable.ic_check),
                                                    getString(R.string.txt_title_congratulation),
                                                    getString(R.string.txt_sub_title_sign_in_google),
                                                    getResources().getColor(R.color.green));
                                        });
                            }

                        });

            } else {
                loadDialog.hideLoadingDialog();
                Toast.makeText(LoginActivity.this, "Existing User with email: " + email, Toast.LENGTH_SHORT)
                        .show();
            }
            moveToHome();
        }).addOnFailureListener(e -> {
            loadDialog.hideLoadingDialog();
            dialogCustom.showLoadingDialog(getDrawable(R.drawable.ic_error),
                    getString(R.string.txt_title_logged_failed),
                    e.getMessage(), getResources().getColor(R.color.red));
        });
    }

    @SuppressWarnings("deprecation")
    private void signInGoogle() {
        binding.googleSignInBtn.setOnClickListener(v -> {
            //begin Google Sign In
            Log.d(TAG, "onClick: begin Google Sign In");
            Intent intent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(intent, RC_SING_IN);
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

    //Handle result of intent at line 112
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        //Result returned from launching the intent at line 112- from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SING_IN) {
            Log.d(TAG, "onActivityResult: Google SignIn intent result");
            Task<GoogleSignInAccount> accountTask = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                //Google SignIn success, auth with Firebase
                GoogleSignInAccount account = accountTask.getResult(ApiException.class);
                firebaseAuthWithGoogleAccount(account);
            } catch (Exception e) {
                //failed Google SignIn
                Log.d(TAG, "onActivityResult: " + e.getMessage());
            }
        } else {
            mCallbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void moveToHome() {
        startActivity(new Intent(LoginActivity.this, HomeActivity.class).addFlags(
                Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
        Animatoo.animateSlideLeft(LoginActivity.this);
        finish();
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void firebaseAuthWithGoogleAccount(GoogleSignInAccount account) {
        Log.d(TAG, "firebaseAuthWithGoogleAccount: begin firebase auth with google account");
        GoogleSignInAccount googleSignInAccount;
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential).addOnSuccessListener(authResult -> {
            loadDialog.showLoadingDialog();
            //login successful
            Log.d(TAG, "onSuccess: Logged In");

            //get logged in user
            FirebaseUser mUser = mAuth.getCurrentUser();

            //get user Information
            String uId = Objects.requireNonNull(mUser).getUid();
            String email = Objects.requireNonNull(mUser.getEmail());
            String fullName = Objects.requireNonNull(mUser.getDisplayName());

            Log.d(TAG, "onSuccess: EMAIL: " + email);
            Log.d(TAG, "onSuccess: UID: " + uId);

            //check if user is new or existing

            if (Objects.requireNonNull(authResult.getAdditionalUserInfo()).isNewUser()) {
                //user is new -- Account Created
                Log.d(TAG, "onSuccess: Account Created...\n" + email);
                Toast.makeText(LoginActivity.this, "Account Created..." + email, Toast.LENGTH_SHORT).show();

                HashMap<String, Object> userInfo = new HashMap<>();
                userInfo.put("fullName", fullName);
                userInfo.put("email", email);
                userInfo.put("userImgId", "");
                userInfo.put("dob", "");
                userInfo.put("phoneNumber", "");
                userInfo.put("gender", "");

                userInfo.put("userId", uId);

                mRef.child("Users").child(mAuth.getCurrentUser().getUid()).setValue(userInfo).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(task1 -> {
                            loadDialog.hideLoadingDialog();

                            //Show dialog
                            dialogCustom.showLoadingDialog(getDrawable(R.drawable.ic_check), getString(R.string.txt_title_congratulation),
                                    getString(R.string.txt_sub_title_sign_in_google), getResources().getColor(R.color.green));
                            dialogCustom.hideLoadingDialogTime(3000);
                            moveToHome();

                        });
                    }

                });
            } else {
                //existing user -- Logged In
                Log.d(TAG, "onSuccess: Existing User...\n" + email);
                loadDialog.hideLoadingDialog();
                moveToHome();
            }
        }).addOnFailureListener(e -> {
            //login failed
            Log.d(TAG, "onFailed: Logged Failed " + e.getMessage());
            loadDialog.hideLoadingDialog();
            dialogCustom.showLoadingDialog(getDrawable(R.drawable.ic_error), getString(R.string.txt_title_logged_failed),
                    getString(R.string.sub_title_logged_failed), getResources().getColor(R.color.red));
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

    private void validateInput() {
        String email = Objects.requireNonNull(Objects.requireNonNull(binding.tietEmailInput.getText()).toString());
        String password = Objects.requireNonNull(Objects.requireNonNull(binding.tietPasswordInput.getText()).toString());

        if (TextUtils.isEmpty(email)) {
            binding.tilEmailInput.setErrorEnabled(true);
            binding.tilEmailInput.setError(getString(R.string.error_input_email_empty));
            binding.tilEmailInput.requestFocus();
        }
        if (TextUtils.isEmpty(password)) {
            binding.tilPasswordInput.setErrorEnabled(true);
            binding.tilPasswordInput.setError(getString(R.string.error_input_password_signup));
            binding.tilPasswordInput.requestFocus();
        }
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(LoginActivity.this, getText(R.string.error_empty_credential), Toast.LENGTH_SHORT).show();
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