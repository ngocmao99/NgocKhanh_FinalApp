package com.example.finalproject.view.activity.menu;

import static com.example.finalproject.utils.Constants.DEBUG_LOG;
import static com.example.finalproject.utils.Constants.PATH_USER;
import static com.example.finalproject.utils.Constants.USER_ID;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.bumptech.glide.Glide;
import com.example.finalproject.models.User;
import com.example.finalproject.view.activity.EditProfileActivity;
import com.example.finalproject.base.BaseFragment;
import com.example.finalproject.databinding.ProfilefragmentBinding;
import com.example.finalproject.view.activity.LoginActivity;
import com.example.finalproject.view.adapter.TabAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Locale;

public class Fragment_Profile extends BaseFragment {
    private ProfilefragmentBinding binding;
    private FirebaseAuth mAuth;
    private DatabaseReference mRef;
    private FirebaseUser mUser;
    private FirebaseStorage storage;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = ProfilefragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //init variables
        mRef = FirebaseDatabase.getInstance().getReference();
        //init firebase storage
        storage = FirebaseStorage.getInstance();
        //handle TabLayout and ViewPager
        handleTabLayout();

        //handle edit profile buttons
        binding.editProfileBtn.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), EditProfileActivity.class);
            startActivity(intent);
            Animatoo.animateSlideLeft(getActivity());
        });

        //display user info consists user name and avatar
        showUserInfo();

    }

    private void showUserInfo() {
        if(getCurrentUser()){
            String currentId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            mRef.child(PATH_USER).orderByChild(USER_ID).equalTo(currentId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        for (DataSnapshot ds : snapshot.getChildren()){
                            User currentUser = ds.getValue(User.class);
                            binding.userName.setText(currentUser.getFullName());
                            String imageId = currentUser.getUserImgId().trim();
                            //declare reference storage
                            StorageReference storageRef = storage.getReference();
                            //
                            storageRef.child("Avatars/"+imageId).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    //get url and use trim() to clear the blank.
                                    String imageUrl = uri.toString().trim();
                                    Glide.with(getActivity()).load(imageUrl).centerCrop().into(binding.userImg);
                                }
                            });
                        }
                    }
                    else{
                        Log.e(DEBUG_LOG,"No data found");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }else{
            startActivity(new Intent(getActivity(), LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP));
            Animatoo.animateSlideRight(getActivity());
        }
    }


    private boolean getCurrentUser() {
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        return mUser != null;
    }


    private void handleTabLayout() {
        binding.viewPager.beginFakeDrag();

        binding.viewPager.setAdapter(new TabAdapter(getChildFragmentManager(), binding.tabLayoutProfile.getTabCount()));

        binding.tabLayoutProfile.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                binding.viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }


}
