package com.example.finalproject.view.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.finalproject.R;
import com.example.finalproject.base.BaseFragment;
import com.example.finalproject.databinding.FragmentPropertyBinding;
import com.example.finalproject.databinding.ProfilefragmentBinding;

public class PropertyFragment extends BaseFragment {
    private FragmentPropertyBinding binding;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = FragmentPropertyBinding.inflate(inflater, container, false);
        return binding.getRoot();

}
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.btnCreateItem1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment CreateFrag = new CreateFragment();
                FragmentTransaction fm = getActivity().getSupportFragmentManager().beginTransaction();
                fm.replace(R.id.drawer_layout,CreateFrag).commit();
            }
        });
}
}