package com.example.finalproject.view.activity.menu;

import static com.example.finalproject.utils.Constants.DETAIL_KEY;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.base.BaseFragment;
import com.example.finalproject.databinding.HomefragmentBinding;
import com.example.finalproject.models.Property;
import com.example.finalproject.view.activity.PropertyActivity;
import com.example.finalproject.view.activity.PropertyDetailActivity;
import com.example.finalproject.view.adapter.PropertyAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import java.util.List;


public class Fragment_Home extends BaseFragment implements PropertyAdapter.OnItemClickListener {
    //using view binding in fragment
    private HomefragmentBinding binding;
    private List<Property> itemList;
    private PropertyAdapter itemAdapter;
    private RecyclerView recyclerView;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;
    private FirebaseStorage mStorage;
    private Button add;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = HomefragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //khai bao thu muc luu tru tren firestore
        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference().child("Item");
        mStorage = FirebaseStorage.getInstance();

//        binding.rcvListItem.setHasFixedSize(true);
//        binding.rcvListItem.setLayoutManager(new GridLayoutManager(getActivity(), 1));
//        binding.rcvListItem.setAdapter(itemAdapter);
//
//        itemList = new ArrayList<Property>();
//        itemAdapter = new PropertyAdapter(getContext(), itemList,this);


//        mRef.addValueEventListener(new ValueEventListener() {
//            @SuppressLint("NotifyDataSetChanged")
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                    Property item = dataSnapshot.getValue(Property.class);
//                    itemList.add(item);
//                }
//                itemAdapter.notifyDataSetChanged();
//                binding.rcvListItem.setAdapter(itemAdapter);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//            }
//        });
        //button
        binding.btnAddHome.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), PropertyActivity.class);
            startActivity(intent);
        });
    }


    @Override
    public void onClickGoToDetailProperty(Property property) {
        Intent intent = new Intent(getActivity(), PropertyDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(DETAIL_KEY, property);
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
