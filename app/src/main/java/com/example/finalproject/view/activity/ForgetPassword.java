package com.example.finalproject.view.activity;

import static com.example.finalproject.utils.Constants.EMAIL_REGEX;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.finalproject.R;
import com.example.finalproject.databinding.ActivityForgetPasswordBinding;
import com.example.finalproject.view.activity.dialog.ButtonDialog;
import com.example.finalproject.view.activity.dialog.DialogCustom;
import com.example.finalproject.view.activity.dialog.LoadingDialogCustom;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class ForgetPassword extends AppCompatActivity{
    private ActivityForgetPasswordBinding binding;

    private FirebaseAuth mAuth;

    private DialogCustom dialog;

    private LoadingDialogCustom loadDialog;

    private ButtonDialog buttonDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Turn off system bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);

        //Using viewBinding instead of findViewById
        binding = ActivityForgetPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        textWatcher();

        mAuth = FirebaseAuth.getInstance();

        //Declare alert dialog constructor to define the activity
        dialog = new DialogCustom(ForgetPassword.this);

        //Declare loading dialog constructor to define the activity
        loadDialog = new LoadingDialogCustom(ForgetPassword.this);

        //Declare button dialog constructor to define the activity
        buttonDialog = new ButtonDialog(ForgetPassword.this);

        binding.btnBack.setOnClickListener(v -> {
            startActivity(new Intent(ForgetPassword.this, LoginActivity.class)
            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP));
            Animatoo.animateSlideRight(ForgetPassword.this);
        });

        binding.btnSendEmail.setOnClickListener(v -> validationEmail());


    }

    private void textWatcher() {
        binding.tietresetEmaill.addTextChangedListener(tw_email);
    }

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

            if(s.length() == 0){
                binding.tilresetEmail.setError(getText(R.string.error_input_email_empty));
                binding.tilresetEmail.setErrorEnabled(true);
                binding.tilresetEmail.requestFocus();
            }

            if(!email.matches(EMAIL_REGEX)){
                binding.tilresetEmail.setError(getText(R.string.error_invalid_email));
                binding.tilresetEmail.setErrorEnabled(true);
                binding.tilresetEmail.requestFocus();
            }

            else{
                binding.tilresetEmail.setErrorEnabled(false);
            }

        }
    };


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Animatoo.animateSlideLeft(ForgetPassword.this); //fire the slide right animation
    }

    public void moveToLogin(int timeDelay){
        Runnable runnable = () -> {
            // if you are redirecting from a fragment then use getActivity() as the context.
            startActivity(new Intent(ForgetPassword.this, LoginActivity.class)
            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP));

            //Set slide animation
            Animatoo.animateSlideLeft(ForgetPassword.this);
        };
        Handler handler = new Handler();
        handler.postDelayed(runnable,timeDelay);
    }


    public void validationEmail() {
        String email = Objects.requireNonNull(Objects.requireNonNull(binding.tietresetEmaill.getText()).toString().trim());

        if (TextUtils.isEmpty(email)){
            binding.tilresetEmail.setError(getText(R.string.error_input_email_empty));
            binding.tilresetEmail.setErrorEnabled(true);
            binding.tilresetEmail.requestFocus();
        }
        else{
            sendEmail(email);


        }
    }



    @SuppressLint("UseCompatLoadingForDrawables")
    public void sendEmail(String email) {
        loadDialog.showLoadingDialog();

        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                loadDialog.hideLoadingDialog();
                dialog.showLoadingDialog(getDrawable(R.drawable.ic_check),
                        getString(R.string.txt_successful_sending_email),
                        getString(R.string.txt_sub_title_sending_email),getResources().getColor(R.color.green));
                dialog.hideLoadingDialogTime(2000);
                moveToLogin(2000);
            }
            else{
                loadDialog.hideLoadingDialog();
                buttonDialog.showLoadingDialog(getDrawable(R.drawable.ic_error),
                        getString(R.string.txt_failed_sending_email),Objects.requireNonNull(task.getException()).getMessage(),
                        getString(R.string.btn_negative),
                        getResources().getColor(R.color.red),getString(R.string.txt_try_again),
                        getResources().getColor(R.color.red),
                         getResources().getColor(R.color.red));


            }

        });

    }
}