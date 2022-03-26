package com.example.finalproject.base;

import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.finalproject.dialog.LoadingDialog;


public class BaseActivity extends AppCompatActivity {
    private LoadingDialog loadingDialog;

    protected void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    protected void showToast(int message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadingDialog = new LoadingDialog(this);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        View v = getCurrentFocus();
        if (v instanceof EditText) {
            int[] scoops = new int[2];
            v.getLocationOnScreen(scoops);
            float x = event.getRawX() + v.getLeft() - scoops[0];
            float y = event.getRawY() + v.getTop() - scoops[1];

            if (event.getAction() == MotionEvent.ACTION_UP
                    && (x < v.getLeft() || x >= v.getRight() || y < v.getTop() || y > v.getBottom())
            ) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        }
        return super.dispatchTouchEvent(event);
    }

    protected void showLoading() {
        loadingDialog.show();
    }

    protected void hideLoading() {
        loadingDialog.dismiss();
    }
}
