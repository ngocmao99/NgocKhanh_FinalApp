package com.example.finalproject.view.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.finalproject.R;
import com.example.finalproject.base.BaseFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class PropertyFragment extends BaseFragment {

    View mView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_property, container, false);
        FloatingActionButton fab = mView.findViewById(R.id.add_property);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CreateFragment.class);
                getActivity().startActivity(intent);
            }
        });


        return mView;
    }
}