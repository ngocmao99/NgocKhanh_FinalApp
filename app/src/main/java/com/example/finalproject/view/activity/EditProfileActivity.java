package com.example.finalproject.view.activity;

import static com.example.finalproject.utils.Constants.AVATAR;
import static com.example.finalproject.utils.Constants.DATE_FORMAT;
import static com.example.finalproject.utils.Constants.DEBUG_LOG;
import static com.example.finalproject.utils.Constants.DEFAULT_VALUE;
import static com.example.finalproject.utils.Constants.DISTRICT;
import static com.example.finalproject.utils.Constants.DOB;
import static com.example.finalproject.utils.Constants.FEMALE;
import static com.example.finalproject.utils.Constants.FULLNAME;
import static com.example.finalproject.utils.Constants.GENDER;
import static com.example.finalproject.utils.Constants.HOUSE_NUMBER;
import static com.example.finalproject.utils.Constants.LATITUDE;
import static com.example.finalproject.utils.Constants.LOCAL;
import static com.example.finalproject.utils.Constants.LONGITUDE;
import static com.example.finalproject.utils.Constants.MALE;
import static com.example.finalproject.utils.Constants.NAME_REGEX1;
import static com.example.finalproject.utils.Constants.NAME_REGEX2;
import static com.example.finalproject.utils.Constants.OTHER;
import static com.example.finalproject.utils.Constants.PATH_USER;
import static com.example.finalproject.utils.Constants.PHONE_NUMBER;
import static com.example.finalproject.utils.Constants.PHONE_REGEX;
import static com.example.finalproject.utils.Constants.POSTAL_CODE;
import static com.example.finalproject.utils.Constants.PROVINCE;
import static com.example.finalproject.utils.Constants.PROVINCE_NAME;
import static com.example.finalproject.utils.Constants.RC_IMAGE;
import static com.example.finalproject.utils.Constants.REQUEST_LOCATION_CODE;
import static com.example.finalproject.utils.Constants.TAG_DISTRICT;
import static com.example.finalproject.utils.Constants.TAG_PROVINCE;
import static com.example.finalproject.utils.Constants.USER_ID;
import static com.example.finalproject.utils.Constants.WARD;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.bumptech.glide.Glide;
import com.example.finalproject.R;
import com.example.finalproject.base.BaseActivity;
import com.example.finalproject.databinding.ActivityEditProfileBinding;
import com.example.finalproject.models.District;
import com.example.finalproject.models.Province;
import com.example.finalproject.models.User;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.saadahmedsoft.popupdialog.PopupDialog;
import com.saadahmedsoft.popupdialog.Styles;
import com.saadahmedsoft.popupdialog.listener.OnDialogButtonClickListener;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class EditProfileActivity extends BaseActivity {

    private ActivityEditProfileBinding binding;
    //Image Uri
    private Uri imageUri;
    //Image name
    private String imageName;
    private String uGender;
    //Declare a variable FirebaseStorage
    private StorageReference mStorageRef;
    //Declare a Database Reference variable
    private DatabaseReference mRef;
    //This variable contain the imageName that get from the Firebase Storage
    String userImageId;
    //alert dialog
    private AlertDialog alertDialog;
    //permission granted variable
    private final boolean mLocationPermissionGranted = false;
    //fusedLocationProviderClient
    private FusedLocationProviderClient mFusedLocationProviderClient;
    //location manager
    private LocationManager mLocationManager;
    //Location listener
    private LocationListener mLocationListener;
    //LatLng
    private double lat;
    private double lng;
    private Location currentLocation;
    private String postalCode;
    private String uProvince;
    private String uDistrict;
    private String uWard;
    private String houseNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityEditProfileBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        //init Firebase Storage
        mStorageRef = FirebaseStorage.getInstance().getReference();

        //Init Database Ref
        mRef = FirebaseDatabase.getInstance().getReference();

        //init fusedLocationProviderClient
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(EditProfileActivity.this);

        getLocationPermission();
        showUserInfo();
        selectAvatar();
        validate();
        validateFields();
        handleBackButton();

    }

    private void validateFields(){
        validateDob();
        validateGender();
        validatePhone();
        validateAddress();
        handleProvinces();
    }

    //drop-down menu - Provinces
    private void handleProvinces() {
        List<String> provincesName = new ArrayList<>();
        mRef.child(LOCAL).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Log.d(TAG_PROVINCE, "getProvinceList: data was found ");
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Province mProvince;
                        mProvince = ds.getValue(Province.class);
                        provincesName.add(mProvince.getName());
                    }
                    String province = binding.mactvProvince.getText().toString().trim();
                    ArrayAdapter<String> provinceAdapter = new ArrayAdapter<>(EditProfileActivity.this, R.layout.item_dropdownlist, provincesName);
                    binding.mactvProvince.setAdapter(provinceAdapter);
                    if(!province.isEmpty()){
                        // if province field doesn't empty => get the value and push value to method handleDistrict()
                        handleDistrict(province);
                    }
                    else{
                        binding.mactvProvince.setOnItemClickListener((adapterView, view, i, l) -> {
                            String selectedItem = adapterView.getItemAtPosition(i).toString();
                            uProvince = selectedItem;
                            binding.mactvDist.getText().clear();
                            handleDistrict(selectedItem);

                        });
                    }
                } else {
                    Log.d(TAG_PROVINCE, "getProvinceList: data not found ");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }

    //drop-down menu - District
    private void handleDistrict(String selectedProvince) {
        mRef.child(LOCAL).orderByChild(PROVINCE_NAME).equalTo(selectedProvince).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    Log.d(TAG_PROVINCE, "handleDistrict: getProvinceCode: No data found");
                } else {
                    Log.d(TAG_PROVINCE, "handleDistrict: getProvinceCode: Data was found ");
                    for (DataSnapshot mDs : snapshot.getChildren()) {
                        Province sProvince = mDs.getValue(Province.class);
                        String provinceId = sProvince.getCode();
                        postalCode = sProvince.getZipCode();
                        getDistrictsList(provinceId);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getDistrictsList(String code) {
        List<String> districtNameList = new ArrayList<>();
        mRef.child("Districts").orderByChild("provinceId").equalTo(code).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    Log.d(TAG_DISTRICT, "getDistrict: No data found");
                } else {
                    Log.d(TAG_DISTRICT, "getDistricts: Data was found");
                    for (DataSnapshot mSnapshot : snapshot.getChildren()) {
                        District sDistrict = mSnapshot.getValue(District.class);
                        String name = sDistrict.getName();
                        districtNameList.add(name);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Districts","getDistrictList: was not found");
            }
        });
        //setup drop-down menu for mact district
        ArrayAdapter<String> districtAdapter = new ArrayAdapter<>(this, R.layout.item_dropdownlist, districtNameList);
        binding.mactvDist.setAdapter(districtAdapter);
        binding.mactvDist.setThreshold(1);
        binding.mactvDist.setOnItemClickListener((adapterView, view, i, l) -> {
            uDistrict = adapterView.getItemAtPosition(i).toString();
            binding.tietWard.getText().clear();
        });
    }
    //location permission granted - request to access the location on the user's device
    @SuppressLint("MissingPermission")
    private void getLocationPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_CODE);
        }

    }

    //Show user information
    private void showUserInfo() {
        showLoading();
        //get current user
        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();

        mRef.child(PATH_USER).orderByChild(USER_ID).equalTo(mUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        //Declare a new user object to contain the data get from firebase
                        User user = ds.getValue(User.class);

                        //handle loading the data into screen
                        binding.tietFullName.setText(user.getFullName());
                        binding.tietEmail.setText(user.getEmail());
                        binding.mactvDob.setText(user.getDob());
                        binding.tietWard.setText(user.getWard());
                        binding.tietHNumber.setText(user.getHouseNumber());
                        binding.mactvProvince.setText(user.getProvince());
                        binding.mactvDist.setText(user.getDistrict());

                        //handle phone number
                        String phoneNumber = user.getPhoneNumber().trim();
                        if(!phoneNumber.isEmpty()){
                            binding.tietPhoneNumber.setText(phoneNumber);
                        }


                        //handle user image
                        userImageId = user.getUserImgId().trim();
                        if (!userImageId.isEmpty()) {
                            mStorageRef.child("Avatars/" + userImageId).getDownloadUrl().addOnSuccessListener(uri -> {
                                String imageUrl = uri.toString().trim();

                                Glide.with(EditProfileActivity.this).load(imageUrl).centerCrop().into(binding.userImg);
                            });
                        } else {
                            Glide.with(EditProfileActivity.this)
                                    .load(R.drawable.ic_baseline_photo_camera_24)
                                    .centerInside()
                                    .into(binding.userImg);
                        }

                        //handle user gender
                        String uGender = user.getGender().trim();
                        if (uGender != null) {
                            switch (uGender) {
                                case FEMALE:
                                    binding.rgGender.check(R.id.female);
                                    break;
                                case MALE:
                                    binding.rgGender.check(R.id.male);
                                    break;
                                case OTHER:
                                    binding.rgGender.check(R.id.other);
                                    break;
                            }
                        } else {
                            binding.rgGender.check(R.id.other);
                        }


                    }
                    hideLoading();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(DEBUG_LOG, "No data found.");
            }

        });
    }

    //Validate information before upload them, to expect the issue missing important info.
    private void validate() {
        binding.btnUpdate.setOnClickListener(view -> {
            String fullName = Objects.requireNonNull(binding.tietFullName.getText().toString().trim());
            String phoneNumber = Objects.requireNonNull(binding.tietPhoneNumber.getText().toString().trim());
            String province = Objects.requireNonNull(binding.mactvProvince.getText().toString().trim());
            String district = Objects.requireNonNull(binding.mactvDist.getText().toString().trim());
            String ward = Objects.requireNonNull(binding.tietWard.getText().toString().trim());
            String houseNumbers = Objects.requireNonNull(binding.tietHNumber.getText().toString().trim());

            //prevent empty fullname field and doesn't matches with the name format
            if (TextUtils.isEmpty(fullName)) {
                binding.tilFullName.setErrorEnabled(true);
                binding.tilFullName.setError(getString(R.string.error_name_signup));
                binding.tilFullName.requestFocus();
            }
            //prevent empty phone numver field and doesn't matches with the format
            if (TextUtils.isEmpty(phoneNumber)) {
                binding.tilPhoneNumber.setErrorEnabled(true);
                binding.tilPhoneNumber.setError(getString(R.string.error_empty_phoneNumber));
                binding.tilPhoneNumber.requestFocus();
            } else if (phoneNumber.length() <= 0 && phoneNumber.length() > 12) {
                binding.tilPhoneNumber.setErrorEnabled(true);
                binding.tilPhoneNumber.setError(getString(R.string.error_phonen_number_length));
                binding.tilPhoneNumber.requestFocus();
            } else if (phoneNumber.matches(PHONE_REGEX)) {
                binding.tilPhoneNumber.setErrorEnabled(true);
                binding.tilPhoneNumber.setError(getString(R.string.error_phone_number_format));
                binding.tilPhoneNumber.requestFocus();
            }
            if (TextUtils.isEmpty(fullName) || TextUtils.isEmpty(phoneNumber)) {
                showToast(getString(R.string.error_blank_required_fields));
                binding.tietPhoneNumber.requestFocus();
            }
            //if the properties of address is empty => set lat and lng 0.0d
            if (province.isEmpty() && district.isEmpty() && ward.isEmpty() && houseNumbers.isEmpty()) {
                binding.tilProvince.setErrorEnabled(true);
                binding.tilProvince.setError("District cannot be empty!");

                binding.tilDist.setErrorEnabled(true);
                binding.tilDist.setError("District cannot be empty!");

                binding.tilWard.setErrorEnabled(true);
                binding.tilWard.setError("Ward cannot be empty!");

                binding.tilHNumber.setErrorEnabled(true);
                binding.tilHNumber.setError("House numbers cannot be empty!");
            } else {
                getCoordinates();
            }
        });
    }

    private void validateAddress() {
        binding.mactvProvince.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String province = editable.toString().trim();
                if (editable.length() > 0) {
                    binding.tilProvince.setErrorEnabled(false);
                }
            }
        });

        binding.mactvDist.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String district = editable.toString().trim();
                if (editable.length() > 0) {
                    binding.tilDist.setErrorEnabled(false);
                }

            }
        });

        binding.tietWard.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String ward = editable.toString().trim();
                if (editable.length() > 0) {
                    binding.tilWard.setErrorEnabled(false);
                }

            }
        });

        binding.tietHNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() > 0) {
                    binding.tilHNumber.setErrorEnabled(false);
                }

            }
        });
    }

    private void updateProfile() {
        if (imageUri != null && !userImageId.equals(imageName)) {
            //method upload avatar and info if user provide imageUri
            putImageInStorage();
            // when user provide an new imageUri, the old imageUri that formatted and assigned the old imageName variable- put on Firebase Storage
            //previous will delete and update new imageName
            deletePreviousAvatar();
            hideLoading();
            Log.d(DEBUG_LOG, "ImageUri is existing!");
        } else {
            //method upload avatar and info if users don't provide imageUri
            uploadInfoWithoutImage();
            hideLoading();
            Log.d(DEBUG_LOG, "ImageUri doesn't contain any value");
        }
    }

    //method upload avatar and info if users don't provide imageUri
    private void uploadInfoWithoutImage() {
        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
        String currentUserId = mUser.getUid();
        DatabaseReference userRef = mRef.child(PATH_USER).child(currentUserId);

        HashMap<String, Object> uUser = new HashMap<>();
        uUser.put(FULLNAME, binding.tietFullName.getText().toString().trim());
        uUser.put(DOB, binding.mactvDob.getText().toString().trim());
        uUser.put(PHONE_NUMBER, binding.tietPhoneNumber.getText().toString().trim());
        uUser.put(GENDER, getUserGender());
        uUser.put(AVATAR, userImageId);
        uUser.put(LATITUDE, lat);
        uUser.put(LONGITUDE, lng);
        uUser.put(PROVINCE, binding.mactvProvince.getText().toString().trim());
        uUser.put(POSTAL_CODE, postalCode);
        uUser.put(DISTRICT, binding.mactvDist.getText().toString().trim());
        uUser.put(WARD, binding.tietWard.getText().toString().trim());
        uUser.put(HOUSE_NUMBER, binding.tietHNumber.getText().toString().trim());

        userRef.updateChildren(uUser).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                hideLoading();
                showToast("Updated successful...!");
            } else {
                hideLoading();
                showToast("Update failed..!");
            }
        });
    }

    // Delete the old image
    private void deletePreviousAvatar() {
        StorageReference delRef = mStorageRef.child("Avatars/" + userImageId);

        delRef.delete().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.d(DEBUG_LOG, "Old image was deleted from storage successful.");
            } else {
                Log.d(DEBUG_LOG, "Delete Old Image is failed!");
            }
        });
    }

    // push data to firebase realtime
    private void putUser() {
        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
        String currentUserId = mUser.getUid();
        DatabaseReference userRef = mRef.child(PATH_USER).child(currentUserId);

        HashMap<String, Object> updateInfo = new HashMap<>();
        updateInfo.put(FULLNAME, binding.tietFullName.getText().toString().trim());
        updateInfo.put(DOB, binding.mactvDob.getText().toString().trim());
        updateInfo.put(PHONE_NUMBER, binding.tietPhoneNumber.getText().toString().trim());
        updateInfo.put(GENDER, getUserGender());
        updateInfo.put(AVATAR, imageName);
        updateInfo.put(LATITUDE, lat);
        updateInfo.put(LONGITUDE, lng);
        updateInfo.put(PROVINCE, binding.mactvProvince.getText().toString().trim());
        updateInfo.put(POSTAL_CODE, postalCode);
        updateInfo.put(DISTRICT, binding.mactvDist.getText().toString().trim());
        updateInfo.put(WARD, binding.tietWard.getText().toString().trim());
        updateInfo.put(HOUSE_NUMBER, binding.tietHNumber.getText().toString().trim());

        userRef.updateChildren(updateInfo).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                hideLoading();
                showToast("Updated successful...!");
            } else {
                hideLoading();
                showToast("Update failed..!");
            }
        });
    }

    //get the gender from radio group
    private String getUserGender() {
        String gender = DEFAULT_VALUE;
        //Declare a int variable to contain gender Item Id
        int genderItemId = binding.rgGender.getCheckedRadioButtonId();
        if (genderItemId != -1) {
            //Declare a radio button variable => prepared to get text of the radio button
            RadioButton rb = findViewById(genderItemId);
            //get text checked radio button
            gender = rb.getText().toString();
            return gender;
        } else {
            return OTHER;
        }
    }

    //TextWatcher: phone number
    private void validatePhone() {
        binding.tietPhoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String phoneNumbers = s.toString().trim();
                if (s.length() > 0) {
                    binding.tilPhoneNumber.setErrorEnabled(false);
                } else if (s.length() > 0 && s.length() < 12 || !phoneNumbers.matches(PHONE_REGEX)) {
                    binding.tilPhoneNumber.setErrorEnabled(false);
                }
            }
        });
    }

    //Validate gender radio button group
    @SuppressLint("NonConstantResourceId")
    private void validateGender() {
        binding.rgGender.setOnCheckedChangeListener((group, checkedId) -> {
            if (binding.rgGender.getCheckedRadioButtonId() == -1) {
                binding.tvError.setText(getString(R.string.rbtn_nonselect));
                binding.tvError.setVisibility(View.VISIBLE);
            } else {

                switch (checkedId) {
                    case R.id.female:

                    case R.id.male:

                    case R.id.other:
                        binding.tvError.setVisibility(View.GONE);
                        break;

                }
            }
        });
    }

    //DatePickerDialog - set the maximum date
    private void validateDob() {
        binding.mactvDob.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            int month = calendar.get(Calendar.MONTH);
            int year = calendar.get(Calendar.YEAR);
            DatePickerDialog datePickerDialog = new DatePickerDialog(EditProfileActivity.this, (datePicker, i, i1, i2) -> {
                calendar.set(i, i1, i2);
                @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT);
                binding.mactvDob.setText(simpleDateFormat.format(calendar.getTime()));
            }, year, month, day);
            //set up the maximum date is current date.
            datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
            datePickerDialog.show();
        });
    }

    //Text Watcher: full name
    private void validateFullName() {
        binding.tietFullName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String userName = s.toString().trim();
                if (s.length() > 0) {
                    binding.tilFullName.setErrorEnabled(false);
                } else if (userName.matches(NAME_REGEX1) || !userName.matches(NAME_REGEX2)) {
                    binding.tilFullName.setErrorEnabled(false);
                }
            }
        });
    }

    //upload Avatar feature
    private void selectAvatar() {
        binding.userImg.setOnClickListener(view -> {
            //Select Image feature
            //Create an intent to access gallery of device
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, RC_IMAGE);
        });

    }

    //startActivityForResult(intent,RC_IMAGE);
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_IMAGE && data != null && data.getData() != null) {
            //Set imageUri is getData()
            imageUri = data.getData();
            //set imageUri to the ImageView
            Glide.with(EditProfileActivity.this).load(imageUri).centerCrop().into(binding.userImg);
        }
    }

    //put image to Firebase Storage feature
    private void putImageInStorage() {
        //Create formatter to apply into image name
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.CANADA);
        Date now = new Date();
        imageName = formatter.format(now);
        //put image process using Storage Ref
        mStorageRef.child("Avatars/" + imageName).putFile(imageUri).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                task.getResult().getMetadata().getReference().getDownloadUrl().addOnSuccessListener(uri -> {
                    String imageUrl = Objects.requireNonNull(uri.toString());
                    //Set avatar by using Glide
                    Glide.with(EditProfileActivity.this).load(imageUrl).centerCrop().into(binding.userImg);
                    showToast("Successful uploaded avatar with name: " + imageName + "\n DownloadURL: " + imageUrl);
                    //If the user avatar upload on the Storage successful, the process edit information will implement
                    putUser();
                });
            } else {
                hideLoading();
                showToast("Failed upload image!");
            }
        });
    }

    //get location coordinates from address line
    private void getCoordinates() {
        String inputWard = binding.tietWard.getText().toString().trim();
        String inputHouseNumbers = binding.tietHNumber.getText().toString().trim();
        // get address
        String address = inputHouseNumbers + ", " + inputWard + ", " + uDistrict + " ," + uProvince + " " + postalCode + ", " + "Vietnam";

        Geocoder geoCoder = new Geocoder(EditProfileActivity.this, Locale.getDefault());
        try {
            List<Address> addressList = geoCoder.getFromLocationName(address, 1);
            if (addressList != null && addressList.size() > 0) {
                Address uAddress = addressList.get(0);
                lat = uAddress.getLatitude();
                lng = uAddress.getLongitude();
                Log.d("Coordinates", "The user location coordinates: Lat: " + lat + ", Long: " + lng);

                PopupDialog.getInstance(EditProfileActivity.this)
                        .setStyle(Styles.STANDARD)
                        .setHeading("Update information")
                        .setHeading("Are you sure you want to update these information?")
                        .setPopupDialogIcon(R.drawable.warning_icon)
                        .setCancelable(false)
                        .setNegativeButtonTextColor(R.color.first)
                        .showDialog(new OnDialogButtonClickListener() {
                            @Override
                            public void onPositiveClicked(Dialog dialog) {
                                super.onPositiveClicked(dialog);
                                showLoading();
                                updateProfile();
                            }

                            @Override
                            public void onNegativeClicked(Dialog dialog) {
                                super.onNegativeClicked(dialog);
                                dialog.dismiss();
                            }
                        });

                binding.tilProvince.setErrorEnabled(false);
                binding.tilDist.setErrorEnabled(false);
                binding.tilWard.setErrorEnabled(false);
                binding.tilHNumber.setErrorEnabled(false);
            } else {
                showToast("Your address is invalid. Please re-check and fill out again!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    // Back button on tool bar
    private void handleBackButton() {
        binding.toolBar.toolBarBack.setOnClickListener(view -> {
            onBackPressed();
            Animatoo.animateSlideRight(EditProfileActivity.this);
            onSupportNavigateUp();
        });
        binding.toolBar.toolbarTitle.setText(getString(R.string.txt_account_infomation));
        binding.toolBar.toolbarTitle.setVisibility(View.VISIBLE);
    }

}

