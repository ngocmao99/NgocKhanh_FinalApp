package com.example.finalproject.view.activity;

import static com.example.finalproject.utils.Constants.DETAIL_KEY;
import static com.example.finalproject.utils.Constants.PATH_USER;
import static com.example.finalproject.utils.Constants.USER_ID;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.bumptech.glide.Glide;
import com.example.finalproject.R;
import com.example.finalproject.base.BaseActivity;
import com.example.finalproject.databinding.ActivityPropertyDetailBinding;
import com.example.finalproject.models.Property;
import com.example.finalproject.models.User;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class PropertyDetailActivity extends BaseActivity implements OnMapReadyCallback {
    private ActivityPropertyDetailBinding binding;
    private String propertyTitle;
    private String userId;
    private DatabaseReference mRef;
    private StorageReference mStorageRef;
    private GoogleMap map;
    private double lat;
    private double lng;
    private String title;
    private String facilities;
    private String phoneNumber;
    private String email;
    private String ownerId;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPropertyDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // Init database reference mRef
        mRef = FirebaseDatabase.getInstance().getReference();

        // Init database reference mRef
        mStorageRef = FirebaseStorage.getInstance().getReference();

        //get bundle
        showLoading();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            Property property = (Property) bundle.getParcelable(DETAIL_KEY);
            propertyTitle = property.getPropertyName();
            Picasso.get().load(property.getPropertyImage()).into(binding.propertyImage);
            binding.price.setText("$ " + property.getPrice());
            binding.title.setText(property.getPropertyName());
            title = property.getPropertyName();
            binding.location.setText(property.getHouseNumber() + ", " + property.getWard() + ", " +
                    property.getDistrict() + ", " + property.getProvince());
            binding.bedroom.setText(property.getBedroom() + " " + getString(R.string.txt_suffix_bedroom));
            binding.bathroom.setText(property.getBathroom() + " " + getString(R.string.txt_suffix_bathroom));
            binding.area.setText(property.getArea() + getString(R.string.txt_area_unit));
            binding.description.setText(property.getPropertyDescription());
            binding.type.setText(property.getPropertyType());
            binding.floor.setText(property.getPropertyFloor());
            //get coordinate of property location
            lat = property.getLat();
            lng = property.getLng();
            //get owner -> userID
            userId = property.getUserId();
            getOwnerInfo(userId);
            //get facilities
            facilities = property.getPropertyFacilities();
            hideLoading();
        } else {
            return;
        }

        handleFacility(facilities);
        //handle back button and toolbar
        handleToolbar();

        //handle map
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        
        //ge phone number and email address
        getPhoneAndMail(ownerId);
        
        //call button
        callToOwner(phoneNumber);
        
        //send email 
        sendMailToOwner(email);
    }

    private void sendMailToOwner(String email) {
    }

    private void callToOwner(String phoneNumber) {
        binding.btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (phoneNumber.isEmpty()){
                    Toasty.error(PropertyDetailActivity.this,"Cannot call to owner. Kindly send mail for him/her",Toasty.LENGTH_SHORT).show();
                }else {
                    startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel",phoneNumber,null)));
                    Animatoo.animateSlideLeft(PropertyDetailActivity.this);
                }

            }
        });
    }

    private void getPhoneAndMail(String userId){
        DatabaseReference userRef = mRef.child(PATH_USER);
        userRef.orderByChild(USER_ID).equalTo(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot ds : snapshot.getChildren()){
                        User owner = ds.getValue(User.class);
                        phoneNumber = owner.getPhoneNumber();
                        email = owner.getEmail();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void handleFacility(String facilities) {
        List<customCheckbox> checkboxes = new ArrayList<>();
        checkboxes.add(binding.cbWifi);
        checkboxes.add(binding.cbGrocery);
        checkboxes.add(binding.cbSpa);
        checkboxes.add(binding.cbAirConditioner);
        checkboxes.add(binding.cbHospital);
        checkboxes.add(binding.cbHotWater);
        checkboxes.add(binding.cbMarket);
        checkboxes.add(binding.cbPool);
        checkboxes.add(binding.cbEducation);
        checkboxes.add(binding.cbGim);
        for (int i = 0; i < checkboxes.size(); i++) {
            String facility = checkboxes.get(i).getText().toString().trim();
            if (facilities.contains(facility)) {
                checkboxes.get(i).setChecked(true);
            }
        }


    }
    

    private void handleToolbar() {
        binding.toolBar.toolBarBack.setOnClickListener(view -> {
            onBackPressed();
            Animatoo.animateSlideRight(PropertyDetailActivity.this);
            onSupportNavigateUp();
        });
        binding.toolBar.toolbarTitle.setText(propertyTitle);
        binding.toolBar.toolbarTitle.setVisibility(View.VISIBLE);
    }

    private void getOwnerInfo(String userId) {
        mRef.child(PATH_USER).orderByChild(USER_ID).equalTo(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        User owner = ds.getValue(User.class);

                        binding.userName.setText(owner.getFullName());
                        //handle user image
                        String userImageId = owner.getUserImgId().trim();
                        if (!userImageId.isEmpty()) {
                            mStorageRef.child("Avatars/" + userImageId).getDownloadUrl().addOnSuccessListener(uri -> {
                                String imageUrl = uri.toString();
                                Glide.with(PropertyDetailActivity.this).load(imageUrl).centerCrop().into(binding.userImg);
                            }).addOnFailureListener(e -> showToast("Failure"));
                        } else {
                            Glide.with(PropertyDetailActivity.this)
                                    .load(R.drawable.ic_baseline_photo_camera_24)
                                    .centerInside()
                                    .into(binding.userImg);
                        }
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;
        Log.d("COORDINATOR", "getCoordinator: " + lat + ", " + lng);
        LatLng propertyLocation = new LatLng(lat, lng);
        map.addMarker(new MarkerOptions().position(propertyLocation).title(title));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(propertyLocation, 20f));
    }
}