package com.example.finalproject.view.activity.menu;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.R;
import com.example.finalproject.base.BaseFragment;
import com.example.finalproject.databinding.FragmentPropertyBinding;
import com.example.finalproject.databinding.SearchFragmentBinding;
import com.example.finalproject.models.Item;
import com.example.finalproject.view.activity.DetailActivity;
import com.example.finalproject.view.activity.PropertyActivity;
import com.example.finalproject.view.activity.SearchActivity;
import com.example.finalproject.view.adapter.ItemAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

public class Fragment_Search extends BaseFragment {
    private SearchFragmentBinding binding;
    private List<Item> itemList;
    private ItemAdapter itemAdapter;
    private RecyclerView recyclerView;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;
    private FirebaseStorage mStorage;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = SearchFragmentBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference().child("Item");
        mStorage = FirebaseStorage.getInstance();

        binding.rcvfindmain.setHasFixedSize(true);
        binding.rcvfindmain.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        binding.rcvfindmain.setAdapter(itemAdapter);

        itemList = new ArrayList<Item>();
        itemAdapter = new ItemAdapter(getContext(), itemList);


        mRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Item item = dataSnapshot.getValue(Item.class);
                    itemList.add(item);
                }
                itemAdapter.notifyDataSetChanged();
                binding.rcvfindmain.setAdapter(itemAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        binding.searchFilter.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), SearchActivity.class);
            startActivity(intent);
        });
    }
    private void onClickGoToDetail(Item item) {
        Intent intent = new Intent(getActivity(), DetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("item property", item);
        intent.putExtras(bundle);
        startActivity(intent);
    }}
