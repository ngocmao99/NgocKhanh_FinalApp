package com.example.finalproject.view.activity.menu;

import static com.example.finalproject.utils.Constants.FULLNAME;
import static com.example.finalproject.utils.Constants.PATH_USER;
import static com.example.finalproject.utils.Constants.USER_ID;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.finalproject.view.activity.EditProfileActivity;
import com.example.finalproject.base.BaseFragment;
import com.example.finalproject.databinding.ProfilefragmentBinding;
import com.example.finalproject.view.adapter.TabAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Fragment_Profile extends BaseFragment {
    private ProfilefragmentBinding binding;
    private FirebaseAuth mAuth;
    private DatabaseReference mRef;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = ProfilefragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //handle TabLayout and ViewPager
        handleTabLayout();

        //handle edit profile buttons
        binding.editProfileBtn.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), EditProfileActivity.class);
            startActivity(intent);
            Animatoo.animateSlideLeft(getActivity());
        });



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
