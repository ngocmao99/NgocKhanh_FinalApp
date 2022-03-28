package com.example.finalproject.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.example.finalproject.databinding.DialogLoadingBinding;


public class LoadingDialog extends Dialog {
    private DialogLoadingBinding dialogLoadingBinding;

    public LoadingDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dialogLoadingBinding = DialogLoadingBinding.inflate(getLayoutInflater());
        setContentView(dialogLoadingBinding.getRoot());
        setCancelable(false);
    }
}
