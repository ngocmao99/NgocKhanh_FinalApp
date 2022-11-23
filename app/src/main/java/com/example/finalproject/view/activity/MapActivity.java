package com.example.finalproject.view.activity;

import static com.example.finalproject.utils.Constants.COARSE_LOCATION;
import static com.example.finalproject.utils.Constants.DEFAULT_ZOOM;
import static com.example.finalproject.utils.Constants.FINE_LOCATION;
import static com.example.finalproject.utils.Constants.LOCATION_PERMISSION_REQUEST_CODE;
import static com.example.finalproject.utils.Constants.MAP_API_KEY;
import static com.example.finalproject.utils.Constants.PATH_USER;
import static com.example.finalproject.utils.Constants.TAG_MAP;
import static com.example.finalproject.utils.Constants.TAG_PLACE;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.finalproject.R;
import com.example.finalproject.base.BaseActivity;
import com.example.finalproject.databinding.ActivityMapBinding;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MapActivity extends BaseActivity{
    private ActivityMapBinding binding;

    private GoogleMap mMap;

    private boolean isPermissionGrant;

    private FirebaseUser mUser;

    private DatabaseReference mRef;

    //Declare red code variable of the user
    private double lat;

    private double lng;

    private boolean mLocationPermissionGranted = false;

    private FusedLocationProviderClient mFusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMapBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //init firebase user
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        //init Database Reference
        mRef = FirebaseDatabase.getInstance().getReference(PATH_USER);
        //request location permission
        getLocationPermission();
    }
    //get the current device's location
    private void getDeviceLocation(){

        Log.d(TAG_MAP,"getDeviceLocation: getting the current device's location");
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        try {
            if (mLocationPermissionGranted){
                @SuppressLint("MissingPermission") Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()){
                            Log.d(TAG_MAP, "onComplete: found location");
                            Location currentLocation = (Location) task.getResult();
                        }else {
                            Log.d(TAG_MAP,"onComplete: current location is null");
                        }
                    }
                });

            }

        }catch (SecurityException e){
            Log.e(TAG_MAP,"getDeviceLocation: SecurityException" + e.getMessage());
        }
    }
    //request to access the location on the user's device
    private void getLocationPermission(){
        Log.d(TAG_MAP,"getLocationPermission: getting location permissions");
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION};
        if(ContextCompat.checkSelfPermission(this.getApplicationContext(), FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(),COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                mLocationPermissionGranted = true;
            }else {
                ActivityCompat.requestPermissions(this,permissions,LOCATION_PERMISSION_REQUEST_CODE);
            }
        }
        else{
            ActivityCompat.requestPermissions(this,permissions,LOCATION_PERMISSION_REQUEST_CODE);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d(TAG_MAP, "onRequestPermissionsResult: called");
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            mLocationPermissionGranted = false;
                            Log.d(TAG_MAP, "onRequestPermissionsResult: permission failed");
                            return;
                        }
                    }
                }
                Log.d(TAG_MAP, "onRequestPermissionsResult: permission granted");
                mLocationPermissionGranted = true;
                //initialize map
                getDeviceLocation();
            }
        }
    }
}

