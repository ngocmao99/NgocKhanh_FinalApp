package com.example.finalproject.view.activity.menu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.R;
import com.example.finalproject.models.Category;
import com.example.finalproject.models.House;
import com.example.finalproject.models.Motel;
import com.example.finalproject.view.activity.HomeActivity;
import com.example.finalproject.view.adapter.CategoryAdapter;


import java.util.ArrayList;
import java.util.List;

public class Fragment_Home extends Fragment {
    // Add RecyclerView member
    private RecyclerView rcvCategory;
    private CategoryAdapter categoryAdapter;
    private HomeActivity homeActivity;
    private View mView;
    SearchView searchView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.homefragment, container, false);

        // Add the following lines to create RecyclerView
        rcvCategory = mView.findViewById(R.id.rcv_category);
        categoryAdapter = new CategoryAdapter(Fragment_Home.this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(homeActivity, RecyclerView.VERTICAL, false);
        rcvCategory.setLayoutManager(linearLayoutManager);
        categoryAdapter.setData(getListCategory());
        rcvCategory.setAdapter(categoryAdapter);
        return mView;
    }

    private List<Category> getListCategory() {
        List<Category> listCategory = new ArrayList<>();
        List<House> listHouse = new ArrayList<>();
        List<Motel> listMotel = new ArrayList<>();
        listHouse.add(new House(R.drawable.house_1,"House 1"));
        listHouse.add(new House(R.drawable.house_2,"House 2"));
        listHouse.add(new House(R.drawable.house_3,"House 3"));
        listHouse.add(new House(R.drawable.house_4,"House 4"));
        listHouse.add(new House(R.drawable.house_1,"House 1"));
        listHouse.add(new House(R.drawable.house_2,"House 2"));
        listHouse.add(new House(R.drawable.house_3,"House 3"));
        listHouse.add(new House(R.drawable.house_4,"House 4"));
        listMotel.add(new Motel(R.drawable.ic_motel,"Motel"));
        listMotel.add(new Motel(R.drawable.ic_motel2,"Townhouse"));
        listMotel.add(new Motel(R.drawable.ic_motel3,"Apartment"));
//        listCategory.add(new Category("For you",listMotel));
//        listCategory.add(new Category("Categories",listHouse));

        return listCategory;
    }
}


