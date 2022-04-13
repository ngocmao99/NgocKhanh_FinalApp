package com.example.finalproject.view.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.finalproject.view.activity.InfoFragment;
import com.example.finalproject.view.activity.PasswordChangeFragment;
import com.example.finalproject.view.activity.createpropertyfragment.CreateProperty1;
import com.example.finalproject.view.activity.createpropertyfragment.CreateProperty2;
import com.example.finalproject.view.activity.createpropertyfragment.CreateProperty3;
import com.example.finalproject.view.activity.createpropertyfragment.CreateProperty4;
import com.example.finalproject.view.activity.createpropertyfragment.CreateProperty5;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class CreateAdapter extends FragmentStatePagerAdapter {

    private final int numOfItem;
    public CreateAdapter(@NonNull FragmentManager fm, int numOfItem) {
        super(fm);
        this.numOfItem = numOfItem;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new CreateProperty1();

            case 1:
                return new CreateProperty2();
            case 2:
                return new CreateProperty3();
            case 3:
                return new CreateProperty4();
            case 4:
                return new CreateProperty5();

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numOfItem;
    }
}