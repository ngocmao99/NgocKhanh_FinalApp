package com.example.finalproject.view.activity.menu;

import static com.example.finalproject.utils.Constants.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.finalproject.R;
import com.example.finalproject.base.BaseFragment;
import com.example.finalproject.databinding.HomefragmentBinding;

import com.example.finalproject.models.Item;
import com.example.finalproject.my_interface.IClickItemListener;
import com.example.finalproject.view.activity.AddActivity;
import com.example.finalproject.view.activity.DetailActivity;
import com.example.finalproject.view.activity.HomeActivity;
import com.example.finalproject.view.activity.PropertyActivity;
import com.example.finalproject.view.adapter.ItemAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Fragment_Home extends BaseFragment {
    //using view binding in fragment
    private HomefragmentBinding binding;
    List<Item> itemList;
    ItemAdapter itemAdapter;
    RecyclerView recyclerView;
    FirebaseDatabase mDatabase;
    DatabaseReference mRef;
    FirebaseStorage mStorage;
    Button add;



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
        //khai bao thu muc luu tru tren firestore
        mDatabase=FirebaseDatabase.getInstance();
        mRef=mDatabase.getReference().child("Item");
        mStorage=FirebaseStorage.getInstance();

      binding.rcvListItem.setHasFixedSize(true);
      binding.rcvListItem.setLayoutManager(new GridLayoutManager(getActivity(),1));
       binding.rcvListItem.setAdapter(itemAdapter);

        itemList= new ArrayList<Item>();
        itemAdapter = new ItemAdapter(getContext(),itemList);


        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                        Item item = dataSnapshot.getValue(Item.class);
                        itemList.add(item);
                    }


                    itemAdapter.notifyDataSetChanged();
                    binding.rcvListItem.setAdapter(itemAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }



        });
        //button
        binding.btnAddHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PropertyActivity.class);
                startActivity(intent);


    }});





}
    private void onClickGoToDetail(Item item) {
        Intent intent = new Intent(getActivity(), DetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("item property",item);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
