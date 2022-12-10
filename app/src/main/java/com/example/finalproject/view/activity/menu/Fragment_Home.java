package com.example.finalproject.view.activity.menu;

import static com.example.finalproject.utils.Constants.DETAIL_KEY;
import static com.example.finalproject.utils.Constants.PATH_PROPERTIES;
import static com.example.finalproject.utils.Constants.REQUEST_LOCATION_CODE;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.finalproject.R;
import com.example.finalproject.base.BaseFragment;
import com.example.finalproject.databinding.HomefragmentBinding;
import com.example.finalproject.models.Property;
import com.example.finalproject.view.activity.PropertyActivity;
import com.example.finalproject.view.activity.PropertyDetailActivity;
import com.example.finalproject.view.adapter.ItemAdapter;
import com.example.finalproject.view.adapter.MPropertyAdapter;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;


public class Fragment_Home extends BaseFragment implements MPropertyAdapter.OnItemClickListener{
    //using view binding in fragment
    private HomefragmentBinding binding;
    private List<Property> itemList;
    private MPropertyAdapter itemAdapter;
    private DatabaseReference mRef;
    private FirebaseStorage mStorage;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private double latitude, longitude;
    private List<Property> properties;

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
        mRef = FirebaseDatabase.getInstance().getReference();
        mStorage = FirebaseStorage.getInstance();

        //handle add button
        handleAddbutton();

        getCurrentLocation();
        showRecomendedList();
        showAllItem();
    }

    private void showAllItem() {
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
        binding.rcvProperties.setLayoutManager(layoutManager);
        binding.rcvProperties.setAdapter(itemAdapter);

        properties = new ArrayList<Property>();

        itemAdapter = new MPropertyAdapter(getContext(),itemList,this);

        mRef.child(PATH_PROPERTIES).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    for (DataSnapshot ds: snapshot.getChildren()){
                        Property property = ds.getValue(Property.class);
                        properties.add(property);
                    }
                    binding.rcvProperties.setAdapter(itemAdapter);
                    itemAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void getCurrentLocation() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            //if permission granted
                @SuppressLint("MissingPermission")
                Task<Location> task = fusedLocationProviderClient.getLastLocation();
                task.addOnSuccessListener(location -> {
                    if (location != null){
                        //get geocode of location
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                        Log.d("LOCATION","getLocation: Lat: "+latitude +"/nLng: "+longitude);

                    }
                    else {
                        showToast(getString(R.string.txt_location_not_found));
                    }
                });

        }else {
            //when permission denied
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_LOCATION_CODE);
        }

    }
    private void showRecomendedList() {
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        binding.rcvListItem.setLayoutManager(layoutManager);
        binding.rcvListItem.setAdapter(itemAdapter);

        itemList = new ArrayList<Property>();

        itemAdapter = new MPropertyAdapter(getContext(),itemList,this);

        //current location
        LatLng currentLatLng = new LatLng(latitude,longitude);

        //get data
        mRef.child(PATH_PROPERTIES).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot ds :snapshot.getChildren()){
                        Property reProperty = ds.getValue(Property.class);
                        LatLng propertyCoordinator = new LatLng(reProperty.getLat(),reProperty.getLng());
                        Log.d("LOCATION: ","LatLng: "+propertyCoordinator);
                        Log.d("DISTANCE","getDistance: "+getDistanceBetweenTwoPoints(latitude, longitude,reProperty.getLat(),reProperty.getLng()));
                        if (getDistanceBetweenTwoPoints(latitude, longitude,reProperty.getLat(),reProperty.getLng()) <= 5.0f){
                            itemList.add(reProperty);
                        }
                    }

                    binding.rcvListItem.setAdapter(itemAdapter);
                    itemAdapter.notifyDataSetChanged();
                }
                else{
                    Toasty.info(getContext(),"No data found",Toasty.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private float getDistanceBetweenTwoPoints(double lat1,double lon1,double lat2,double lon2) {

        float[] distance = new float[2];

        Location.distanceBetween( lat1, lon1,
                lat2, lon2, distance);

        return distance[0];
    }

    private void handleAddbutton() {
        binding.toolBar.toolbarTitle.setText("Explore");
        binding.toolBar.toolBarBack.setVisibility(View.INVISIBLE);
        binding.toolBar.tookBarAdd.setVisibility(View.VISIBLE);
        binding.toolBar.tookBarAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), PropertyActivity.class));
                Animatoo.animateSlideLeft(getActivity());
            }
        });

    }

    @Override
    public void onClickGoToDetailVerticalProperty(Property property) {
        Intent intent = new Intent(getActivity(), PropertyDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(DETAIL_KEY,property);
        intent.putExtras(bundle);
        startActivity(intent);
        Animatoo.animateSlideLeft(getContext());

    }
}
