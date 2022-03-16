package com.example.finalproject.view.activity.onboardingfragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.example.finalproject.R;


public class OnboardingFragment2 extends Fragment {


    public OnboardingFragment2() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       return   inflater.inflate(R.layout.fragment_onboarding2, container, false);


    }
}