package com.example.finalproject.base;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.finalproject.view.activity.dialog.DialogCustom;
import com.example.finalproject.view.activity.dialog.LoadingDialogCustom;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class BaseActivity extends AppCompatActivity {
    private LoadingDialogCustom loadingDialog;
    private DialogCustom dialogCustom;

    protected void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    protected void showToast(int message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private final FirebaseAuth.AuthStateListener mHandlerAuthStateListener =
            new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    if (user != null){
                        //Since the user object is non-null, the current user is now signed in.
                        BaseActivity.this.handleUserSignIn(user);
                    }else{
                        //Since the user object is null, the current user is now signed out.
                        BaseActivity.this.handleUserSignOut(user);
                    }
                }
            };

    @Override
    protected void onStart() {
        super.onStart();

        //Start listening for Firebase Auth state changes
        FirebaseAuth.getInstance().addAuthStateListener(mHandlerAuthStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();

        //Stop listening for Firebase Auth state changes
        FirebaseAuth.getInstance().removeAuthStateListener(mHandlerAuthStateListener);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Turn off header bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        //initial loading dialog
        loadingDialog = new LoadingDialogCustom(this);

        //initial dialog custom
        dialogCustom = new DialogCustom(this);
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
        loadingDialog.showLoadingDialog();
    }

    protected void hideLoading() {
        if (loadingDialog != null){
            loadingDialog.hideLoadingDialog();
        }
    }

    protected void showDialog(Drawable image, String title, String subTitle, int color){
        dialogCustom.showLoadingDialog(image,title,subTitle,color);
    }

    protected void hideDialog(){
        dialogCustom.hideLoadingDialog();
    }

    /** Determines and the returns whether or not the current user signed in
     *
     * @return true if the current user is signed into this app false otherwise.
     */
    protected boolean currentUserIsSignedIn(){
        return FirebaseAuth.getInstance().getCurrentUser() != null;
    }

    /**
     * Handle when the user sign in
     *
     * Override in subclasses to respond to this authentication state change
     *
     * @param mUser The user who is signed in now
     */
    protected void handleUserSignIn(FirebaseUser mUser){

    }

    /**
     * Handle when the user signs out
     *
     * Override in subclasses to respond to this authentication state change
     *
     */
    protected void handleUserSignOut(FirebaseUser mUser){
    }

}
