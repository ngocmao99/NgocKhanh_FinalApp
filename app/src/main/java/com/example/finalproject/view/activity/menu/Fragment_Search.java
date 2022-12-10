package com.example.finalproject.view.activity.menu;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
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
import com.example.finalproject.view.adapter.ItemEditAdapter;
import com.example.finalproject.view.adapter.PropertyAdapter;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Fragment_Search extends BaseFragment implements ItemEditAdapter.OnItemClickListener {
    private SearchFragmentBinding binding;
    private List<Item> itemList;
    private ItemEditAdapter itemAdapter;
    private RecyclerView recyclerView;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;
    private FirebaseStorage mStorage;
    SearchView searchView;

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
        binding.search.clearFocus();
        binding.rcvSearch.setHasFixedSize(true);
        binding.rcvSearch.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        binding.rcvSearch.setAdapter(itemAdapter);

        itemList = new ArrayList<Item>();
        itemAdapter = new ItemEditAdapter(getContext(), itemList,  this);


        mRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Item item = dataSnapshot.getValue(Item.class);
                    itemList.add(item);
                }
                itemAdapter.notifyDataSetChanged();
                binding.rcvSearch.setAdapter(itemAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        binding.searchFilter.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), SearchActivity.class);
            startActivity(intent);
        });
        binding.search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchList(newText);
                return true;
            }
        });
    }
    public void searchList(String text){
        ArrayList<Item> searchList= new ArrayList<>();
        for (Item item:itemList){
            if(item.getItemName().toLowerCase().contains(text.toLowerCase())){
                searchList.add(item);
            }
        }
        itemAdapter.searchDataList(searchList);
    }








    private void onClickGoToDetail(Item item) {
        Intent intent = new Intent(getActivity(), DetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("item property", item);
        intent.putExtras(bundle);
        startActivity(intent);
    }


    @Override
    public void onItemEditClicked(Item item) {

    }

    @Override
    public void onItemDeleteClicked(Item item) {

    }
}

