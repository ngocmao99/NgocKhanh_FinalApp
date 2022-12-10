package com.example.finalproject.view.activity.menu;

import static com.example.finalproject.utils.Constants.DETAIL_KEY;
import static com.example.finalproject.utils.Constants.PATH_PROPERTIES;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.base.BaseFragment;
import com.example.finalproject.databinding.SearchFragmentBinding;
import com.example.finalproject.models.Property;
import com.example.finalproject.view.activity.PropertyDetailActivity;
import com.example.finalproject.view.activity.SearchActivity;
import com.example.finalproject.view.adapter.PropertyAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

public class Fragment_Search extends BaseFragment implements PropertyAdapter.OnItemClickListener {
    private SearchFragmentBinding binding;
    private List<Property> itemList;
    private PropertyAdapter itemAdapter;
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
        mRef = mDatabase.getReference().child(PATH_PROPERTIES);
        mStorage = FirebaseStorage.getInstance();
        binding.search.clearFocus();
        binding.rcvSearch.setHasFixedSize(true);
        binding.rcvSearch.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        binding.rcvSearch.setAdapter(itemAdapter);

        itemList = new ArrayList<Property>();
        itemAdapter = new PropertyAdapter(getContext(), itemList,  this);


        mRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Property item = dataSnapshot.getValue(Property.class);
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
        ArrayList<Property> searchList= new ArrayList<>();
        for (Property item:itemList){
            if(item.getPropertyName().toLowerCase().contains(text.toLowerCase())){
                searchList.add(item);
            }
        }
        itemAdapter.searchDataList(searchList);
    }


    @Override
    public void onClickGoToDetailProperty(Property property) {
        Intent intent = new Intent(getActivity(), PropertyDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(DETAIL_KEY, property);
        intent.putExtras(bundle);
        startActivity(intent);

    }

    @Override
    public void onClickEditProperty(Property property) {

    }

    @Override
    public void onClickRemoveProperty(Property property) {

    }
}

