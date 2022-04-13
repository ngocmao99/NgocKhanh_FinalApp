package com.example.finalproject.view.activity;

import static com.example.finalproject.utils.Constants.PATH_USER;
import static com.example.finalproject.utils.Constants.USER_ID;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.example.finalproject.R;
import com.example.finalproject.base.BaseActivity;
import com.example.finalproject.databinding.ActivityMapBinding;
import com.example.finalproject.models.User;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

public class MapActivity extends BaseActivity implements OnMapReadyCallback {
    private ActivityMapBinding binding;

    private GoogleMap googleMap;

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

        getUserLocationCode();

        auditPermission();

        if (isPermissionGrant = true) {
            if (checkGooglePlayService()) {
                showToast("Google Play Service available");

                binding.mapView.getMapAsync(this);

                binding.mapView.onCreate(savedInstanceState);
            } else {
                showToast("Google Play is unavailable");
            }
        }
    }

    private void getUserLocationCode() {
        String uId = mUser.getUid();

        mRef.orderByChild(USER_ID).equalTo(uId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        User user = ds.getValue(User.class);
                        lat = user.getLatitude();
                        lng = user.getLongitude();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    //The method of map view must have
    @Override
    protected void onStart() {
        super.onStart();

        binding.mapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();

        binding.mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();

        binding.mapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();

        binding.mapView.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        binding.mapView.onDestroy();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);

        binding.mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();

        binding.mapView.onLowMemory();
    }

    private boolean checkGooglePlayService() {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int result = googleApiAvailability.isGooglePlayServicesAvailable(MapActivity.this);

        if (result == ConnectionResult.SUCCESS) {
            return true;
        } else if (googleApiAvailability.isUserResolvableError(result)) {
            Dialog dialog = googleApiAvailability.getErrorDialog(MapActivity.this, result, 201, new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    showToast("User cancelled dialog");
                }
            });
            dialog.show();
        }

        return false;
    }

    private void auditPermission() {
        Dexter.withContext(MapActivity.this).withPermission(Manifest.permission.ACCESS_FINE_LOCATION).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                isPermissionGrant = true;
                showToast("Permission Granted...!");

            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), "");
                intent.setData(uri);
                startActivity(intent);

            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();

            }
        }).check();
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        googleMap = googleMap;

        showToast("Latitude: " + lat + "\nLongitude: " + lng);

        LatLng userPostition = new LatLng(lat, lng);

        //inite Marker options
        MarkerOptions markerOptions = new MarkerOptions();

        googleMap.addMarker(markerOptions.position(userPostition)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                .title("User Location"));

        //Animation
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(userPostition, 15);
        googleMap.animateCamera(cameraUpdate);

        //Zoom Controller
        googleMap.getUiSettings().setZoomControlsEnabled(true);

        //Compass
        googleMap.getUiSettings().setCompassEnabled(true);

        //Zoom Gesture
        googleMap.getUiSettings().setZoomGesturesEnabled(true);

        //Scroll Gesture
        googleMap.getUiSettings().setScrollGesturesEnabled(true);

        //Rotate Gesture
        googleMap.getUiSettings().setRotateGesturesEnabled(true);

        //Current Location
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        googleMap.setMyLocationEnabled(true);


    }
}