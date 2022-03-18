package com.example.finalproject.view.activity.dialog;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.finalproject.R;
import com.example.finalproject.databinding.ButtonDialogBinding;
import com.example.finalproject.view.activity.LoginActivity;

public class ButtonDialog{

    private final Context context;
    private AlertDialog dialog;
    ButtonDialogBinding binding;


    public ButtonDialog(Context context) {
        this.context = context;
    }

    public void showLoadingDialog(Drawable image, String title, String subTitle,String btnNegative,
                                  int negativeColor, String btnPositive,int positiveColor, int color) {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(context).inflate(R.layout.button_dialog, null, false);

        binding = ButtonDialogBinding.bind(view);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setView(binding.getRoot());
        builder.create();

        binding.imgViewDialog.setImageDrawable(image);
        binding.titleDialog.setText(title);
        binding.titleDialog.setTextColor(color);
        binding.subTitle.setText(subTitle);
        binding.btnNegative.setText(btnNegative);
        binding.btnPositive.setText(btnPositive);
        binding.btnNegative.setTextColor(negativeColor);
        binding.btnPositive.setBackgroundColor(positiveColor);

        builder.setCancelable(true);
        //Set dialog by builder.show() to display the button dialog after click Send Email Button.
        dialog = builder.show();

        //Set the background color of the dialog by transparent
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        binding.btnNegative.setOnClickListener(v -> {
            Intent i = new Intent(context, LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(i);
            Animatoo.animateSlideLeft(context);



        });

        binding.btnPositive.setOnClickListener(v -> {
            //To call dismiss() for dialog, must set variable of AlertDialog (in this case is 'dialog'- at line 50)
            //And must set 'true' for setCancelable for variable of AlertDialog.Builder (in this case is 'builder'- at line 48)
            dialog.dismiss();
        });


    }



}
