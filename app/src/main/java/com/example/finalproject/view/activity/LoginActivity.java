package com.example.finalproject.view.activity;

import static com.example.finalproject.utils.Constants.EMAIL_REGEX;
import static com.example.finalproject.utils.Constants.RC_SING_IN;
import static com.example.finalproject.utils.Constants.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.finalproject.R;
import com.example.finalproject.databinding.ActivityLoginBinding;
import com.example.finalproject.view.activity.dialog.DialogCustom;
import com.example.finalproject.view.activity.dialog.LoadingDialogCustom;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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

        //init firebase auth
        mAuth = FirebaseAuth.getInstance();

        //init DatabaseReference
        mRef = FirebaseDatabase.getInstance().getReference();

        textWatcher();

        //Declare loading dialog custom
        loadDialog = new LoadingDialogCustom(LoginActivity.this);

        //Declare  dialog custom
        dialogCustom = new DialogCustom(LoginActivity.this);

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

        // Configure sign-in to request the user's ID, email address, and basic profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))// Don't mention this error, when running app itself solve.
                .requestEmail().build();

        //Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(LoginActivity.this,gso);

        signInGoogle();




    }

    private void signInGoogle() {
        binding.googleSignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //begin Google Sign In
                Log.d(TAG,"onClick: begin Google Sign In");
                Intent intent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(intent,RC_SING_IN);
            }
        });
    }

    //Handle result of intent at line 112


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Result returned from launching the intent at line 112- from GoogleSignInApi.getSignInIntent(...);
        if(requestCode == RC_SING_IN){
            Log.d(TAG,"onActivityResult: Google SignIn intent result");
            Task<GoogleSignInAccount> accountTask = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                //Google SignIn success, auth with Firebase
                GoogleSignInAccount account = accountTask.getResult(ApiException.class);
                firebaseAuthWithGoogleAccount(account);
            }catch (Exception e){
                //failed Google SignIn
                Log.d(TAG,"onActivityResult: "+e.getMessage());
            }
        }
    }

    private void moveToHome(){
        startActivity(new Intent(LoginActivity.this, HomeActivity.class).addFlags(
                Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
        Animatoo.animateSlideLeft(LoginActivity.this);
        finish();
    }

    private void firebaseAuthWithGoogleAccount(GoogleSignInAccount account) {
        Log.d(TAG,"firebaseAuthWithGoogleAccount: begin firebase auth with google account");
        GoogleSignInAccount googleSignInAccount;
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                loadDialog.showLoadingDialog();
                //login successful
                Log.d(TAG,"onSuccess: Logged In");

                //get logged in user
                FirebaseUser mUser = mAuth.getCurrentUser();

                //get user Information
                String uId = Objects.requireNonNull(mUser.getUid());
                String email = Objects.requireNonNull(mUser.getEmail());
                String fullName = Objects.requireNonNull(mUser.getDisplayName());

                Log.d(TAG,"onSuccess: EMAIL: "+email);
                Log.d(TAG,"onSuccess: UID: "+uId);

                //check if user is new or existing

                if (authResult.getAdditionalUserInfo().isNewUser()){
                    //user is new -- Account Created
                    Log.d(TAG,"onSuccess: Account Created...\n"+email);
                    Toast.makeText(LoginActivity.this, "Account Created..."+email, Toast.LENGTH_SHORT).show();

                    HashMap<String, Object> userInfo = new HashMap<>();
                    userInfo.put("fullName",fullName);
                    userInfo.put("email",email);
                    userInfo.put("userImgId","");
                    userInfo.put("dob","");
                    userInfo.put("phoneNumber","");
                    userInfo.put("gender","");

                    userInfo.put("userId",uId);

                    mRef.child("Users").child(mAuth.getCurrentUser().getUid()).setValue(userInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        loadDialog.hideLoadingDialog();
                                        dialogCustom.showLoadingDialog(getDrawable(R.drawable.ic_check),getString(R.string.txt_title_congratulation),
                                                getString(R.string.txt_sub_title_sign_in_google),getResources().getColor(R.color.green));
                                    }
                                });
                            }




                        }
                    });
                }

                else {
                    //existing user -- Logged In
                    Log.d(TAG,"onSuccess: Existing User...\n"+email);
                }
                //Start move to Home Activity
                //TODO: turn off note mode for startActivity() move to Home Activity.
//                moveToHome();


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //login failed
                Log.d(TAG,"onFailed: Logged Failed "+e.getMessage());
                loadDialog.hideLoadingDialog();
                dialogCustom.showLoadingDialog(getDrawable(R.drawable.ic_error),getString(R.string.txt_title_logged_failed),
                        getString(R.string.sub_title_logged_failed),getResources().getColor(R.color.red));
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
//                        moveToHome();

                    }

                }else{
                    loadDialog.hideLoadingDialog();
                    Toast.makeText(LoginActivity.this,task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}