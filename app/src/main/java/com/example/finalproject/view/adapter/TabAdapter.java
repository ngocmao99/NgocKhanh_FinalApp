package com.example.finalproject.view.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.finalproject.view.activity.InfoFragment;
import com.example.finalproject.view.activity.PasswordChangeFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class TabAdapter extends FragmentStatePagerAdapter {

    private final int numOfItem;
    public TabAdapter(@NonNull FragmentManager fm, int numOfItem) {
        super(fm);
        this.numOfItem = numOfItem;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new InfoFragment();

            case 1:
                return new PasswordChangeFragment();

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numOfItem;
    }
}
