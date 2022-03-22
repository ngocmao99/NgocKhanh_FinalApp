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
import com.example.finalproject.models.Category2;
import com.example.finalproject.view.activity.HomeActivity;
import com.example.finalproject.view.adapter.Category2Adapter;
import com.example.finalproject.view.adapter.CategoryAdapter;


import java.util.ArrayList;
import java.util.List;

public class Fragment_Home extends Fragment {
    // Add RecyclerView member
    private RecyclerView rcvCategory;
    private RecyclerView rcvCategory2;
    private CategoryAdapter categoryAdapter;
    private Category2Adapter category2Adapter;
    private HomeActivity homeActivity;
    private View mView;
    SearchView searchView;
    


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.homefragment, container, false);

        // Add the following lines to create RecyclerView

       rcvCategory = mView.findViewById(R.id.rcv_category1);
        rcvCategory2 = mView.findViewById(R.id.rcv_category2);
        //category1
        categoryAdapter = new CategoryAdapter();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(homeActivity,3);
        rcvCategory.setLayoutManager(gridLayoutManager);
        rcvCategory.setFocusable(false);
        rcvCategory.setNestedScrollingEnabled(false);
        categoryAdapter.setData(getListCategory());
        rcvCategory.setAdapter(categoryAdapter);
        //category2
        category2Adapter = new Category2Adapter();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(homeActivity);
        rcvCategory2.setLayoutManager(linearLayoutManager);
        rcvCategory2.setFocusable(false);
        rcvCategory2.setNestedScrollingEnabled(false);
        category2Adapter.setData(getListCategory2());
        rcvCategory2.setAdapter(category2Adapter);
        return mView;

    }
    private List<Category> getListCategory(){
    List<Category> list= new ArrayList<>();
        list.add(new Category(R.drawable.ic_motel,"Motel "));
        list.add(new Category(R.drawable.ic_motel2,"hotel "));
        list.add(new Category(R.drawable.ic_motel3,"TownHouse "));

        return list;
    }
    private List<Category2> getListCategory2(){
        List<Category2> list = new ArrayList<>();
        list.add(new Category2(R.drawable.house_1,"2Category 1"));
        list.add(new Category2(R.drawable.house_2,"2Category 2"));
        list.add(new Category2(R.drawable.house_3,"2Category 3"));
        list.add(new Category2(R.drawable.house_4,"2Category 4"));
        list.add(new Category2(R.drawable.house_1,"2Category 1"));
        list.add(new Category2(R.drawable.house_2,"2Category 2"));
        list.add(new Category2(R.drawable.house_3,"2Category 3"));
        list.add(new Category2(R.drawable.house_4,"2Category 4"));
    return list;
    }


}


