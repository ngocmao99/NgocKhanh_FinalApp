package com.example.finalproject.view.activity.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.LayoutInflater;

import com.example.finalproject.databinding.AlertDialogBinding;

public class DialogCustom {
    private final Context context;
    private Dialog dialog;

    public DialogCustom(Context context) {
        this.context = context;
    }

    public void showLoadingDialog(Drawable image, String title, String subTitle, int color) {
        dialog = new Dialog(context);
        com.example.finalproject.databinding.AlertDialogBinding binding = AlertDialogBinding.inflate(LayoutInflater.from(dialog.getContext()));
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

    public void hideLoadingDialogTime(int time){
        Handler handler = new Handler();
        handler.postDelayed(() -> dialog.dismiss(),time);
    }
}
