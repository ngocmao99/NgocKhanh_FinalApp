package com.example.finalproject.view.activity.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import com.example.finalproject.R;

public class LoadingDialogCustom {
    private final Context context;
    Dialog dialog;

    public LoadingDialogCustom(Context context) {
        this.context = context;
    }

    public void showLoadingDialog(){
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.create();
        dialog.show();
    }

    public void hideLoadingDialog(){
        dialog.dismiss();
    }
}
