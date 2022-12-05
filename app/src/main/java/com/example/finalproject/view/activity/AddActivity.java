package com.example.finalproject.view.activity;


import static com.example.finalproject.utils.Constants.GALLERY_CODE;
import static com.example.finalproject.utils.Constants.NUMBER_VALID_FORMAT;
import static com.example.finalproject.utils.Constants.REQUEST_LOCATION_CODE;
import static com.example.finalproject.utils.Constants.RESULT_CODE;
import static com.example.finalproject.utils.Constants.TAG_FACILITY;
import static com.example.finalproject.utils.Constants.TAG_LOCATION;
import static com.example.finalproject.utils.Constants.TAG_TYPE;
import static com.example.finalproject.utils.Constants.TYPE_PATH;

import android.Manifest;
import android.annotation.SuppressLint;
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

import com.bumptech.glide.Glide;
import com.example.finalproject.R;
import com.example.finalproject.base.BaseActivity;
import com.example.finalproject.databinding.ActivityAddBinding;
import com.example.finalproject.models.PropertyType;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class AddActivity extends BaseActivity {

    private ActivityAddBinding binding;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private double lat, lng;
    private String propertyAddress;
    private String postalCode;
    private String province;
    private String district;
    private String ward;
    private String propertyImage;
    private Uri propertyUri;
    private String propertyFacilities;

    //Global variable firebase database
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;
    // Cloud storage to contain property image
    private StorageReference mStorageRef;

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        //using binding
        binding = ActivityAddBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //init database references mRef;
        mRef = FirebaseDatabase.getInstance().getReference();
        //init storage references mStorageRef
        mStorageRef = FirebaseStorage.getInstance().getReference();
        //method to get exactly property location -> to prevent the user submit wrong location and geocode that no match actual
        getPropertyLocation();
        //method to add property image
        addPropertyImage();
        //to catch the wrong input or empty input of required fields
        textChangeWatcher();
        //set option for property types field
        handleTypeOption();
        //catch the facilities that selected
        handleFacilities();
        //create new property method
        createNewProperty();
    }

    private void createNewProperty() {
        binding.btnSubmit.setOnClickListener(view -> {
            String title = Objects.requireNonNull(binding.propertyTitle.getText().toString().trim());
            String address = Objects.requireNonNull(binding.propertyLocation.getText().toString());
            String type = Objects.requireNonNull(binding.propertyType.getText().toString());
            String description = Objects.requireNonNull(binding.propertyDesciprion.getText().toString().trim());
            int bedroom = Integer.parseInt(binding.bedRoom.getText().toString());
            int bathroom = Integer.parseInt(binding.bathRoom.getText().toString());
            double area = Double.parseDouble(binding.area.getText().toString());
            long price = Long.parseLong(binding.price.getText().toString());
            //title is empty
            if (title.isEmpty()){
                binding.tilPropertyTitle.setError(getString(R.string.error_empty_required_fields));
                binding.tilPropertyTitle.setErrorEnabled(true);
                binding.tilPropertyTitle.requestFocus();
            }
            //location is empty
            if(address.isEmpty()){
                binding.tilPropertyLocation.setError(getString(R.string.error_empty_property_location));
                binding.tilPropertyLocation.setErrorEnabled(true);
                binding.tilPropertyLocation.requestFocus();
            }

            //Property type is not selected any option
            if(type.isEmpty()){
                binding.tilPropertyType.setError(getString(R.string.error_not_choose_any_option));
                binding.tilPropertyType.setErrorEnabled(true);
                binding.tilPropertyType.requestFocus();
            }

        });
    }

    // catch and add selected check box text to string list and otherwise, remove from list if not check
    private void handleFacilities() {
        List<String> facilities = new ArrayList<>();
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
    }
    //get and set types arrays to auto complete text view property type.
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
                    ArrayAdapter<String> typeAdapter = new ArrayAdapter<String>(AddActivity.this,R.layout.item_dropdownlist,types);
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
    //text change watcher method to catch the right input - address field
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
    //method get property location - to prevent user declare the wrongly location -> fake news
    private void getPropertyLocation() {
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(AddActivity.this);
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
                        Geocoder geocoder = new Geocoder(AddActivity.this, Locale.getDefault());
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
                                propertyAddress = components[0];
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
}




