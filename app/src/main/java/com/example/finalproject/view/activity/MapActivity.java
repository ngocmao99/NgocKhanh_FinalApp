package com.example.finalproject.view.activity;

import static com.example.finalproject.utils.Constants.PATH_USER;

import android.os.Bundle;

import com.example.finalproject.base.BaseActivity;
import com.example.finalproject.databinding.ActivityMapBinding;
import com.google.android.gms.maps.GoogleMap;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import es.dmoral.toasty.Toasty;

public class MapActivity extends BaseActivity {
    private ActivityMapBinding binding;

    private GoogleMap map;

    private boolean isPermissionGrant;

    private FirebaseUser mUser;

    private DatabaseReference mRef;

    //Declare red code variable of the user
    private double lat;

    private double lng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMapBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //init firebase user
        mUser = FirebaseAuth.getInstance().getCurrentUser();

        //init Database Reference
        mRef = FirebaseDatabase.getInstance().getReference(PATH_USER);

        Toasty.info(MapActivity.this,"Map Screen");
    }
}
