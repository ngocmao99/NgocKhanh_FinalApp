package com.example.finalproject.view.activity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.example.finalproject.R;
import com.example.finalproject.base.BaseFragment;
import com.example.finalproject.databinding.FragmentCreateBinding;
import com.example.finalproject.view.adapter.CreateAdapter;
import com.google.android.material.tabs.TabLayout;


public class CreateFragment extends BaseFragment {
    private FragmentCreateBinding binding;
    private HomeActivity homeActivity;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = FragmentCreateBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //handle TabLayout and ViewPager
        handleTabLayout();





    }


    private void handleTabLayout() {
        binding.viewPagerCreate.beginFakeDrag();

        binding.viewPagerCreate.setAdapter(new CreateAdapter(getChildFragmentManager(), binding.tabLayoutCreate.getTabCount()));

        binding.tabLayoutCreate.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                binding.viewPagerCreate.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }}