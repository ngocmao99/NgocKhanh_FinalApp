package com.example.finalproject.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.viewbinding.ViewBinding;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public abstract class BaseFragment extends Fragment {
    /**
     * Show a toast
     * @param message String
     */
    protected void showToast(String message){
        Toast.makeText(getContext(),message,Toast.LENGTH_SHORT).show();
    }

    private final FirebaseAuth.AuthStateListener mHandlerAuthStateListener =
            new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    if (user != null){
                        //Since the user object is non-null, the current user is now signed in.
                        BaseFragment.this.handleUserSignIn(user);
                    }else{
                        //Since the user object is null, the current user is now signed out.
                        BaseFragment.this.handleUserSignOut(user);
                    }
                }
            };

    @Override
    public void onStart() {
        super.onStart();

        //Start listening for Firebase Auth state changes
        FirebaseAuth.getInstance().addAuthStateListener(mHandlerAuthStateListener);
    }

    @Override
    public void onStop() {
        super.onStop();

        //Stop listening for Firebase Auth state changes
        FirebaseAuth.getInstance().removeAuthStateListener(mHandlerAuthStateListener);
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
