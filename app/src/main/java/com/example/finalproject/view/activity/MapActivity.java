package com.example.finalproject.view.activity;

import static com.example.finalproject.utils.Constants.AUTOCOMPLETE_REQUEST_CODE;
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
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.example.finalproject.R;
import com.example.finalproject.base.BaseActivity;
import com.example.finalproject.databinding.ActivityMapBinding;
import com.example.finalproject.models.User;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
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

import java.util.Arrays;
import java.util.List;

public class MapActivity extends BaseActivity implements OnMapReadyCallback {
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

        getUserLocationCode();

        auditPermission();

        configPositionCurrentButton();

        //Initialize the SDK
        Places.initialize(getApplicationContext(),getString(R.string.my_map_api));

        //Create a new PlacesClient instance
        PlacesClient placesClient = Places.createClient(MapActivity.this);

        if (isPermissionGrant = true) {
            if (checkGooglePlayService()) {
                showToast("Google Play Service available");

                binding.mapView.getMapAsync(this);

                binding.mapView.onCreate(savedInstanceState);
            } else {
                showToast("Google Play is unavailable");
            }
        }

//        handlePlaceAutocomplete();


    }

    private void handlePlaceAutocomplete() {
        binding.inputLocation.setOnClickListener(v -> {
            // Set the fields to specify which types of places data to return after the user has made a selection
            List<Place.Field> fields = Arrays.asList(Place.Field.ADDRESS,Place.Field.LAT_LNG, Place.Field.NAME);

            //Start the autocomplete intent using Autocomplete.IntentBuilder
            Intent i = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN,fields).build(MapActivity.this);
            startActivityForResult(i, AUTOCOMPLETE_REQUEST_CODE);
        });

    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == 100 && resultCode == RESULT_OK){
//            //When success, innit place
//            Place place = Autocomplete.getPlaceFromIntent(data);
//
//            //Show location information
//            showToast("Location information "+"\nAddress: "+ place.getAddress()
//            +"Lat log: "+place.getLatLng() +"/n Address name: "+place.getName());
//        }
//        else if (resultCode == AutocompleteActivity.RESULT_ERROR){
//            //When error
//            //Init status
//            Status status = Autocomplete.getStatusFromIntent(data);
//
//            //Display toast
//            showToast(status.getStatusMessage());
//        }
//    }

    private void configPositionCurrentButton(){
        View locationButton = ((View) binding.mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
        RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
        // position on right bottom
        rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
        rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
        rlp.setMargins(0, 180, 180, 0);
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
            Dialog dialog = googleApiAvailability.getErrorDialog(MapActivity.this, result, 201, dialog1 -> showToast("User cancelled dialog"));
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
        map = googleMap;

        showToast("Latitude: " + lat + "\nLongitude: " + lng);

        LatLng userPosition = new LatLng(lat, lng);

        //init Marker options
        MarkerOptions markerOptions = new MarkerOptions();

        map.addMarker(markerOptions.position(userPosition)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                .title("User Location"));

        //Animation
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(userPosition, 15);
        map.animateCamera(cameraUpdate);

        //Zoom Controller
        map.getUiSettings().setZoomControlsEnabled(true);

        //Compass
        map.getUiSettings().setCompassEnabled(true);

        //Zoom Gesture
        map.getUiSettings().setZoomGesturesEnabled(true);

        //Scroll Gesture
        map.getUiSettings().setScrollGesturesEnabled(true);

        //Rotate Gesture
        map.getUiSettings().setRotateGesturesEnabled(true);

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
        map.setMyLocationEnabled(true);


    }
}