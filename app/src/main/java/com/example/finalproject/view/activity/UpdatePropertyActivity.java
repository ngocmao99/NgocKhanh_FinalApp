package com.example.finalproject.view.activity;

import static com.example.finalproject.utils.Constants.AREA;
import static com.example.finalproject.utils.Constants.BATH;
import static com.example.finalproject.utils.Constants.BED;
import static com.example.finalproject.utils.Constants.DETAIL_KEY;
import static com.example.finalproject.utils.Constants.FLOOR;
import static com.example.finalproject.utils.Constants.GALLERY_CODE;
import static com.example.finalproject.utils.Constants.LOCATION;
import static com.example.finalproject.utils.Constants.NUMBER_VALID_FORMAT;
import static com.example.finalproject.utils.Constants.PRICE;
import static com.example.finalproject.utils.Constants.PUSH_DATA;
import static com.example.finalproject.utils.Constants.P_CREATOR;
import static com.example.finalproject.utils.Constants.P_DESCRIPTION;
import static com.example.finalproject.utils.Constants.P_DISTRICT;
import static com.example.finalproject.utils.Constants.P_FACILITIES;
import static com.example.finalproject.utils.Constants.P_HN;
import static com.example.finalproject.utils.Constants.P_ID;
import static com.example.finalproject.utils.Constants.P_IMG;
import static com.example.finalproject.utils.Constants.P_LAT;
import static com.example.finalproject.utils.Constants.P_LNG;
import static com.example.finalproject.utils.Constants.P_POSTALCODE;
import static com.example.finalproject.utils.Constants.P_PROVINCE;
import static com.example.finalproject.utils.Constants.P_TYPE;
import static com.example.finalproject.utils.Constants.P_WARD;
import static com.example.finalproject.utils.Constants.REQUEST_LOCATION_CODE;
import static com.example.finalproject.utils.Constants.RESULT_CODE;
import static com.example.finalproject.utils.Constants.TAG_FACILITY;
import static com.example.finalproject.utils.Constants.TAG_LOCATION;
import static com.example.finalproject.utils.Constants.TAG_TYPE;
import static com.example.finalproject.utils.Constants.TIME;
import static com.example.finalproject.utils.Constants.TITLE;
import static com.example.finalproject.utils.Constants.TYPE_PATH;
import static com.example.finalproject.utils.Constants.ZERO;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.bumptech.glide.Glide;
import com.example.finalproject.R;
import com.example.finalproject.base.BaseActivity;
import com.example.finalproject.databinding.ActivityUpdatePropertyBinding;
import com.example.finalproject.models.Property;
import com.example.finalproject.models.PropertyType;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.saadahmedsoft.popupdialog.PopupDialog;
import com.saadahmedsoft.popupdialog.Styles;
import com.saadahmedsoft.popupdialog.listener.OnDialogButtonClickListener;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import es.dmoral.toasty.Toasty;

public class UpdatePropertyActivity extends BaseActivity {
    private ActivityUpdatePropertyBinding binding;
    private Bundle bundle;
    private Property property;
    private DatabaseReference mRef;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private double lat, lng;
    private String postalCode, province, district, ward, houseNumber;
    private Uri propertyUri;
    private String propertyFacilities;
    private StorageReference mStorageReference;
    private String propertyImage;
    private FirebaseUser mUser;
    private String pId;
    private String currentImg;
    private String currentFac;
    private double currentlat, currentlng;
    private String cPostalcode, cProvince, cDistritc, cWard, cHouseNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUpdatePropertyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mUser = FirebaseAuth.getInstance().getCurrentUser();

        mRef = FirebaseDatabase.getInstance().getReference();
        // handle toolbar
        handleToolbar();
        //get data from Property Activity
       bundle = getIntent().getExtras();
       if (bundle != null){
           property = (Property) bundle.get(DETAIL_KEY);
           pId = property.getPropertyId();
           currentImg = property.getPropertyImage();
           currentFac = property.getPropertyFacilities();
           currentlat = property.getLat();
           currentlng = property.getLng();
           cPostalcode = property.getPostalCode();
           cProvince = property.getProvince();
           cWard = property.getWard();
           cDistritc = property.getDistrict();
           cHouseNumber = property.getHouseNumber();
       }
       //show details information
        showDetails();
        textChangeWatcher();
        handleTypeOption();
        addPropertyImage();
        getPropertyLocation();
        update();
        cancel();


    }

    private void cancel() {
        binding.btnCancel.setOnClickListener(v -> startActivity(new Intent()));
    }

    private void update() {
        binding.btnUpdate.setOnClickListener(view -> {
            String title = Objects.requireNonNull(binding.propertyTitle.getText().toString().trim());
            String address = Objects.requireNonNull(binding.propertyLocation.getText().toString());
            String type = Objects.requireNonNull(binding.propertyType.getText().toString());
            String description = Objects.requireNonNull(binding.propertyDesciprion.getText().toString().trim());
            String bedroom = Objects.requireNonNull(binding.bedRoom.getText().toString());
            String bathroom = binding.bathRoom.getText().toString();
            String area = binding.area.getText().toString();
            String price = binding.price.getText().toString();
            String facilities = handleFacilities();
            //title is empty
            if (TextUtils.isEmpty(title)) {
                binding.tilPropertyTitle.setError(getString(R.string.error_empty_required_fields));
                binding.tilPropertyTitle.setErrorEnabled(true);
                binding.tilPropertyTitle.requestFocus();
            }
            //location is empty
            if (TextUtils.isEmpty(address)) {
                binding.tilPropertyLocation.setError(getString(R.string.error_empty_property_location));
                binding.tilPropertyLocation.setErrorEnabled(true);
                binding.tilPropertyLocation.requestFocus();
            }

            //Property type is not selected any option
            if (TextUtils.isEmpty(type)) {
                binding.tilPropertyType.setError(getString(R.string.error_not_choose_any_option));
                binding.tilPropertyType.setErrorEnabled(true);
                binding.tilPropertyType.requestFocus();
            }
//            Property description
            if (TextUtils.isEmpty(description)) {
                binding.tilPropertyDescription.setError(getString(R.string.error_empty_required_fields));
                binding.tilPropertyDescription.setErrorEnabled(true);
                binding.tilPropertyDescription.requestFocus();
            } else if (description.length() < 30) {
                binding.tilPropertyDescription.setError(getString(R.string.error_description_length_is_smaller_than_ten));
                binding.tilPropertyDescription.setErrorEnabled(true);
                binding.tilPropertyDescription.requestFocus();
            }
            // Bedroom quantity validate - not empty and equals 0
            if (TextUtils.isEmpty(bedroom)) {
                binding.tilBedroom.setErrorEnabled(true);
                binding.tilBedroom.setError(" ");
                binding.tilBedroom.requestFocus();
            } else if (bedroom.equals(ZERO)) {
                binding.tilBedroom.setErrorEnabled(true);
                binding.tilBedroom.setError(" ");
                binding.tilBedroom.requestFocus();
            }

            //Bathroom quantity validate - not empty and equals 0
            if (TextUtils.isEmpty(bathroom)) {
                binding.tilBathroom.setErrorEnabled(true);
                binding.tilBathroom.setError(" ");
                binding.tilBathroom.requestFocus();
            }
            if (bathroom.equals(ZERO)) {
                binding.tilBathroom.setErrorEnabled(true);
                binding.tilBathroom.setError(" ");
                binding.tilBathroom.requestFocus();
            }

            // Area of property validate - not empty and over than 0
            if (TextUtils.isEmpty(area)) {
                binding.tilArea.setErrorEnabled(true);
                binding.tilArea.setError(" ");
                binding.tilArea.requestFocus();
            }

            //Price per month validate
            if (TextUtils.isEmpty(price)) {
                binding.tilPrice.setErrorEnabled(true);
                binding.tilPrice.setError(" ");
                binding.tilPrice.requestFocus();
            } else if (! price.matches(NUMBER_VALID_FORMAT)) {
                binding.tilPrice.setErrorEnabled(true);
                binding.tilPrice.setError(" ");
                binding.tilPrice.requestFocus();
            }

            if (TextUtils.isEmpty(title) || TextUtils.isEmpty(address) || TextUtils.isEmpty(type) || TextUtils.isEmpty(description) ||
                    TextUtils.isEmpty(bedroom) || bedroom.equals(ZERO) || TextUtils.isEmpty(bathroom) || bathroom.equals(ZERO) || TextUtils.isEmpty(area)) {
                Toasty.error(UpdatePropertyActivity.this, getString(R.string.error_empty_required_fields),Toasty.LENGTH_SHORT).show();
            } else {
                PopupDialog.getInstance(UpdatePropertyActivity.this).setStyle(Styles.STANDARD)
                        .setHeading(getString(R.string.txt_title_popup_update))
                        .setHeading(getString(R.string.txt_popup_dialog_subtitle_update))
                        .setPopupDialogIcon(R.drawable.warning_icon)
                        .setCancelable(false)
                        .setPositiveButtonText(getString(R.string.txt_submit))
                        .showDialog(new OnDialogButtonClickListener() {
                            @Override
                            public void onPositiveClicked(Dialog dialog) {
                                super.onPositiveClicked(dialog);
                                showLoading();
                                if (propertyUri == null){
                                    updateNoUri(title, address, currentImg, type, description, facilities, bedroom, bathroom, area, price);
                                }
                                else if(lat == 0 && lng == 0){
                                    lat = currentlat;
                                    lng =currentlng;
                                    updateNoGeo(title, address,lat, lng, currentImg, type, description, facilities, bedroom, bathroom, area, price);
                                }
                                else if(propertyUri != null){

                                    updateUri(title, address, propertyUri, type, description, facilities, bedroom, bathroom, area, price);
                                }
                                else if( currentlat != lat && currentlng != lng && lat != 0 && lng != 0){
                                    updateGeo(title, address, propertyUri, type, description, facilities, bedroom, bathroom, area, price);
                                }
                                else if (propertyUri != null && currentlat != lat && currentlng != lng && lat != 0 && lng != 0){
                                    updateUriGeo(title, address, propertyUri, type, description, facilities, bedroom, bathroom, area, price);
                                }
                            }

                            @Override
                            public void onNegativeClicked(Dialog dialog) {
                                super.onNegativeClicked(dialog);
                            }
                        });
            }
        });
    }

    private void updateUriGeo(String title, String address, Uri propertyUri, String type, String description, String facilities, String bedroom, String bathroom, String area, String price) {
        //init a storage filepath to contain property image
        if (facilities.isEmpty()){
            facilities = currentFac;
        }
        StorageReference propertyImgRef = mStorageReference.child("PropertyImages").child(propertyUri.getLastPathSegment());
        //put property image to path storage / PropertyImages
        propertyImgRef.putFile(propertyUri).addOnSuccessListener(taskSnapshot -> {
            //get property image URL
            Task<Uri> downloadUrl = taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(task -> {
                // assign url link to propertyImage variable
                propertyImage = task.getResult().toString();
                //get current userId
                String currentUserId = mUser.getUid().trim();
                //declare property path
                DatabaseReference propertyRef = mRef.child("Properties");

                //push property detail to real-time database - init a hash map to contain details property
                HashMap<String,Object> property = new HashMap<>();
                property.put(P_ID,pId);
                property.put(P_CREATOR,currentUserId);
                property.put(TITLE,title);
                property.put(LOCATION,address);
                property.put(P_LAT,lat);
                property.put(P_LNG,lng);
                property.put(P_PROVINCE,province);
                property.put(P_POSTALCODE,postalCode);
                property.put(P_DISTRICT,district);
                property.put(P_WARD,ward);
                property.put(P_HN,houseNumber);
                property.put(P_IMG,propertyImage);
                property.put(P_TYPE,type);
                property.put(FLOOR,binding.tietFloor.getText().toString().trim());
                property.put(P_DESCRIPTION,description);
                property.put(P_FACILITIES,handleFacilities());
                property.put(BED,Integer.parseInt(bedroom));
                property.put(BATH,Integer.parseInt(bathroom));
                property.put(AREA,Double.parseDouble(area));
                property.put(PRICE,Long.parseLong(price));
                property.put(TIME, ServerValue.TIMESTAMP);

                //using push method to push data
                propertyRef.child(pId).updateChildren(property).addOnSuccessListener(unused -> {
                    //if create and push data successful
                    Toasty.success(UpdatePropertyActivity.this,"Property is updated successful").show();
                    hideLoading();
                    startActivity(new Intent(UpdatePropertyActivity.this,PropertyActivity.class));
                    Animatoo.animateSlideRight(UpdatePropertyActivity.this);
                }).addOnFailureListener(e -> {
                    Toasty.error(UpdatePropertyActivity.this,"Updated Failed!").show();
                    Log.d(PUSH_DATA, e.getMessage().trim());
                });
            });
        });
    }

    private void updateGeo(String title, String address, Uri propertyUri, String type, String description, String facilities, String bedroom, String bathroom, String area, String price) {
        //get current userId
        String currentUserId = mUser.getUid().trim();
        //declare property path
        DatabaseReference propertyRef = mRef.child("Properties");
        //push property detail to real-time database - init a hash map to contain details property
        if (facilities.isEmpty()){
            facilities = currentFac;
        }
        HashMap<String,Object> property = new HashMap<>();
        property.put(P_ID,pId);
        property.put(P_CREATOR,currentUserId);
        property.put(TITLE,title);
        property.put(LOCATION,address);
        property.put(P_LAT,lat);
        property.put(P_LNG,lng);
        property.put(P_PROVINCE,province);
        property.put(P_POSTALCODE,postalCode);
        property.put(P_DISTRICT,district);
        property.put(P_WARD,ward);
        property.put(P_HN,houseNumber);
        property.put(P_IMG,currentImg);
        property.put(P_TYPE,type);
        property.put(FLOOR,binding.tietFloor.getText().toString().trim());
        property.put(P_DESCRIPTION,description);
        property.put(P_FACILITIES,handleFacilities());
        property.put(BED,Integer.parseInt(bedroom));
        property.put(BATH,Integer.parseInt(bathroom));
        property.put(AREA,Double.parseDouble(area));
        property.put(PRICE,Long.parseLong(price));
        property.put(TIME, ServerValue.TIMESTAMP);

        propertyRef.child(pId).updateChildren(property).addOnCompleteListener(task -> {
            //if create and push data successful
            Toasty.success(UpdatePropertyActivity.this,"Property was updated successful").show();
            hideLoading();
            startActivity(new Intent(UpdatePropertyActivity.this,PropertyActivity.class));
            Animatoo.animateSlideRight(UpdatePropertyActivity.this);
        }).addOnFailureListener(e -> {
            Toasty.error(UpdatePropertyActivity.this,"Updated Failed!").show();
            Log.d(PUSH_DATA, e.getMessage().trim());
        });
    }

    private void updateUri(String title, String address, Uri propertyUri, String type, String description, String facilities, String bedroom, String bathroom, String area, String price) {
        //init a storage filepath to contain property image
        if (facilities.isEmpty()){
            facilities = currentFac;
        }
        StorageReference propertyImgRef = mStorageReference.child("PropertyImages").child(propertyUri.getLastPathSegment());
        //put property image to path storage / PropertyImages
        propertyImgRef.putFile(propertyUri).addOnSuccessListener(taskSnapshot -> {
            //get property image URL
            Task<Uri> downloadUrl = taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(task -> {
                // assign url link to propertyImage variable
                propertyImage = task.getResult().toString();
                //get current userId
                String currentUserId = mUser.getUid().trim();
                //declare property path
                DatabaseReference propertyRef = mRef.child("Properties");
                //push property detail to real-time database - init a hash map to contain details property
                HashMap<String,Object> property = new HashMap<>();
                property.put(P_ID,pId);
                property.put(P_CREATOR,currentUserId);
                property.put(TITLE,title);
                property.put(LOCATION,address);
                property.put(P_LAT,currentlat);
                property.put(P_LNG,currentlng);
                property.put(P_PROVINCE,cProvince);
                property.put(P_POSTALCODE,cPostalcode);
                property.put(P_DISTRICT,cDistritc);
                property.put(P_WARD,cWard);
                property.put(P_HN,cHouseNumber);
                property.put(P_IMG,propertyImage);
                property.put(P_TYPE,type);
                property.put(FLOOR,binding.tietFloor.getText().toString().trim());
                property.put(P_DESCRIPTION,description);
                property.put(P_FACILITIES,handleFacilities());
                property.put(BED,Integer.parseInt(bedroom));
                property.put(BATH,Integer.parseInt(bathroom));
                property.put(AREA,Double.parseDouble(area));
                property.put(PRICE,Long.parseLong(price));
                property.put(TIME, ServerValue.TIMESTAMP);

                //using push method to push data
                propertyRef.child(pId).updateChildren(property).addOnSuccessListener(unused -> {
                    //if create and push data successful
                    Toasty.success(UpdatePropertyActivity.this,"Property is updated successful").show();
                    hideLoading();
                    startActivity(new Intent(UpdatePropertyActivity.this,PropertyActivity.class));
                    Animatoo.animateSlideRight(UpdatePropertyActivity.this);
                }).addOnFailureListener(e -> {
                    Toasty.error(UpdatePropertyActivity.this,"Updated Failed!").show();
                    Log.d(PUSH_DATA, e.getMessage().trim());
                });
            });
        });
    }

    private void updateNoGeo(String title, String address, double lat, double lng, String currentImg, String type, String description, String facilities, String bedroom, String bathroom, String area, String price) {
        //get current userId
        String currentUserId = mUser.getUid().trim();
        //declare property path
        DatabaseReference propertyRef = mRef.child("Properties");
        if (facilities.isEmpty()){
            facilities = currentFac;
        }
        //push property detail to real-time database - init a hash map to contain details property
        HashMap<String,Object> property = new HashMap<>();
        property.put(P_ID,pId);
        property.put(P_CREATOR,currentUserId);
        property.put(TITLE,title);
        property.put(LOCATION,address);
        property.put(P_LAT,currentlat);
        property.put(P_LNG,currentlng);
        property.put(P_PROVINCE,cProvince);
        property.put(P_POSTALCODE,cPostalcode);
        property.put(P_DISTRICT,cDistritc);
        property.put(P_WARD,cWard);
        property.put(P_HN,cHouseNumber);
        property.put(P_IMG,currentImg);
        property.put(P_TYPE,type);
        property.put(FLOOR,binding.tietFloor.getText().toString().trim());
        property.put(P_DESCRIPTION,description);
        property.put(P_FACILITIES,handleFacilities());
        property.put(BED,Integer.parseInt(bedroom));
        property.put(BATH,Integer.parseInt(bathroom));
        property.put(AREA,Double.parseDouble(area));
        property.put(PRICE,Long.parseLong(price));
        property.put(TIME, ServerValue.TIMESTAMP);

        propertyRef.child(pId).updateChildren(property).addOnCompleteListener(task -> {
            //if create and push data successful
            Toasty.success(UpdatePropertyActivity.this,"Property was updated successful").show();
            hideLoading();
            startActivity(new Intent(UpdatePropertyActivity.this,PropertyActivity.class));
            Animatoo.animateSlideRight(UpdatePropertyActivity.this);
        }).addOnFailureListener(e -> {
            Toasty.error(UpdatePropertyActivity.this,"Updated Failed!").show();
            Log.d(PUSH_DATA, e.getMessage().trim());
        });
    }

    private void updateNoUri(String title, String address, String currentImg, String type, String description, String facilities, String bedroom, String bathroom, String area, String price) {
        //get current userId
        String currentUserId = mUser.getUid().trim();
        //declare property path
        DatabaseReference propertyRef = mRef.child("Properties");
        if (facilities.isEmpty()){
            facilities = currentFac;
        }
        //push property detail to real-time database - init a hash map to contain details property
        HashMap<String,Object> property = new HashMap<>();
        property.put(P_ID,pId);
        property.put(P_CREATOR,currentUserId);
        property.put(TITLE,title);
        property.put(LOCATION,address);
        property.put(P_LAT,currentlat);
        property.put(P_LNG,currentlng);
        property.put(P_PROVINCE,cProvince);
        property.put(P_POSTALCODE,cPostalcode);
        property.put(P_DISTRICT,cDistritc);
        property.put(P_WARD,cWard);
        property.put(P_HN,cHouseNumber);
        property.put(P_IMG,currentImg);
        property.put(P_TYPE,type);
        property.put(FLOOR,binding.tietFloor.getText().toString().trim());
        property.put(P_DESCRIPTION,description);
        property.put(P_FACILITIES,handleFacilities());
        property.put(BED,Integer.parseInt(bedroom));
        property.put(BATH,Integer.parseInt(bathroom));
        property.put(AREA,Double.parseDouble(area));
        property.put(PRICE,Long.parseLong(price));
        property.put(TIME, ServerValue.TIMESTAMP);

        propertyRef.child(pId).updateChildren(property).addOnCompleteListener(task -> {
            //if create and push data successful
            Toasty.success(UpdatePropertyActivity.this,"Property was updated successful").show();
            hideLoading();
            startActivity(new Intent(UpdatePropertyActivity.this,PropertyActivity.class));
            Animatoo.animateSlideRight(UpdatePropertyActivity.this);
        }).addOnFailureListener(e -> {
            Toasty.error(UpdatePropertyActivity.this,"Updated Failed!").show();
            Log.d(PUSH_DATA, e.getMessage().trim());
        });


}

    // catch and add selected check box text to string list and otherwise, remove from list if not check
    private String handleFacilities() {
        List<String> facilities = new ArrayList<>(Arrays.asList(currentFac.split(", ")));
        Log.d(TAG_FACILITY,"GET g :"+facilities);
        //init a array list to contain all check box on add new screen
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
        // init a loop to catch which check box is checked -> get text and add to facilities arrayList
        for (customCheckbox item : checkboxes){
                item.setOnCheckedChangeListener((compoundButton, b) -> {
                     if (compoundButton.isChecked()){
                        String selectedItem = compoundButton.getText().toString().trim();
                        Toasty.normal(UpdatePropertyActivity.this,selectedItem,Toasty.LENGTH_LONG).show();
                        // if check box is checked -> add to facilities list
                        facilities.add(selectedItem);
                    }
                    else {
                        //else remove it from that list
                        String uncheckedItem = compoundButton.getText().toString().trim();
                        facilities.remove(uncheckedItem);
                    }
                    // wrap list to string -> assign global variable propertyFacilities
                    if (facilities.size() > 0){
                        for (String fac : facilities){
                            propertyFacilities = TextUtils.join(", ",facilities);
                            Log.d(TAG_FACILITY,"property facilities: "+ facilities);
                        }
                    }

                });

        }
        return propertyFacilities;
    }
    //method move to device gallery and select image
    private void addPropertyImage() {
        //method to intent the device gallery
        binding.propertyImage.setOnClickListener(view -> {
            //declare an intent to move device gallery
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.setType("image/*");
            startActivityForResult(i,GALLERY_CODE);
        });
    }
    //request to access the content at device gallery and set selected image to image view
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("CODE","requestCode: "+requestCode);
        if (requestCode == GALLERY_CODE && resultCode == RESULT_OK){
            propertyUri = data.getData();
            if(propertyUri != null){
                //using Glide lib to set property image into Image view
                Glide.with(this).load(propertyUri)
                        .centerCrop()
                        .into(binding.propertyImage);
            }
            else{
                Log.d("PROPERTY_IMAGE","property image: null");
            }
        }
    }

    private void getPropertyLocation() {
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(UpdatePropertyActivity.this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            //if permission granted
            binding.btnGetLocation.setOnClickListener(view -> {
                @SuppressLint("MissingPermission") Task<Location> task = mFusedLocationProviderClient.getLastLocation();
                task.addOnSuccessListener(location -> {
                    if (location != null){
                        //get geocode of location
                        lat = location.getLatitude();
                        lng = location.getLongitude();

                        //get address line
                        Geocoder geocoder = new Geocoder(UpdatePropertyActivity.this, Locale.getDefault());
                        try {
                            List<Address> addresses = geocoder.getFromLocation(lat,lng,RESULT_CODE);
                            if (addresses!=null && addresses.size() > 0){
                                Address currentAddress = addresses.get(0);
                                StringBuilder stringBuilder = new StringBuilder();
                                Log.d(TAG_LOCATION, "getCurrentAddress: address line: " + stringBuilder.append(currentAddress.getAddressLine(0)));
                                //get address line and assign addressLine
                                String addressLine = String.valueOf(stringBuilder.append(currentAddress.getAddressLine(0)));
                                //set property address line to auto completed text view address
                                binding.propertyLocation.setText(addressLine);
                                //split up house number and street, ward, district, province and postal code -> then, assign to the declared variables
                                //create an array to contain the phrases separated by commas
                                String[] components = addressLine.split(", ");
                                //create an array to contain province and postal code because they doesn't separated by commas
                                String[] provincePostal = components[3].split(" ");
                                //assign value for postalCode variable
                                postalCode = provincePostal[provincePostal.length -1];
                                //get province name
                                String provinceTemp = components[3];
                                province = provinceTemp.substring(0,provinceTemp.length() - postalCode.length());
                                //get district
                                district = components[2];
                                //get ward
                                ward = components[1];
                                //get house number and street
                                houseNumber = components[0];
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    else {
                        showToast(getString(R.string.txt_location_not_found));
                    }
                });

            });
        }else {
            //when permission denied
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_LOCATION_CODE);
        }

    }

    private void textChangeWatcher() {
        //validate property title
        validatePropertyTitle();
        //text watcher add location text input edit text
        validateAddressFields();
        //text watcher add description
        validateDescription();
        //text change watcher - quantity of bedroom and bathroom
        validateRoomQuantity();
        //text change watcher - area and price
        validateAreaPrice();
        //text change watcher - property type
        validateType();
    }

    private void validateAddressFields() {
        binding.propertyLocation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                // if the length of input bigger than 0 means not empty -> error fields is gone
                if (editable.length() > 0){
                    binding.tilPropertyLocation.setErrorEnabled(false);
                }
            }
        });
    }
    //type validate -> prent input not empty
    private void validateType() {
        binding.propertyType.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String input = editable.toString().trim();
                if (!TextUtils.isEmpty(editable)){
                    binding.tilPropertyType.setErrorEnabled(false);
                }
            }
        });
    }

    //text change watcher - area and price
    private void validateAreaPrice() {
        // text change watcher ensure input :
       /* Match zero or more digits; the ? means to match as few as possible before moving onto the next part
        then one digit that's not a zero
        then zero or more digits*/
        //Price
        binding.price.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() > 0 && editable.toString().matches(NUMBER_VALID_FORMAT)){
                    binding.tilPrice.setErrorEnabled(false);
                }

            }
        });
        //Area field
        binding.area.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() > 0 && editable.toString().matches(NUMBER_VALID_FORMAT)){
                    binding.tilArea.setErrorEnabled(false);
                }

            }
        });
    }

    //text change watcher - quantity of bedroom and bathroom
    private void validateRoomQuantity() {
        //bathroom is not empty and equal 0 -> error enable is false means turn off this part
        binding.bathRoom.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() > 0 && !editable.toString().equals("0")){
                    binding.tilBathroom.setErrorEnabled(false);
                }
            }
        });
        //bedroom is not empty and equal 0 -> error enable is false means turn off this part
        binding.bedRoom.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() > 0 && !editable.toString().equals("0")){
                    binding.tilBedroom.setErrorEnabled(false);
                }
            }
        });
    }

    //text change watcher method to catch the right input - property description field
    private void validateDescription() {
        binding.propertyDesciprion.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                //if this fields is not empty -> the error field will remove
                if (editable.length() > 0){
                    binding.tilPropertyDescription.setErrorEnabled(false);
                }

            }
        });
    }
    //text change watcher method to catch the right input - property tittle field
    private void validatePropertyTitle() {
        binding.propertyTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //if input is not empty -> move the error notification
                if (editable.length() > 0){
                    binding.tilPropertyTitle.setErrorEnabled(false);
                }

            }
        });
    }
    private void handleTypeOption() {
        //declare a list String to contain type name
        List<String> types = new ArrayList<>();
        //using database references and access to propertyTypes path on firebase and get data
        mRef.child(TYPE_PATH).addValueEventListener(new ValueEventListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    Log.d(TAG_TYPE,"getTypes: data was found");
                    for (DataSnapshot ds : snapshot.getChildren()){
                        //declare a variable has type is PropertyType
                        PropertyType type;
                        //assign the data that get from firebase to type variable and wrap to PropertyType.class
                        type = ds.getValue(PropertyType.class);
                        //add item to types list that initialized
                        types.add(type.getTypeName());
                    }
                    //declare a array adapter and assign the types to it
                    ArrayAdapter<String> typeAdapter = new ArrayAdapter<String>(UpdatePropertyActivity.this,R.layout.item_dropdownlist,types);
                    //set rounded background for drop down list
                    binding.propertyType.setDropDownBackgroundDrawable(getDrawable(R.drawable.dropdownbg));
                    binding.propertyType.setAdapter(typeAdapter);
                    binding.propertyType.setThreshold(1);
                }else {
                    Log.d(TAG_TYPE,"Snapshot doesn't exists");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void showDetails() {
        //show property details
        Picasso.get().load(property.getPropertyImage()).into(binding.propertyImage);
        binding.propertyTitle.setText(property.getPropertyName());
        binding.propertyLocation.setText(property.getPropertyLocation());
        binding.propertyType.setText(property.getPropertyType());
        binding.tietFloor.setText(property.getPropertyFloor());
        binding.propertyDesciprion.setText(property.getPropertyDescription());
        //set text facilities
        String facilities = property.getPropertyFacilities();
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
        binding.bedRoom.setText(String.valueOf(property.getBedroom()));
        binding.bathRoom.setText(String.valueOf(property.getBathroom()));
        binding.area.setText(String.valueOf(property.getArea()));
        binding.price.setText(String.valueOf(property.getPrice()));
    }


    private void handleToolbar() {
        binding.toolBar.toolBarBack.setOnClickListener(view -> PopupDialog.getInstance(UpdatePropertyActivity.this)
                .setStyle(Styles.STANDARD)
                .setHeading(getString(R.string.txt_title_dialog_leave_update))
                .setHeading(getString(R.string.txt_subtile_dialog_leave_update))
                .setPopupDialogIcon(R.drawable.warning_icon)
                .setCancelable(false)
                .setPositiveButtonText(getString(R.string.txt_yes_yes))
                .setNegativeButtonTextColor(R.color.first)
                .showDialog(new OnDialogButtonClickListener() {
                    @Override
                    public void onPositiveClicked(Dialog dialog) {
                        super.onPositiveClicked(dialog);
                        showLoading();
                        onBackPressed();
                        Animatoo.animateSlideRight(UpdatePropertyActivity.this);
                        onSupportNavigateUp();
                    }
                    @Override
                    public void onNegativeClicked(Dialog dialog) {
                        super.onNegativeClicked(dialog);
                        dialog.dismiss();
                    }
                }));
        binding.toolBar.toolbarTitle.setText(getString(R.string.txt_update_propperty_details));
        binding.toolBar.toolbarTitle.setVisibility(View.VISIBLE);
    }

}