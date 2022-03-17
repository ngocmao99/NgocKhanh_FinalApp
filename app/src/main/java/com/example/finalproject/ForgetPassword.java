package com.example.finalproject;

import static com.example.finalproject.utils.Constants.EMAIL_REGEX;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.finalproject.databinding.ActivityForgetPasswordBinding;
import com.example.finalproject.view.activity.LoginActivity;
import com.example.finalproject.view.dialog.AlertDialog;
import com.example.finalproject.view.dialog.LoadingDialogCustom;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class ForgetPassword extends AppCompatActivity {
    private ActivityForgetPasswordBinding binding;

    private FirebaseAuth mAuth;

    private AlertDialog dialog;

    private LoadingDialogCustom loadDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Tunr off system bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);

        //Using viewBinding instead of findViewById
        binding = ActivityForgetPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        textWatcher();

        mAuth = FirebaseAuth.getInstance();

        //Declare alert dialog constructor to define the activity
        dialog = new AlertDialog(ForgetPassword.this);

        //Declare loadng dialog constructor to define the activity
        loadDialog = new LoadingDialogCustom(ForgetPassword.this);

        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ForgetPassword.this, LoginActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });

        binding.btnSendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validationEmail();
            }
        });


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


    private void validationEmail() {
        String email = Objects.requireNonNull(binding.tietresetEmaill.getText().toString().trim());

        if (TextUtils.isEmpty(email)){
            binding.tilresetEmail.setError(getText(R.string.error_input_email_empty));
            binding.tilresetEmail.setErrorEnabled(true);
            binding.tilresetEmail.requestFocus();
        }
        else{
            sendEmail(email);


        }
    }

    private void sendEmail(String email) {
        loadDialog.showLoadingDialog();

        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    loadDialog.hideLoadingDialog();
                    dialog.showLoadingDialog(getDrawable(R.drawable.ic_check),
                            getString(R.string.txt_successful_sending_email),
                            getString(R.string.txt_sub_title_sending_email),getResources().getColor(R.color.green));
                    startActivity(new Intent(ForgetPassword.this,LoginActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP));
                }
                else{
                    loadDialog.hideLoadingDialog();
                    dialog.showLoadingDialog(getDrawable(R.drawable.ic_error),
                            getString(R.string.txt_failed_sending_email),
                            task.getException().getMessage(), getResources().getColor(R.color.red));

                }

            }
        });

    }
}