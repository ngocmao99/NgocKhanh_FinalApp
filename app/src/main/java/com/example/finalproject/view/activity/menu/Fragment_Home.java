package com.example.finalproject.view.activity.menu;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.finalproject.R;
import com.example.finalproject.databinding.HomefragmentBinding;
import com.example.finalproject.models.Category;
import com.example.finalproject.models.Property;
import com.example.finalproject.view.activity.HomeActivity;
import com.example.finalproject.view.adapter.CategoryAdapter;
import com.example.finalproject.view.adapter.PropertyAdapter;

import java.util.ArrayList;
import java.util.List;

public class Fragment_Home extends Fragment {
    //using view binding in fragment
    private HomefragmentBinding binding;

    private CategoryAdapter categoryAdapter;

    private PropertyAdapter propertyAdapter;

    private HomeActivity homeActivity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        binding = HomefragmentBinding.inflate(inflater,container,false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //category
        categoryAdapter = new CategoryAdapter();

        //Init gridviews
        GridLayoutManager gridLayoutManager = new GridLayoutManager(homeActivity,1);
        binding.rcvCategory1.setLayoutManager(gridLayoutManager);
        binding.rcvCategory1.setFocusable(false);
        binding.rcvCategory1.setNestedScrollingEnabled(true);
        categoryAdapter.setData(getListCategory());
        binding.rcvCategory1.setAdapter(categoryAdapter);

        //property
        propertyAdapter = new PropertyAdapter();
        Context context;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(homeActivity);
        binding.rcvCategory2.setLayoutManager(linearLayoutManager);
        binding.rcvCategory2.setFocusable(false);
        binding.rcvCategory2.setNestedScrollingEnabled(false);
        propertyAdapter.setData(getListProperty());
        binding.rcvCategory2.setAdapter(propertyAdapter);

    }

    private List<Property> getListProperty() {
        List<Property> mList = new ArrayList<>();
        mList.add(new Property("1","Fluffy","Motel","123 NorthWay",R.drawable.firstpicture));
        mList.add(new Property("2","Nancy","Villa","123 NorthWay",R.drawable.firstpicture));
        mList.add(new Property("3","Danny","Studio","123 NorthWay",R.drawable.firstpicture));
        return mList;
    }

    private List<Category> getListCategory() {
        List<Category> mList = new ArrayList<>();
        mList.add(new Category("1",getString(R.string.txt_category_name),"motel",R.drawable.ic_motel));
        mList.add(new Category("2",getString(R.string.txt_category_name),"motel",R.drawable.ic_check));
        mList.add(new Category("3",getString(R.string.txt_category_name),"motel",R.drawable.ic_error));
        return mList;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
