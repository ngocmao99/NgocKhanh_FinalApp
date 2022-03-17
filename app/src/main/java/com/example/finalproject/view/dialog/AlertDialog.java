package com.example.finalproject.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;

import androidx.core.content.ContextCompat;

import com.example.finalproject.R;
import com.example.finalproject.databinding.AlertDialogBinding;

public class AlertDialog {
    private AlertDialogBinding binding;
    private final Context context;
    private Dialog dialog;

    public AlertDialog(Context context) {
        this.context = context;
    }

    public void showLoadingDialog(Drawable image, String title, String subTitle, int color) {
        dialog = new Dialog(context);
        binding = AlertDialogBinding.inflate(LayoutInflater.from(dialog.getContext()));
        dialog.setContentView(binding.getRoot());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        binding.imgViewDialog.setImageDrawable(image);
        binding.titleDialog.setText(title);
        binding.titleDialog.setTextColor(color);
        binding.subTitle.setText(subTitle);

        dialog.create();
        dialog.show();
    }

    public void hideLoadingDialog() {
        dialog.dismiss();
    }
}
