package com.example.finalproject.view.activity.onboardingfragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.finalproject.R;
import com.example.finalproject.view.activity.LoginActivity;


public class OnboardingFragment3 extends Fragment {

    private Button btnGetStart;
    private View mView;

    public OnboardingFragment3() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       mView =  inflater.inflate(R.layout.fragment_onboarding3, container, false);

       btnGetStart = mView.findViewById(R.id.btn_get_start);
       btnGetStart.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intent = new Intent(getActivity(), LoginActivity.class);
               getActivity().startActivity(intent);
           }
       });
        return mView;
    }
}