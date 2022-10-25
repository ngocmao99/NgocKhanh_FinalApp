package com.example.finalproject.view.activity;

import static com.example.finalproject.utils.Constants.USER_ID;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.finalproject.base.BaseFragment;
import com.example.finalproject.databinding.FragmentInfoBinding;
import com.example.finalproject.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import es.dmoral.toasty.Toasty;

public class InfoFragment extends BaseFragment {
    private FragmentInfoBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentInfoBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Validate the current user signed in
        getCurrentUser();
        if (getCurrentUser()){
            showUserInfo();
        }
        else {
            startActivity(new Intent(getActivity(), LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP));
            Animatoo.animateSlideRight(getActivity());
        }
    }

    private void showUserInfo() {
        //get current userID
        String currentId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        //declare the location of user data on firebase realtime
        DatabaseReference mReference = FirebaseDatabase.getInstance().getReference("USER_PATH");

        mReference.orderByChild("userId").equalTo(currentId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()){
                    Log.d("firebase","Not found data");
                }
                else{
                    Log.d("firebase","Data existed");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("Warning","oncancelled");
            }
        });



    }

    private boolean getCurrentUser() {
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        return mUser != null;
    }
}